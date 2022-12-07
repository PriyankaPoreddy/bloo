package uk.ac.tees.w9585141.blooplus.PatientDetails;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import uk.ac.tees.w9585141.blooplus.R;

public class EditProfileActivity extends AppCompatActivity {

    Button saveEdit;
    ImageView profilePic,editMpBackbutton;
    Uri imageUri;
    StorageReference storef;
    FirebaseStorage fstorage;
    Uri urlImage;
    String crntuser;

    EditText mpe_fname,mpe_lname, mpe_phn, mpe_email, mpe_password;

    String firstName,lastname,email,Phone,password;


    public static final String TAG= "EditProfileActivity";

 //FirebaseDatabase fdb= FirebaseDatabase.getInstance();
//    DatabaseReference dbref;
    DocumentReference docref;
    FirebaseFirestore fbStore= FirebaseFirestore.getInstance();

   private DatabaseReference mDB = FirebaseDatabase.getInstance().getReference().child("user");
   private FirebaseAuth mAth = FirebaseAuth.getInstance();
    FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mpe_fname= findViewById(R.id.mpE_fname);
        mpe_lname=findViewById(R.id.mpE_lname);
        mpe_email=findViewById(R.id.mpE_email);
        mpe_phn= findViewById(R.id.mpE_phn);
        mpe_password=findViewById(R.id.mpE_password);

        editMpBackbutton= findViewById(R.id.emp_menu_backButton);
        editMpBackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, Myprofile.class);
                startActivity(intent);
                finish();
            }
        });


        user= mAth.getCurrentUser();
        if (user != null) {
            crntuser=user.getUid();
        }
        docref=fbStore.collection("user").document(crntuser);



       Intent data= getIntent();
        firstName= data.getStringExtra("fName").toString();
        lastname= data.getStringExtra("Lname").toString();
        email=data.getStringExtra("email").toString();
        Phone= data.getStringExtra("phone").toString();
        password= data.getStringExtra("password").toString();

        mpe_fname.setText(firstName);
        mpe_lname.setText(lastname);
        mpe_email.setText(email);
        mpe_phn.setText(Phone);
        mpe_password.setText(password);

        Log.d(TAG, "onCreate: "+ firstName + " " + lastname + " " + email+" "+ Phone+" "+password+"");



        profilePic= findViewById(R.id.editProfilePic);

        saveEdit= findViewById(R.id.mpE_save);
        saveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //same as signup
                //uploadImageTodb();
                updateProfile();


            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPick();

            }
        });

        fstorage = FirebaseStorage.getInstance();



    }

   /* @Override
    protected void onStart() {
        super.onStart();


        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.getResult().exists()){

                    Intent data= getIntent();
                    firstName= data.getStringExtra("fName").toString();
                    lastname= data.getStringExtra("Lname").toString();
                    email=data.getStringExtra("email").toString();
                    Phone= data.getStringExtra("phone").toString();
                    password= data.getStringExtra("password").toString();

                    mpe_fname.setText(firstName);
                    mpe_lname.setText(lastname);
                    mpe_email.setText(email);
                    mpe_phn.setText(Phone);
                    mpe_password.setText(password);


                }else {
                    Toast.makeText(EditProfileActivity.this, "there is no profile exist", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }*/



    private void updateProfile() {

        String uFname= mpe_fname.getText().toString();
        String uLname= mpe_lname.getText().toString();
        String uEmail= mpe_email.getText().toString();
        String uPhn= mpe_phn.getText().toString();
        String uPwd= mpe_password.getText().toString();

        DocumentReference docref= fbStore.collection("user").document(crntuser);

        fbStore.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                       // DocumentSnapshot snapshot = transaction.get(sfDocRef);

                        transaction.update(docref, "fName",firstName);
                        transaction.update(docref, "Lname",lastname);
                        transaction.update(docref, "email",email);
                        transaction.update(docref, "phone",Phone);
                        transaction.update(docref, "password",password);

                        // Success
                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent= new Intent(EditProfileActivity.this, Myprofile.class);
                        Toast.makeText(EditProfileActivity.this, "Profile is updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileActivity.this, "Profile not updated", Toast.LENGTH_SHORT).show();
                    }
                });


        /*mDB.child(crntuser).child("fName").setValue(firstName);
        mDB.child(crntuser).child("Lname").setValue(lastname);
        mDB.child(crntuser).child("email").setValue(email);
        mDB.child(crntuser).child("phone").setValue(Phone);
        mDB.child(crntuser).child("password").setValue(password);*/

        Log.d(TAG, "updateProfile: successfully updated");
    }

    private void selectPick() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data !=null && data.getData()!=null){
            imageUri= data.getData();
            profilePic.setImageURI(imageUri);
            uploadpick();

        }
    }


    private void uploadpick() {

        final ProgressDialog progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Image is uploading....");
        progressDialog.show();

        final String rkey= UUID.randomUUID().toString();
        storef = fstorage.getReference();

        StorageReference imageref = storef.child("images/"+rkey);


//        imageref.putFile(imageUri);

        UploadTask uploadTask = imageref.putFile(imageUri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return imageref.getDownloadUrl();

            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {

                    //Retrieving the image link from storage
                    urlImage = task.getResult();
                    Snackbar.make(findViewById(android.R.id.content),"Image is uploaded",Snackbar.LENGTH_SHORT).show();
                    Log.d(TAG, "onComplete: Image uploaded successfully"+task.getResult());
                    progressDialog.dismiss();
                } else {
                    // Handle failures
                    // ...
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfileActivity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


}