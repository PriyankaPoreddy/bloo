package uk.ac.tees.w9585141.blooplus;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    Button signup;
    EditText emailAd;
    EditText Paswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        signup = findViewById(R.id.button2);
        signup.setOnClickListner(new View.OnClickListener() {
            @Override
            public void Onclick(View view) {
                Intent i = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(i);
                finish();

            }

        });
    }
}