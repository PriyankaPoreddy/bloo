package uk.ac.tees.w9585141.blooplus.home.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import uk.ac.tees.w9585141.blooplus.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DateFragment#} factory method to
 * create an instance of this fragment.
 */
public class DateFragment extends Fragment {

    private TextView mySelectDate, mySelectedDate, myAvailableDate;
    private RecyclerView recyclerView;
    private int count = 0;

    private Calendar calendar;
    private DatePickerDialog datePickerDialog;

    private String currentUserID = "";

    private DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();


    public DateFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View finalView=inflater.inflate(R.layout.fragment_date, container, false);

        recyclerView = (RecyclerView) finalView.findViewById(R.id.date_doctorList_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(finalView.getContext()));


        mySelectDate = (TextView) finalView.findViewById(R.id.date_select);
        mySelectedDate = (TextView) finalView.findViewById(R.id.date_selected);
        myAvailableDate = (TextView) finalView.findViewById(R.id.date_avilable);
        mySelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(finalView.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        String date = dayOfMonth + "-" + (month + 1) + "-" + year;
                        Toast.makeText(finalView.getContext(), date, Toast.LENGTH_SHORT).show();
                        mySelectedDate.setVisibility(View.VISIBLE);
                        mySelectDate.setText(date);
                        myAvailableDate.setVisibility(View.VISIBLE);

                        showDoctorList(date);

                    }
                }, day, month, year);
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + (3 * 60 * 60 * 1000));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + (15 * 24 * 60 * 60 * 1000));
                datePickerDialog.show();
            }
        });




        return finalView;


    }

    private void showDoctorList(String date) {

        Query countQuery = dbref.child("Appointment");
        countQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String doctorID = dataSnapshot.getKey().toString();
                dbref.child("Appointment").child(doctorID).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        count = (int) dataSnapshot.getChildrenCount();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }




    public class DoctorListView extends RecyclerView.ViewHolder {

        View mView;

        public DoctorListView(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setDoctorName(String doctorName) {
            TextView userName = (TextView) mView.findViewById(R.id.name_id_singleuser);
            userName.setText(doctorName);
        }

        public void setSpecialization(String specialization) {
            TextView userName = (TextView) mView.findViewById(R.id.special_id_singleuser);
            userName.setText(specialization);
        }
    }


}