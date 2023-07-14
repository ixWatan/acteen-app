package com.example.meet_workshop;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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

    private CheckBox checkBox;

    private Boolean checkBoxState;

    EditText email;
    EditText password;
    EditText phoneNum;
    EditText orgname;

    private TextView checkBoxText;
    EditText city;

    private EditText password1;

    EditText region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_organization);
        this.checkBoxText = findViewById(R.id.checkBoxText);
        this.checkBox = findViewById(R.id.simpleCheckBox);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.signup_email);
        password  = findViewById(R.id.signup_pass);
        orgname = findViewById(R.id.signup_orgname);
        phoneNum = findViewById(R.id.signup_phone);
        region = findViewById(R.id.signup_region);
        city = findViewById(R.id.signup_city);
        password1 = findViewById(R.id.signup_pass);


        CheckBox passwordToggleCheckBox = findViewById(R.id.passwordToggleCheckBox);
        passwordToggleCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show password
                password.setTransformationMethod(null);
            } else {
                // Hide password
                password.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

    }

    public void goToHome(View view) {
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();
        String orgNameStr = orgname.getText().toString();
        String phoneNumStr = phoneNum.getText().toString();
        String regionStr = region.getText().toString();
        String cityStr = city.getText().toString();

        Boolean checkBoxState = checkBox.isChecked();

        if (emailStr.isEmpty() || passwordStr.isEmpty() || orgNameStr.isEmpty() || phoneNumStr.isEmpty() || regionStr.isEmpty() || cityStr.isEmpty() || checkBoxState == false) {
            Toast.makeText(this, "Please enter email, password, name, city, age and region", Toast.LENGTH_SHORT).show();
        } else {
            create_user(emailStr, passwordStr, orgNameStr, phoneNumStr,regionStr, cityStr);
        }
    }

    public void OpenTermsAndGuidelines(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        startActivity(browserIntent);
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
                        userData.put("organization_name", name);
                        userData.put("phone", age);
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