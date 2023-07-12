package com.example.meet_workshop.homepage.homeorganization;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.meet_workshop.MainActivity;
import com.example.meet_workshop.OrgOrActActivity;
import com.example.meet_workshop.R;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfileActivity extends AppCompatActivity {

    private Button buttonHabibi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Find the Sign Out button by ID
        buttonHabibi = findViewById(R.id.buttonHabibi);

        // Set click listener for the button
        buttonHabibi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void signOut() {
        // Sign out from Firebase Authentication
        FirebaseAuth.getInstance().signOut();

        // Show a toast message
        Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show();

        // Start the MainActivity
        Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Optionally, call finish() to prevent the user from returning to the UserProfileActivity using the back button
    }

    public void goToOrgOrActActivity(View view) {
        // Handle the click event for another button (if applicable)
        Intent intent = new Intent(this, OrgOrActActivity.class);
        startActivity(intent);
    }
}
