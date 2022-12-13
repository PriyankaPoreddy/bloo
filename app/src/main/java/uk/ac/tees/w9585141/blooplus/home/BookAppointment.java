package uk.ac.tees.w9585141.blooplus.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import uk.ac.tees.w9585141.blooplus.DoctorList;
import uk.ac.tees.w9585141.blooplus.Doctors;
import uk.ac.tees.w9585141.blooplus.PatientDetails.Myprofile;
import uk.ac.tees.w9585141.blooplus.PatientDetails.PatientVieDoctorProfActivity;
import uk.ac.tees.w9585141.blooplus.R;

public class BookAppointment extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView docPic;
    DatabaseReference dbref;

    DoctAdapter dAdapter;
    ArrayList<Doctors> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        /*docPic= findViewById(R.id.profile_id_single_user);

        docPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookAppointment.this, PatientVieDoctorProfActivity.class);
                startActivity(intent);
            }
        });*/

        recyclerView= findViewById(R.id.bookappointmentList);
        dbref= FirebaseDatabase.getInstance().getReference("Doctor_Details");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        dAdapter = new DoctAdapter(this, list);
        recyclerView.setAdapter(dAdapter);

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Doctors user= dataSnapshot.getValue(Doctors.class);
                    list.add(user);
                }

                dAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}