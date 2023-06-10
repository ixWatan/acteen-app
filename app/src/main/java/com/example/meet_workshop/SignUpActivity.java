package com.example.meet_workshop;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.meet_workshop.homepage.homeactivist.AddPostActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private EditText ageEditText;
    private EditText regionEditText;
    private EditText cityEditText;
    private User person = new User(null,null,null,null,null,null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        this.emailEditText = findViewById(R.id.signup_email);
        this.passwordEditText = findViewById(R.id.signup_pass);
        this.nameEditText = findViewById(R.id.signup_name);
        this.ageEditText = findViewById(R.id.signup_age);
        this.regionEditText = findViewById(R.id.signup_region);
        this.cityEditText = findViewById(R.id.signup_city);

    }

    public void goToHome(View view) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String age = ageEditText.getText().toString();
        String region = regionEditText.getText().toString();
        String city = cityEditText.getText().toString();
        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || age.isEmpty() || region.isEmpty() || city.isEmpty()) {
            Toast.makeText(this, "Please enter email, password, name, city, age, and region", Toast.LENGTH_SHORT).show();
        } else {
            this.person.setEmail(email);
            this.person.setPassword(password);
            this.person.setName(name);
            this.person.setAge(age);
            this.person.setRegion(region);
            this.person.setCity(city);

            createUser(person);        }
    }
    private void checkPostsCollectionExistence() {

    }
    public void createUser(User person) {
        mAuth.createUserWithEmailAndPassword(person.getEmail(), person.getPassword())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String uid = user.getUid();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("email", person.getEmail());
                        userData.put("password", person.getPassword());
                        userData.put("name", person.getName());
                        userData.put("age", person.getAge());
                        userData.put("region", person.getRegion());
                        userData.put("city", person.getCity());
                        userData.put("followers", person.getFollowers());
                        userData.put("following", person.getFollowing());
                        userData.put("posts", person.getPosts());

                        db.collection("teenActivists").document(uid)
                                .set(userData)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(person.getName())
                                                .build();
                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(task2 -> {
                                                    if (task2.isSuccessful()) {
                                                        Log.d(TAG, "User profile updated.");
                                                    }
                                                });

                                        // Navigate to the interests page after successfully creating the user.
                                        Toast.makeText(this, "You're in", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpActivity.this, ActivityInterests.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Log.w(TAG, "set:failure", task1.getException());
                                        Toast.makeText(SignUpActivity.this, "Failed to set user data.", Toast.LENGTH_SHORT).show();
                                    }
                                });

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


