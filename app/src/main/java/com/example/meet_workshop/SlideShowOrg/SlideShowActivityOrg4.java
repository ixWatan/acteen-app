package com.example.meet_workshop.SlideShowOrg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meet_workshop.MainActivity;
import com.example.meet_workshop.R;
import com.example.meet_workshop.SignUpOrganizationActivity;

public class SlideShowActivityOrg4 extends AppCompatActivity {

    private Button SignUp;
    private Button LogIn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show_org4);



        SignUp = findViewById(R.id.Signup);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the profileImageButton click event
                SignUpFunction();
            }
        });

        LogIn = findViewById(R.id.Login);
        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the profileImageButton click event
                LogInFunction();
            }
        });

    }

    private void SignUpFunction() {
        Intent intent = new Intent(this, SignUpOrganizationActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    private void LogInFunction() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}