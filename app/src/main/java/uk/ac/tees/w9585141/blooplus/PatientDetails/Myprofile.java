package uk.ac.tees.w9585141.blooplus.PatientDetails;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.w9585141.blooplus.R;
import uk.ac.tees.w9585141.blooplus.auth.LoginActivity;
import uk.ac.tees.w9585141.blooplus.auth.SignupActivity;
import uk.ac.tees.w9585141.blooplus.home.HomeActivity;

public class Myprofile extends AppCompatActivity {

    private static final String TAG = "TAG";

    TextView pfname,plname,pPhn,pemail,pPassword,logOut,edit;
    ImageView profilePick,backButton;
    Button upSave;
    ProgressBar progressbar;

    String CurrId;
    DocumentReference ref;
    FirebaseStorage fstorage;
    StorageReference stref;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);


        fstorage= FirebaseStorage.getInstance();
        stref= fstorage.getReference();
        pfname = findViewById(R.id.mp_fname);
        plname=findViewById(R.id.mp_lname);
        pemail=findViewById(R.id.mp_email);
        pPhn= findViewById(R.id.mp_phn);
        pPassword=findViewById(R.id.mp_password);
        edit= findViewById(R.id.mp_edit);
        profilePick=findViewById(R.id.profileImage2);
        backButton= findViewById(R.id.mp_menu_backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Myprofile.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        StorageReference file=FirebaseStorage.getInstance().getReference("images/"+FirebaseAuth.getInstance().getCurrentUser().getUid());
        file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // adding the url in the arraylist
                Glide.with(getApplicationContext()).load(uri.toString()).into(profilePick);
            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Myprofile.this, EditProfileActivity.class);

                intent.putExtra("fName",pfname.getText().toString());
                intent.putExtra("Lname",plname.getText().toString());
                intent.putExtra("email",pemail.getText().toString());
                intent.putExtra("phone",pPhn.getText().toString());
                intent.putExtra("password",pPassword.getText().toString());
                startActivity(intent);

            }
        });

        logOut= findViewById(R.id.mp_LogOut);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Myprofile.this, LoginActivity.class);
                startActivity(intent);

            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            CurrId=user.getUid();
        }


        FirebaseFirestore fstore= FirebaseFirestore.getInstance();
        ref= fstore.collection("user").document(CurrId);
        ref.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.getResult().exists()){

                            String fnameResult= task.getResult().getString("fName");
                            String lnameResult= task.getResult().getString("Lname");
                            String emailResult= task.getResult().getString("email");
                            String phnResult= task.getResult().getString("phone");
                            String passwordResult= task.getResult().getString("password");
                            pfname.setText(fnameResult);
                            plname.setText(lnameResult);
                            pemail.setText(emailResult);
                            pPhn.setText(phnResult);
                            pPassword.setText(passwordResult);
                        }else {
                            Intent intent= new Intent(Myprofile.this, SignupActivity.class);
                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });

    }
}