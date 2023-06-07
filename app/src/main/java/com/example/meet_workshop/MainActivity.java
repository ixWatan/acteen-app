package com.example.meet_workshop;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.signin_email);
        password = (EditText) findViewById(R.id.signin_pass);

    }



    public void goToHome(View view) {
        loginUser(email.getText().toString(), password.getText().toString());
    }

    public void goToOrgOrActActivity(View view) {
        Intent intent = new Intent(this, OrgOrActActivity.class);
        startActivity(intent);
    }

    private void loginUser(String email, String password) {
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
                                                    startActivity(intent);
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
                                Intent intent = new Intent(MainActivity.this, HomeOrgActivity.class);
                                startActivity(intent);
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

}
