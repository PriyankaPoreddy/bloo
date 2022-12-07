package uk.ac.tees.w9585141.blooplus.DoctorDetails;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import uk.ac.tees.w9585141.blooplus.R;

public class EditDoctorProfile extends AppCompatActivity {

    private TextView dName, dEmail, dSpecialization, dExperiance, dAge, dContact, dAddress, dEducation;
    private Toolbar dToolbar;

    private String name,specialization,experiance,education,email,age,contact,address,update;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Doctor_Details");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctor_profile);

        //Toolbar
        dToolbar = (Toolbar) findViewById(R.id.doctor_editProfile_toolbar);
        setSupportActionBar(dToolbar);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        dName = (TextView) findViewById(R.id.edit_dr_name);
        dSpecialization = (TextView) findViewById(R.id.edit_dr_specialization);
        dExperiance = (TextView) findViewById(R.id.edit_dr_experiance);
        dEducation = (TextView) findViewById(R.id.edit_dr_education);
        dEmail = (TextView) findViewById(R.id.edit_dr_email);
        dAge = (TextView) findViewById(R.id.edit_dr_age);
        dContact = (TextView) findViewById(R.id.edit_doctor_contact);
        dAddress = (TextView) findViewById(R.id.edit_doctor_address);

        name = getIntent().getStringExtra("Name").toString();
        specialization = getIntent().getStringExtra("Specialization").toString();
        experiance = getIntent().getStringExtra("Experiance").toString();
        education = getIntent().getStringExtra("Education").toString();
        email = getIntent().getStringExtra("Email").toString();
        age = getIntent().getStringExtra("Age").toString();
        contact = getIntent().getStringExtra("Contact").toString();
        address = getIntent().getStringExtra("Address").toString();

        dName.setText(name);
        dSpecialization.setText(specialization);
        dExperiance.setText(experiance);
        dEducation.setText(education);
        dEmail.setText(email);
        dAge.setText(age);
        dContact.setText(contact);
        dAddress.setText(address);
    }

    public void update(View view){

        switch (view.getId()){

            case R.id.edit_drname:
                alertDialog(name,"Name");
                break;

            case R.id.edit_drexperiance:
                alertDialog(experiance,"Experience");
                break;

            case R.id.edit_dreducation:
                alertDialog(education,"Education");
                break;
            case R.id.edit_drAddress:
                alertDialog(address,"Address");
                break;
            case R.id.edit_drage:
                alertDialog(age,"Age");
                break;
            case R.id.edit_drcontact:
                alertDialog(contact,"Contact");
                break;

            case R.id.final_update:
                updateDoctorProfile();
                break;

            default:
                break;
        }

    }

    private void updateDoctorProfile() {

        String currentUser = mAuth.getCurrentUser().getUid().toString();

        mDatabase.child(currentUser).child("Name").setValue(name);
        mDatabase.child(currentUser).child("Experiance").setValue(experiance);
        mDatabase.child(currentUser).child("Education").setValue(education);
        mDatabase.child(currentUser).child("Address").setValue(address);
        mDatabase.child(currentUser).child("Contact").setValue(contact);
        mDatabase.child(currentUser).child("Age").setValue(age);

        startActivity(new Intent(EditDoctorProfile.this, DoctorProfile.class));



    }

    private void alertDialog(String text, final String detail){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //need to fix this............
        View view = getLayoutInflater().inflate(R.layout.updatefield, null);

        //need to fix this............
        TextView textView = (TextView) view.findViewById(R.id.update_View);
        final EditText editText = (EditText) view.findViewById(R.id.editText);

        textView.setText(detail);
        editText.setText(text, TextView.BufferType.EDITABLE);

        builder.setView(view);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                update = editText.getText().toString();

                if(detail.equals("Name")){
                    dName.setText(update);
                    name = dName.getText().toString();
                }
                else if(detail.equals("Experience")){
                    dExperiance.setText(update);
                    experiance = dExperiance.getText().toString();
                }
                else if(detail.equals("Education")){
                    dEducation.setText(update);
                    education = dEducation.getText().toString();
                }
                else if(detail.equals("Address")){
                    dAddress.setText(update);
                    address = dAddress.getText().toString();
                }
                else if(detail.equals("Age")){
                    dAge.setText(update);
                    age = dAge.getText().toString();
                }
                else if(detail.equals("Contact")){
                    dContact.setText(update);
                    contact = dContact.getText().toString();
                }
                else {

                }

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}