package uk.ac.tees.w9585141.blooplus.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uk.ac.tees.w9585141.blooplus.DoctorDetails.DoctorProfile;
import uk.ac.tees.w9585141.blooplus.DoctorDetails.showDocAppointmentActivity;
import uk.ac.tees.w9585141.blooplus.KeyboardUtils;
import uk.ac.tees.w9585141.blooplus.PatientDetails.Myprofile;
import uk.ac.tees.w9585141.blooplus.PatientDetails.patientViewAppActivity;
import uk.ac.tees.w9585141.blooplus.R;
import uk.ac.tees.w9585141.blooplus.auth.LoginActivity;

public class HomeActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;

    private String Type = "", status = "";

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private pagerAdapter mSectionPagerAdapter;



    //Firebase Auth
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference();


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

        //NavigationView
        mNavigationView = findViewById(R.id.main_nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        //TabLayout , SectionPagerAdapter & ViewPager
        mViewPager = findViewById(R.id.main_ViewPager);
        mSectionPagerAdapter = new pagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                hideKeyboard();
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {
                hideKeyboard();
            }
        });

        mTabLayout = findViewById(R.id.home_tabLayout);
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

        nav_profile.setVisible(false);
        nav_ShowAppointment.setVisible(false);
        nav_BookedAppointment.setVisible(false);
        nav_logIn.setVisible(false);
        nav_logOut.setVisible(false);


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
            nav_logOut.setVisible(true);
            chechType();
        }
    }

    private void chechType() {

        Menu menuNav = mNavigationView.getMenu();
        final MenuItem menu_profile = menuNav.findItem(R.id.menu_profile);
        final MenuItem menu_ShowAppointment = menuNav.findItem(R.id.menu_showAppointment);
        final MenuItem menu_BookedAppointment = menuNav.findItem(R.id.menu_bookedAppointment);

        menu_profile.setVisible(false);
        menu_ShowAppointment.setVisible(false);
        menu_BookedAppointment.setVisible(false);

        final String uid = mAuth.getUid().toString();

        mUserDatabase.child("User_Type").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Type = (String) dataSnapshot.child("Type").getValue();
                status = (String) dataSnapshot.child("Status").getValue();

                if (Type.equals("Patient")) {
                    menu_BookedAppointment.setVisible(true);


                    mUserDatabase.child("Patient_Details").child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.child("Name").getValue().toString();
                            String email = dataSnapshot.child("Email").getValue().toString();

                            View mView = mNavigationView.getHeaderView(0);
                            TextView userName = (TextView) mView.findViewById(R.id.header_drname);
                            TextView userEmail = (TextView) mView.findViewById(R.id.header_drEmail);

                            userName.setText(name);
                            userEmail.setText(email);

                            Toast.makeText(HomeActivity.this, "Your Are Logged In", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else if (Type.equals("Doctor") && status.equals("Approved")) {
                    menu_profile.setVisible(true);
                    menu_ShowAppointment.setVisible(true);
                    menu_BookedAppointment.setVisible(true);

                    mUserDatabase.child("Doctor_Details").child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String name = dataSnapshot.child("Name").getValue().toString();
                            String email = dataSnapshot.child("Email").getValue().toString();

                            View mView = mNavigationView.getHeaderView(0);
                            TextView userName = (TextView) mView.findViewById(R.id.header_drname);
                            TextView userEmail = (TextView) mView.findViewById(R.id.header_drEmail);

                            userName.setText(name);
                            userEmail.setText(email);

                            Toast.makeText(HomeActivity.this, "Your Are Logged In", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(HomeActivity.this, "You are not authorized for this facility or Account Under Pending", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    onStart();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

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
                launchScreen(DoctorProfile.class);
                startActivity(new Intent(HomeActivity.this, Myprofile.class));

                break;

            case R.id.menu_showAppointment:
                launchScreen(showDocAppointmentActivity.class);
                break;

            case R.id.menu_bookedAppointment:
                launchScreen(patientViewAppActivity.class);
                break;

            case R.id.menu_login:
                launchScreen(LoginActivity.class);
                break;

            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                Toast.makeText(getBaseContext(), "Successfully Logged Out", Toast.LENGTH_LONG).show();
                onStart();
                break;

            case R.id.menu_aboutapp:
                startActivity(new Intent(HomeActivity.this, Myprofile.class));
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
