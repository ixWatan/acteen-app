package com.example.meet_workshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

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
        }
    }

    public void create_user(String email, String password, String age, String name, String city, String region) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            mDatabase = FirebaseDatabase.getInstance().getReference("Users");
                            mDatabase.child(uid).child("email").setValue(email);
                            mDatabase.child(uid).child("password").setValue(password);
                            mDatabase.child(uid).child("age").setValue(age);
                            mDatabase.child(uid).child("city").setValue(city);
                            mDatabase.child(uid).child("name").setValue(name);
                            mDatabase.child(uid).child("region").setValue(region)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Log.w(TAG, "setValue:failure", task.getException());
                                                Toast.makeText(SignUpActivity.this, "Failed to set user data.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

