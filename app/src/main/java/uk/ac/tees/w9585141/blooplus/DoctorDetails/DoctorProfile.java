package uk.ac.tees.w9585141.blooplus.DoctorDetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import uk.ac.tees.w9585141.blooplus.R;

public class DoctorProfile extends AppCompatActivity {

    private TextView dName, dEmail, dSpecialization, dExperiance, dAge, dContact, dAddress, dEducation;
    private Button dShowAvailPlanButton, dEditProfileButton;
    private Toolbar mToolbar;

    private String name, specialization, experiance, education, email, age, contact, address, shift;

    private DatabaseReference mDoctorDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        dName = (TextView) findViewById(R.id.dr_name);
        dSpecialization = (TextView) findViewById(R.id.dr_specialization);
        dExperiance = (TextView) findViewById(R.id.dr_experience);
        dEducation = (TextView) findViewById(R.id.dr_education);
        dEmail = (TextView) findViewById(R.id.dr_email);
        dAge = (TextView) findViewById(R.id.dr_age);
        dContact = (TextView) findViewById(R.id.dr_contact);
        dAddress = (TextView) findViewById(R.id.dr_address);

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.doctor_profile_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dName = (TextView) findViewById(R.id.dr_name);

        dShowAvailPlanButton = (Button) findViewById(R.id.show_AvailPlan_button);
        dShowAvailPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogBox();
            }
        });

        dEditProfileButton = (Button) findViewById(R.id.edit_drprofile_button);
        dEditProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorProfile.this, EditDoctorProfile.class);
                intent.putExtra("Name", name);
                intent.putExtra("Specialization", specialization);
                intent.putExtra("Experiance", experiance);
                intent.putExtra("Education", education);
                intent.putExtra("Email", email);
                intent.putExtra("Age", age);
                intent.putExtra("Contact", contact);
                intent.putExtra("Address", address);
                startActivity(intent);
            }
        });
    }

    private void alertDialogBox() {


    }
}