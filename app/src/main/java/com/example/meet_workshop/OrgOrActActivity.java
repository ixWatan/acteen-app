package com.example.meet_workshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OrgOrActActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_or_act);
    }

    public void goToSignUp(View view) {
        Intent intent = new Intent(OrgOrActActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(OrgOrActActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void SignUpOrganizationActivity(View view) {
        Intent intent = new Intent(OrgOrActActivity.this, SignUpOrganizationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}