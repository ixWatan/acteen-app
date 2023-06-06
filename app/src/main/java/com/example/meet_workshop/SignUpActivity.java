package com.example.meet_workshop;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;


public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;

    EditText email;
    EditText password;
    EditText age;
    EditText name;
    EditText city;
    EditText region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.signup_email);
        password  = findViewById(R.id.signup_pass);
        age = findViewById(R.id.signup_age);
        name = findViewById(R.id.signup_name);
        city = findViewById(R.id.signup_city);
        region = findViewById(R.id.signup_region);
    }

    public void goToHome(View view) {
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();
        String ageStr = age.getText().toString();
        String nameStr = name.getText().toString();
        String cityStr = city.getText().toString();
        String regionStr = region.getText().toString();

        if (emailStr.isEmpty() || passwordStr.isEmpty() || ageStr.isEmpty() || nameStr.isEmpty() || cityStr.isEmpty() || regionStr.isEmpty()) {
            Toast.makeText(this, "Please enter email, password, name, city, age and region", Toast.LENGTH_SHORT).show();
        } else {
            create_user(emailStr, passwordStr, ageStr, nameStr, cityStr, regionStr);
            Toast.makeText(this,"You're in",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ActivityInterests.class);
            startActivity(intent);
        }
    }

    public void create_user(String email, String password, String age, String name, String city, String region) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // ... existing successful sign up code ...
                    } else {
                        // Check the type of exception
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(SignUpActivity.this, "This email is already registered.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}


