package uk.ac.tees.w9585141.blooplus.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import uk.ac.tees.w9585141.blooplus.R;
import uk.ac.tees.w9585141.blooplus.home.HomeActivity;

public class LoginActivity extends AppCompatActivity {
    Button signup;
    Button login;
    String textEmail;
    String textPwd;
    TextView frgtPasswd;
    //public static String uid;
    private EditText emailAd,Paswd;
    private FirebaseAuth authProfile;
    public static final String SHARED_PREF="sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login");

        emailAd= findViewById(R.id.emailAddressLogin);
        Paswd= findViewById(R.id.editPasswordLogin);
        frgtPasswd= findViewById(R.id.Loginforgetpwd);

        authProfile= FirebaseAuth.getInstance();

       // checkbox();


        signup = findViewById(R.id.signupbutton);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        frgtPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        Intent intent = new Intent(LoginActivity.this, ForgotPswrdScreen.class);
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
                    Toast.makeText(LoginActivity.this, "Please enter your emailId", Toast.LENGTH_LONG).show();
                    emailAd.setError("email is required");
                    emailAd.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(LoginActivity.this, "Please re-enter your emailId", Toast.LENGTH_LONG).show();
                    emailAd.setError("Valid email is required");
                    emailAd.requestFocus();
                }else if(TextUtils.isEmpty(textPwd)){
                    Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    Paswd.setError("password is required");
                    Paswd.requestFocus();
                }else {
                    loginUser(textEmail,textPwd);
                }
            }
        });

    }

    /*private void checkbox() {

        SharedPreferences sharedPreferences= getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        String check= sharedPreferences.getString("name","");

        if(check.equals("true")){

            Toast.makeText(LoginActivity.this,"User Logged In successfully", Toast.LENGTH_LONG).show();
            Intent intent= new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

    }*/

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("TAG", "login Activity stop" );
    }

    private void loginUser(String Email, String Pwd){
        authProfile.signInWithEmailAndPassword(Email,Pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    /*SharedPreferences sharedPreferences= getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("name", "true");
                    editor.apply();*/

                    Toast.makeText(LoginActivity.this,"User Logged In successfully", Toast.LENGTH_LONG).show();
                    Intent intent= new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(LoginActivity.this,"Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}