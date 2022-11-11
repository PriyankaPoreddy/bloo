package uk.ac.tees.w9585141.blooplus;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button signup;
    Button login;
    String textEmail;
    String textPwd;
    private EditText emailAd,Paswd;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Login");

        emailAd= findViewById(R.id.emailAddressLogin);
        Paswd= findViewById(R.id.editPasswordLogin);

        authProfile= FirebaseAuth.getInstance();


        signup = findViewById(R.id.SignupButton);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        login= findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textEmail = emailAd.getText().toString();
                textPwd = Paswd.getText().toString();

                if (TextUtils.isEmpty(textEmail)){
                    Toast.makeText(MainActivity.this, "Please enter your emailId", Toast.LENGTH_LONG).show();
                    emailAd.setError("email is required");
                    emailAd.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(MainActivity.this, "Please re-enter your emailId", Toast.LENGTH_LONG).show();
                    emailAd.setError("Valid email is required");
                    emailAd.requestFocus();
                }else if(TextUtils.isEmpty(textPwd)){
                    Toast.makeText(MainActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    Paswd.setError("password is required");
                    Paswd.requestFocus();
                }else {
                    loginUser(textEmail,textPwd);
                }
            }
        });

    }
    private void loginUser(String Email, String Pwd){
        authProfile.signInWithEmailAndPassword(Email,Pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"User Logged In successfully", Toast.LENGTH_LONG).show();
                    Intent intent= new Intent(MainActivity.this, userProfileActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(MainActivity.this,"Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}