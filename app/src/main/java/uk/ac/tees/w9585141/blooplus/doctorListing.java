package uk.ac.tees.w9585141.blooplus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

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
            case R.id.menu_myprofile:
                Toast.makeText(this, "MyProfile is selected", Toast.LENGTH_SHORT);
                return true;

            case R.id.menu_Doctors:
                Toast.makeText(this, "Doctors is selected", Toast.LENGTH_SHORT);
                return true;
            case R.id.menu_appointments:
                Toast.makeText(this, "Appointments is selected", Toast.LENGTH_SHORT);
                return true;
            case R.id.menu_Files:
                Toast.makeText(this, "Files is selected", Toast.LENGTH_SHORT);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}