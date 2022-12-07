package uk.ac.tees.w9585141.blooplus.PatientDetails;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import uk.ac.tees.w9585141.blooplus.R;
import uk.ac.tees.w9585141.blooplus.auth.LoginActivity;
import uk.ac.tees.w9585141.blooplus.auth.SignupActivity;

public class Myprofile extends AppCompatActivity {

    private static final String TAG = "TAG";

    TextView pfname,plname,pPhn,pemail,pPassword,logOut,edit;
    ImageView profilePick;
    Button upSave;
    ProgressBar progressbar;

    String CurrId;
    DocumentReference ref;
//    FirebaseUser user;
//    FirebaseAuth fAuth;
//    FirebaseFirestore fStore;
    FirebaseStorage fstorage;
    StorageReference stref;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);


        fstorage= FirebaseStorage.getInstance();
        stref= fstorage.getReference();


//        fullName= findViewById(R.id.mp_fullname);
//        profilePick= findViewById(R.id.profilePic);
        pfname = findViewById(R.id.mp_fname);
        plname=findViewById(R.id.mp_lname);
        pemail=findViewById(R.id.mp_email);
        pPhn= findViewById(R.id.mp_phn);
        pPassword=findViewById(R.id.mp_password);

//        fAuth= FirebaseAuth.getInstance();
//        fStore= FirebaseFirestore.getInstance();


        edit= findViewById(R.id.mp_edit);

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