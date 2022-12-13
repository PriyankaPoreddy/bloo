package uk.ac.tees.w9585141.blooplus.DoctorDetails;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import uk.ac.tees.w9585141.blooplus.R;

public class doctorListing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_listing);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menulist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_profile:
                Toast.makeText(this, "MyProfile is selected", Toast.LENGTH_SHORT);
                return true;
            case R.id.menu_showAppointment:
                Toast.makeText(this, "appointments selected", Toast.LENGTH_SHORT);
                return true;
            case R.id.menu_bookedAppointment:
                Toast.makeText(this, "BookedAppointments is selected", Toast.LENGTH_SHORT);
                return true;
            case R.id.menu_Documents:
                Toast.makeText(this, "Files is selected", Toast.LENGTH_SHORT);
                return true;
            case R.id.menu_logout:
                Toast.makeText(this, "Logout is selected", Toast.LENGTH_SHORT);
                return true;
            case R.id.menu_aboutapp:
                Toast.makeText(this, "about is selected", Toast.LENGTH_SHORT);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }




}