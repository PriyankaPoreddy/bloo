package uk.ac.tees.w9585141.blooplus;
// test

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class userProfileActivity extends AppCompatActivity {

    TextView logOut;
    ImageView back;
    ImageView profilePick;
    Button upSave;
    ProgressBar progressbar;
    TextView edit, changePwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        upSave = findViewById(R.id.uprofile_save);
        logOut = findViewById(R.id.toolbarLogOut);
        progressbar = findViewById(R.id.uprofile_prgrsbar);
        edit = findViewById(R.id.uprofile_edit);
        changePwd = findViewById(R.id.uprofile_editPwd);

        upSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

//        profilePick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("ProfilePic/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent, PICK_IMAGE);
//            }
//        });




        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(userProfileActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        back = findViewById(R.id.profileBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userProfileActivity.this, doctorListing.class);
                startActivity(intent);
            }
        });


//        upSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                upFname=(TextView)findViewById(R.id.uprofile_fname);
//                upFname.setText(sa.FirstName);
//
//                upLname=(TextView) findViewById(R.id.uprofile_lname);
//                upLname.setText(sa.Lastname);
//
//                upEmail=(TextView) findViewById(R.id.uprofile_email);
//                upEmail.setText(sa.email);
//
//            }
//        });


    }
}

