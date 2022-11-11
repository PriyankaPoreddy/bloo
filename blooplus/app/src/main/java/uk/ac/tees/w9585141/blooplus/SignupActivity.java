package uk.ac.tees.w9585141.blooplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    EditText emailId,Paswrd,CnfmPaswd,Fname,Lname,Phn;
    String emailPattern= "[a-zA-Z0-9\\!\\@\\#\\$]{8,24}";
    ProgressDialog progressDialog;
    FirebaseFirestore fstore;
    String UserId;
    private ProgressBar progressBar;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    String email;
    String password;
    String ConfirmPaswd;
    String FirstName;
    String Lastname;
    String Phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        getSupportActionBar().setTitle("Registration");
        Toast.makeText(SignupActivity.this,"You Can Signup now",Toast.LENGTH_SHORT).show();

        progressBar= findViewById(R.id.progressbar);
        Fname=findViewById(R.id.editTextTextPersonName);
        Lname=findViewById(R.id.editTextTextPersonName2);
        Phn= findViewById(R.id.editTextPhone);
        emailId= findViewById(R.id.emailSignup);
        Paswrd= findViewById(R.id.paswdSignup);
        CnfmPaswd=findViewById(R.id.cnfrmPswdSignup);




        Button Signin=findViewById(R.id.SignInButton);
        Button Cancel = findViewById(R.id.SignInButton);


        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email= emailId.getText().toString();
                password= Paswrd.getText().toString().trim();
                ConfirmPaswd= CnfmPaswd.getText().toString().trim();
                FirstName= Fname.getText().toString();
                Lastname= Lname.getText().toString();
                Phone= Phn.getText().toString();

                if(TextUtils.isEmpty(FirstName)){
                    Toast.makeText(SignupActivity.this, "Please Enter Your First Name", Toast.LENGTH_SHORT).show();
                    Fname.setError("First Name is Required");
                    Fname.requestFocus();
                }else if(TextUtils.isEmpty(Lastname)){
                    Toast.makeText(SignupActivity.this, "Please Enter Your Last Name", Toast.LENGTH_SHORT).show();
                    Lname.setError("last Name is Required");
                    Lname.requestFocus();
                }else if(TextUtils.isEmpty(Phone)){
                    Toast.makeText(SignupActivity.this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();
                    Phn.setError("Phone number is Required");
                    Phn.requestFocus();
                }else if(Phone.length()!=10){
                    Toast.makeText(SignupActivity.this, "Please re-Enter Your PhoneNumber", Toast.LENGTH_SHORT).show();
                    Phn.setError("Mobile number should be 10 digits");
                    Phn.requestFocus();
                }
                else if(TextUtils.isEmpty(email)){
                    Toast.makeText(SignupActivity.this, "Please Enter Your emailID", Toast.LENGTH_SHORT).show();
                    emailId.setError("emailId is Required");
                    emailId.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(SignupActivity.this, "Please re-Enter Your emailID", Toast.LENGTH_SHORT).show();
                    emailId.setError("Valid emailId is Required");
                    emailId.requestFocus();
                }else if(TextUtils.isEmpty(password)){
                    Toast.makeText(SignupActivity.this, "Please Enter the Password", Toast.LENGTH_SHORT).show();
                    Paswrd.setError("Enter Valid password");
                    Paswrd.requestFocus();
                }else if(password.length()<6){
                    Toast.makeText(SignupActivity.this, "Password should be atleast 6 digits", Toast.LENGTH_SHORT).show();
                    Paswrd.setError("Password is too weak");
                    Paswrd.requestFocus();
                }else if(TextUtils.isEmpty(ConfirmPaswd)){
                    Toast.makeText(SignupActivity.this, "Please enter Confirm Password", Toast.LENGTH_SHORT).show();
                    CnfmPaswd.setError("Confirm Password is required");
                    CnfmPaswd.requestFocus();
                }else if(!password.equals(ConfirmPaswd)){
                    Toast.makeText(SignupActivity.this,"Please Enter same as Password",Toast.LENGTH_SHORT).show();
                    CnfmPaswd.setError("Confirm password is required");
                    CnfmPaswd.requestFocus();
                    //Clearing the entered text in password
                    Paswrd.clearComposingText();
                    CnfmPaswd.clearComposingText();
                }else {

                    progressBar.setVisibility(View.VISIBLE);
                    signupUser(FirstName, Lastname, email, Phone, password);
                }
            }
        });
    }

    private void signupUser(String Firstname, String Lastname, String email, String Phone, String password){

        mAuth= FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignupActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignupActivity.this,"User Registered Successfully", Toast.LENGTH_LONG).show();
                            FirebaseUser firebaseUser= mAuth.getCurrentUser();

                            //Send verification email
                            firebaseUser.sendEmailVerification();

                            //open user profile after successful registration
                            Intent intnt = new Intent(SignupActivity.this, userProfileActivity.class);

                            //to prevent the user returning back to register activity on pressing back button after registration
                            intnt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intnt);
                            finish(); //to close register activity

                        }else{
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException e){
                                Paswrd.setError("Your password is too weak, kindly use mix of alphabets,Numbers, and Special characters");
                                Paswrd.requestFocus();
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                Paswrd.setError("Your email is invalid or already in use, Kindly change it");
                                Paswrd.requestFocus();
                            }catch (FirebaseAuthUserCollisionException e){
                                Paswrd.setError("User is already exists with this email");
                                Paswrd.requestFocus();
                            }catch (Exception e){
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(SignupActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);

                            }
                        }
                    }
                });
    }
}