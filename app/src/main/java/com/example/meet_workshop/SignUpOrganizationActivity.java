package com.example.meet_workshop;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.meet_workshop.homepage.homeorganization.HomeOrgActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class SignUpOrganizationActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_sign_up_organization);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.signup_email);
        password  = findViewById(R.id.signup_pass);
        name = findViewById(R.id.signup_name);
        age = findViewById(R.id.signup_age);
        region = findViewById(R.id.signup_region);
        city = findViewById(R.id.signup_city);

    }

    public void goToHome(View view) {
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();
        String nameStr = name.getText().toString();
        String ageStr = age.getText().toString();
        String regionStr = region.getText().toString();
        String cityStr = city.getText().toString();

        if (emailStr.isEmpty() || passwordStr.isEmpty() || nameStr.isEmpty() || ageStr.isEmpty() || regionStr.isEmpty() || cityStr.isEmpty()) {
            Toast.makeText(this, "Please enter email, password, name, city, age and region", Toast.LENGTH_SHORT).show();
        } else {
            create_user(emailStr, passwordStr, nameStr, ageStr,regionStr, cityStr);
        }
    }

    public void create_user(String email, String password, String name , String age, String region, String  city) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String uid = user.getUid();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("email", email);
                        userData.put("password", password);
                        userData.put("name", name);
                        userData.put("age", age);
                        userData.put("region", region);
                        userData.put("city", city);

                        db.collection("organizations").document(uid)   // Changed the collection to "organizations"
                                .set(userData)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(name)
                                                .build();
                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(task2 -> {
                                                    if (task2.isSuccessful()) {
                                                        Log.d(TAG, "User profile updated.");
                                                    }
                                                });

                                        // Navigate to the interests page after successfully creating the user.
                                        Toast.makeText(this,"You're in",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpOrganizationActivity.this, HomeOrgActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Log.w(TAG, "set:failure", task1.getException());
                                        Toast.makeText(SignUpOrganizationActivity.this, "Failed to set user data.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else {
                        // Check the type of exception
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(SignUpOrganizationActivity.this, "This email is already registered.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpOrganizationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}