package uk.ac.tees.w9585141.blooplus.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import uk.ac.tees.w9585141.blooplus.R;

public class ForgotPswrdScreen extends AppCompatActivity {

    Button frgtOk;
    ImageView frgetback;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pswrd_screen);

        frgtOk.findViewById(R.id.frgtButton);
        frgtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        frgetback.findViewById(R.id.forgetback);
        frgetback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPswrdScreen.this, LoginActivity.class);
                startActivity(intent);

            }
        });


    }
}