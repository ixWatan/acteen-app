package com.example.meet_workshop.homepage.homeorganization;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.meet_workshop.R;


public class ChooseLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        // Load the MapsFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new MapsFragment())
                    .commit();
        }
    }
}
