package uk.ac.tees.w9585141.blooplus.home;

import static uk.ac.tees.w9585141.blooplus.R.id.home_layOut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import uk.ac.tees.w9585141.blooplus.AboutActivity;
import uk.ac.tees.w9585141.blooplus.DoctorDetails.DoctorProfile;
import uk.ac.tees.w9585141.blooplus.DoctorDetails.showDocAppointmentActivity;
import uk.ac.tees.w9585141.blooplus.DocumentsPage;
import uk.ac.tees.w9585141.blooplus.KeyboardUtils;
import uk.ac.tees.w9585141.blooplus.PatientDetails.Myprofile;
import uk.ac.tees.w9585141.blooplus.PatientDetails.patientViewAppActivity;
import uk.ac.tees.w9585141.blooplus.PatientDetails.viewAppActivity;
import uk.ac.tees.w9585141.blooplus.R;
import uk.ac.tees.w9585141.blooplus.auth.LoginActivity;
import uk.ac.tees.w9585141.blooplus.auth.SignupActivity;
import uk.ac.tees.w9585141.blooplus.splashScreen;

public class HomeActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    public static boolean flag=false;

    private String Type = "", status = "";

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    //Firebase Auth
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference();
    String uid;



    public static void launch(Activity context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Book Appointment");

        //DrawerLayout and ToggleButton
        mDrawerLayout = findViewById(R.id.home_layOut);
        mToggle = new ActionBarDrawerToggle(HomeActivity.this, mDrawerLayout, R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       // Toast.makeText(this, FirebaseAuth.getInstance().getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
        //NavigationView
        mNavigationView = findViewById(R.id.main_nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        //TabLayout , SectionPagerAdapter & ViewPager
        mViewPager = findViewById(R.id.main_ViewPager);
        mTabLayout = findViewById(R.id.home_tabLayout);
        mTabLayout.addTab(mTabLayout.newTab().setText("Date"));
        mTabLayout.addTab(mTabLayout.newTab().setText("DoctorList"));
     //   mTabLayout.addTab(mTabLayout.newTab().setText("Specialize"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final pagerAdapter adapter = new pagerAdapter(this,getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mTabLayout.setupWithViewPager(mViewPager);

    }


    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        Menu menuNav = mNavigationView.getMenu();
        final MenuItem nav_profile = menuNav.findItem(R.id.menu_profile);
        final MenuItem nav_ShowAppointment = menuNav.findItem(R.id.menu_showAppointment);
        final MenuItem nav_BookedAppointment = menuNav.findItem(R.id.menu_bookedAppointment);
        MenuItem nav_logOut = menuNav.findItem(R.id.menu_logout);
        MenuItem nav_logIn = menuNav.findItem(R.id.menu_login);


//        nav_profile.setVisible(false);
//        nav_ShowAppointment.setVisible(false);
//        nav_BookedAppointment.setVisible(false);

//        nav_logOut.setVisible(false);


        // Check if user is signed in  or not
        if (currentUser == null) {
            nav_logIn.setVisible(true);

            View mView = mNavigationView.getHeaderView(0);
            TextView userName = (TextView) mView.findViewById(R.id.header_drname);
            TextView userEmail = (TextView) mView.findViewById(R.id.header_drEmail);


            userName.setText("User Name");
            userEmail.setText("User Email");

            Toast.makeText(getBaseContext(), "Your Account is not Logged In ", Toast.LENGTH_LONG).show();
        } else {
            nav_logIn.setVisible(false);
            nav_logOut.setVisible(true);
            showDetails();
           getDetails();

        }
    }

    private void showDetails() {
        uid = mAuth.getUid();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid =user.getUid();
        }

        FirebaseFirestore fstore= FirebaseFirestore.getInstance();
        DocumentReference ref= fstore.collection("user").document(uid);
        ref.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.getResult().exists()){

                            String fnameResult= task.getResult().getString("fName");
                            String emailResult= task.getResult().getString("email");

                            View mView = mNavigationView.getHeaderView(0);
                            TextView userName = (TextView) mView.findViewById(R.id.header_drname);
                            TextView userEmail = (TextView) mView.findViewById(R.id.header_drEmail);

                            userName.setText(fnameResult);
                            userEmail.setText(emailResult);


                        }else {
                            Log.d("TAG", "onComplete: ");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("HomeActivity", "onFailure: " + e.toString());
                    }
                });

    }

    private void getDetails() {

        Menu menuNav = mNavigationView.getMenu();
        final MenuItem menu_profile = menuNav.findItem(R.id.menu_profile);
        final MenuItem menu_ShowAppointment = menuNav.findItem(R.id.menu_showAppointment);
        final MenuItem menu_BookedAppointment = menuNav.findItem(R.id.menu_bookedAppointment);

//        mUserDatabase.child("User_Type").child(uid).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Type = String.valueOf(dataSnapshot.child("Type").getValue());
//                status = String.valueOf(dataSnapshot.child("Status").getValue());
//
//                if (Type.equals("Patient")) {
//                    menu_BookedAppointment.setVisible(true);
//
//
//                    mUserDatabase.child("user").child(uid).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            String name = dataSnapshot.child("fName").getValue().toString();
//                            String email = dataSnapshot.child("email").getValue().toString();
//
//                            View mView = mNavigationView.getHeaderView(0);
//                            TextView userName = (TextView) mView.findViewById(R.id.header_drname);
//                            TextView userEmail = (TextView) mView.findViewById(R.id.header_drEmail);
//
//                            userName.setText(name);
//                            userEmail.setText(email);
//
//                            Toast.makeText(HomeActivity.this, "Your Are Logged In", Toast.LENGTH_SHORT).show();
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                } else if (Type.equals("Doctor") && status.equals("Approved")) {
//                    menu_profile.setVisible(true);
//                    menu_ShowAppointment.setVisible(true);
//                    menu_BookedAppointment.setVisible(true);
//
//                    mUserDatabase.child("user").child(uid).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//
//                            String name = dataSnapshot.child("fName").getValue().toString();
//                            String email = dataSnapshot.child("email").getValue().toString();
//
//                            View mView = mNavigationView.getHeaderView(0);
//                            TextView userName = (TextView) mView.findViewById(R.id.header_drname);
//                            TextView userEmail = (TextView) mView.findViewById(R.id.header_drEmail);
//
//                            userName.setText(name);
//                            userEmail.setText(email);
//
//                            Toast.makeText(HomeActivity.this, "Your Are Logged In", Toast.LENGTH_SHORT).show();
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                } else {
//                    Toast.makeText(HomeActivity.this, "You are not authorized for this facility or Account Under Pending", Toast.LENGTH_SHORT).show();
//                    FirebaseAuth.getInstance().signOut();
//                    onStart();
//                }
//            }
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(HomeActivity.this, databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_profile:
                launchScreen(Myprofile.class);
//                startActivity(new Intent(HomeActivity.this, Myprofile.class));
                finish();
                break;



            case R.id.menu_bookedAppointment:
                //launchScreen(BookAppointment.class);
                flag=true;
                startActivity(new Intent(HomeActivity.this, viewAppActivity.class));
                break;

            case R.id.menu_login:
                launchScreen(LoginActivity.class);
                finish();
                break;

            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, splashScreen.class));
                finish();
                break;

            case R.id.menu_Documents:
                startActivity(new Intent(HomeActivity.this, DocumentsPage.class));
                finish();
                break;
            case R.id.menu_aboutapp:
                //startActivity(new Intent(HomeActivity.this, Myprofile.class));
                launchScreen(AboutActivity.class);
                finish();
                break;
            default:
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void launchScreen(Class<?> activity) {
        hideKeyboard();
        Intent intent = new Intent(HomeActivity.this, activity);
        startActivity(intent);
    }

    private void hideKeyboard() {
        KeyboardUtils.hideKeyboard(HomeActivity.this);
    }
    }
