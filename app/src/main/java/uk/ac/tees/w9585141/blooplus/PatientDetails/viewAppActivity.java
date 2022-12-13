package uk.ac.tees.w9585141.blooplus.PatientDetails;

import static uk.ac.tees.w9585141.blooplus.PatientDetails.EditProfileActivity.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import uk.ac.tees.w9585141.blooplus.R;
import uk.ac.tees.w9585141.blooplus.auth.SignupActivity;
import uk.ac.tees.w9585141.blooplus.home.HomeActivity;

public class viewAppActivity extends AppCompatActivity {

    TextView name, date, text;

    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseFirestore fstore= FirebaseFirestore.getInstance();
    DocumentReference docref;
    String curUid;

    String dname, dDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_app);
        fAuth=FirebaseAuth.getInstance();

        name= findViewById(R.id.va_docName);
        date= findViewById(R.id.va_date);
        text=findViewById(R.id.bookedText);

        dname = getIntent().getStringExtra("DoctorUserId");
        dDate = getIntent().getStringExtra("Date");
        name.setText(dname);
        date.setText(dDate);

//        name.setText(getIntent().getStringExtra("DoctorUserId"));
//       date.setText(getIntent().getStringExtra("Date"));
       text.setText("Appointment Booked Successfully");


       if(HomeActivity.flag==false) uploadToDB();
       String t ;
        curUid= fAuth.getCurrentUser().getUid();
        docref=fstore.collection("Booked_Appointments").document(curUid);



        docref.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.getResult().exists()){

                            String fnameResult= task.getResult().getString("dName");
                            String dateOn = task.getResult().getString("dDate");

                            name.setText(fnameResult);
                            date.setText(dateOn);


                        }else {

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });


    }

    private void uploadToDB() {

   if(fAuth.getCurrentUser()!=null){
            curUid= fAuth.getCurrentUser().getUid();
        }

       // curUid= fAuth.getCurrentUser().getUid();
        docref=fstore.collection("Booked_Appointments").document(curUid);
        Map<String, Object> app = new HashMap<>();
        app.put("dName", dname);
        app.put("dDate",dDate);

        docref.set(app).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("TAG", "onsucces: appointment details created successfully " + curUid);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","onFailure"+e.getMessage());
            }
        });



    }
}