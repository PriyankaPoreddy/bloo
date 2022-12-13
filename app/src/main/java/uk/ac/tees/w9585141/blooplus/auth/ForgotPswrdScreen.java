package uk.ac.tees.w9585141.blooplus.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.ac.tees.w9585141.blooplus.R;

public class ForgotPswrdScreen extends AppCompatActivity {

    Button frgtOk;
    ImageView frgetback;
    EditText email;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pswrd_screen);

        auth= FirebaseAuth.getInstance();
        email= findViewById(R.id.editTextTextPersonName3);

        frgtOk.findViewById(R.id.frgtButton);
        frgtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetpassword();
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

    private void resetpassword() {
        String email = this.email.getText().toString().trim();

        if (email.isEmpty()) {
            this.email.setError("Email is required");
            this.email.requestFocus();
            return;
        }
        Pattern regexPattern = Pattern.compile("^(.+)@(.+)$ ");
        Matcher matcher = regexPattern.matcher(email);

        if (matcher.find()) {
            this.email.setError("Please provide a valid email");
            this.email.requestFocus();
            return;
        }
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPswrdScreen.this, "Check your email to reset your password", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ForgotPswrdScreen.this, "Try again! Something wrong happened!", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}