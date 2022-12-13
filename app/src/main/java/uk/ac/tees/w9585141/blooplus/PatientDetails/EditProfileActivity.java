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

import com.bumptech.glide.Glide;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import uk.ac.tees.w9585141.blooplus.R;
import uk.ac.tees.w9585141.blooplus.home.HomeActivity;

public class EditProfileActivity extends AppCompatActivity {

    Button saveEdit;
    ImageView profilePic,editMpBackbutton;
    Uri imageUri;
    StorageReference storef;
    FirebaseStorage fstorage;
    Uri urlImage;
    String crntuser;
    StorageReference imageRef;
    EditText mpe_fname,mpe_lname, mpe_phn, mpe_email, mpe_password;

    String firstName,lastname,email,Phone,password;


    public static final String TAG= "EditProfileActivity";

    DocumentReference docref;
    FirebaseFirestore fbStore= FirebaseFirestore.getInstance();

   private DatabaseReference mDB = FirebaseDatabase.getInstance().getReference().child("user");
   private FirebaseAuth mAth = FirebaseAuth.getInstance();
    FirebaseUser user;
    StorageReference storageReference;
    ProgressDialog progressDialog;



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
        firstName= data.getStringExtra("fName");
        lastname= data.getStringExtra("Lname");
        email=data.getStringExtra("email");
        Phone= data.getStringExtra("phone");
        password= data.getStringExtra("password");

        mpe_fname.setText(firstName);
        mpe_lname.setText(lastname);
        mpe_email.setText(email);
        mpe_phn.setText(Phone);
        mpe_password.setText(password);

        profilePic= findViewById(R.id.editProfilePic);
        saveEdit= findViewById(R.id.mpE_save);
        saveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        StorageReference file=FirebaseStorage.getInstance().getReference("images/"+FirebaseAuth.getInstance().getCurrentUser().getUid());
        file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // adding the url in the arraylist
                Glide.with(getApplicationContext()).load(uri.toString()).into(profilePic);
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

    private void updateProfile() {

        String uFname= mpe_fname.getText().toString();
        String uLname= mpe_lname.getText().toString();
        String uEmail= mpe_email.getText().toString();
        String uPhn= mpe_phn.getText().toString();

        if(uFname.isEmpty() || uLname.isEmpty()  || uEmail.isEmpty()  || uPhn.isEmpty()){
            Toast.makeText(this, "One or Many fields are empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        user.updateEmail(uEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DocumentReference docref= fbStore.collection("user").document(crntuser);
                Map<String,Object> edited = new HashMap<>();
                edited.put("fName", uFname);
                edited.put("Lname",uLname);
                edited.put("email",uEmail);
                edited.put("phone",uPhn);
//                edited.put("imageRef",imageRef);


                docref.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfileActivity.this,   e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



//        DocumentReference docref= fbStore.collection("user").document(crntuser);
//
//        fbStore.runTransaction(new Transaction.Function<Void>() {
//                    @Override
//                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
//                       // DocumentSnapshot snapshot = transaction.get(sfDocRef);
//
//                        transaction.update(docref, "fName",firstName);
//                        transaction.update(docref, "Lname",lastname);
//                        transaction.update(docref, "email",email);
//                        transaction.update(docref, "phone",Phone);
////                        transaction.update(docref, "password",password);
//
//                        // Success
//                        return null;
//                    }
//                }).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Intent intent= new Intent(EditProfileActivity.this, Myprofile.class);
//                        Toast.makeText(EditProfileActivity.this, "Profile is updated", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(EditProfileActivity.this, "Profile not updated", Toast.LENGTH_SHORT).show();
//                    }
//                });


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
            this.imageUri= data.getData();
            profilePic.setImageURI(imageUri);
            uploadImage();

        }
    }

    private void uploadImage() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading File....");
        progressDialog.show();


        String fileName = user.getUid();
        storageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);


        storageReference.putFile(this.imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(EditProfileActivity.this,"Successfully Uploaded",Toast.LENGTH_SHORT).show();
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(EditProfileActivity.this,"Failed to Upload",Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void uploadpick(Uri imageUri) {

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
//                    throw task.getException();
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
                Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                Toast.makeText(EditProfileActivity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


}