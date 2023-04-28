package com.example.meet_workshop;

import static android.content.ContentValues.TAG;

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

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    EditText email;
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        email = (EditText)findViewById(R.id.signup_email);
        password  = (EditText)findViewById(R.id.signup_pass);
    }

    public void goToHome(View view) {
        create_user(email.getText().toString(), password.getText().toString());
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void create_user(String email, String password) {
        mDatabase = FirebaseDatabase.getInstance("https://<YOUR-FIREBASE-REALTIME-DATABASE-URL>").getReference("Users");

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid().toString();
                            mDatabase.child(uid).child("email").setValue(email);
                            mDatabase.child(uid).child("password").setValue(password);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "createUserWithEmail:exception", task.getException());
                        }
                    }
                });
    }

}
