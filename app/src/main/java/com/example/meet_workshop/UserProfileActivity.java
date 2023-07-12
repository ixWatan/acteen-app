package com.example.meet_workshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class UserProfileActivity extends AppCompatActivity {

    private Button signOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        signOutBtn = findViewById(R.id.buttonHabibi);

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserProfileActivity.this, MainActivity.class));
            }
        });

    }








    public void goToOrgOrActActivity(View view) {
        Intent intent = new Intent(this, OrgOrActActivity.class);
        startActivity(intent);
    }

    /*
    protected void goToHomePageAndSignOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

     */
}