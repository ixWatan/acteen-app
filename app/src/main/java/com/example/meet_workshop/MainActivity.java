package com.example.meet_workshop;
import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;


import com.example.meet_workshop.homepage.homeactivist.HomeActivity;
import com.example.meet_workshop.homepage.homeorganization.HomeOrgActivity;
import com.example.meet_workshop.homepage.homeorganization.UserProfileOrgActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText email;
    private EditText password;

    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.signin_email);
        password = findViewById(R.id.signup_pass);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        ProgressDialog pd;




        /*CheckBox passwordToggleCheckBox = findViewById(R.id.passwordToggleCheckBox);
        passwordToggleCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show password
                password.setTransformationMethod(null);
            } else {
                // Hide password
                password.setTransformationMethod(new PasswordTransformationMethod());
            }
        });*/
    }

    public void goToHome(View view) {
        loginUser(email.getText().toString(), password.getText().toString());
    }

    public void goToOrgOrActActivity(View view) {
        Intent intent = new Intent(this, OrgOrActActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    public void goToSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);

    }


    private void loginUser(String email, String password) {

        pd = new ProgressDialog(this);
        pd.setMessage("Signing User ...");
        pd.show();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Email and password cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("teenActivists")
                                    .document(user.getUid())
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            DocumentSnapshot document = task1.getResult();
                                            if (document != null && document.exists()) {
                                                String storedEmail = document.getString("email");
                                                if (email.equalsIgnoreCase(storedEmail)) {
                                                    // User's email is in the system
                                                    // Proceed to the next activity
                                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                                } else {
                                                    checkForOrganizationUser(email, user.getUid());
                                                }
                                            } else {
                                                checkForOrganizationUser(email, user.getUid());
                                            }
                                        } else {
                                            Log.d(TAG, "Failed to get user document.", task1.getException());
                                        }
                                    });
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        pd.dismiss();
    }

    private void checkForOrganizationUser(String email, String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("organizations")
                .document(uid)
                .get()
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        DocumentSnapshot document = task1.getResult();
                        if (document != null && document.exists()) {
                            String storedEmail = document.getString("email");
                            if (email.equalsIgnoreCase(storedEmail)) {
                                // User's email is in the system
                                // Proceed to the next activity
                                Intent intent = new Intent(MainActivity.this, UserProfileOrgActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                            } else {
                                // User's email is not in the system
                                Toast.makeText(MainActivity.this,
                                        "Your email is not registered.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // User's email is not in the system
                            Toast.makeText(MainActivity.this,
                                    "Your email is not registered.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d(TAG, "Failed to get user document.", task1.getException());
                    }
                });
    }



  /*  @Override
    public void onPause() {
        super.onPause();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastActivity", getClass().getName());
        editor.apply();
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String lastActivity = prefs.getString("lastActivity", "");
        if ("com.example.meet_workshop.SignUpActivity".equals(lastActivity)){
            overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        } else if ("com.example.meet_workshop.SlideShowOrg.SlideShowActivityOrg4".equals(lastActivity)) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if ("com.example.meet_workshop.SignUpOrganizationActivity".equals(lastActivity)) {
            overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        } else if ("com.example.meet_workshop.SlideShowAct.SlideShowAct5".equals(lastActivity)) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

    }
}
