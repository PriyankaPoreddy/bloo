package uk.ac.tees.w9585141.blooplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class userProfileActivity extends AppCompatActivity {

    TextView logOut;
    ImageView back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        logOut= findViewById(R.id.toolbarLogOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(userProfileActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        back= findViewById(R.id.profileBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(userProfileActivity.this, doctorListing.class);
                startActivity(intent);
            }
        });



    }
}