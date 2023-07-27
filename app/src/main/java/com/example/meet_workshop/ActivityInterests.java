package com.example.meet_workshop;
import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.meet_workshop.homepage.homeactivist.HomeActivity;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ActivityInterests extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Set<String> selectedInterests = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void onButtonClick(View view) {
        Button button = (Button) view;

        switch(button.getId()) {
            case R.id.my_button:
                handleButtonInteraction(button);
                break;

            case R.id.my_button1:
                handleButtonInteraction(button);
                break;

            case R.id.my_button2:
                handleButtonInteraction(button);
                break;

            case R.id.my_button3:
                handleButtonInteraction(button);
                break;

            case R.id.my_button4:
                handleButtonInteraction(button);
                break;

            case R.id.my_button5:
                handleButtonInteraction(button);
                break;

            case R.id.my_button6:
                handleButtonInteraction(button);
                break;
            case R.id.my_button7:
                handleButtonInteraction(button);
                break;
            case R.id.my_button8:
                handleButtonInteraction(button);
                break;
            case R.id.my_button9:
                handleButtonInteraction(button);
                break;
            case R.id.my_button10:
                handleButtonInteraction(button);
                break;
            case R.id.my_button11:
                handleButtonInteraction(button);
                break;
            case R.id.my_button12:
                handleButtonInteraction(button);
                break;
            case R.id.my_button13:
                handleButtonInteraction(button);
                break;
            case R.id.my_button14:
                handleButtonInteraction(button);
                break;
            case R.id.my_button15:
                handleButtonInteraction(button);
                break;

            default:
                // if the id does not match any case
                break;
        }
    }

    private void handleButtonInteraction(Button button) {
        if (button.getTag() == null || "black".equals(button.getTag())) {
            button.setTag("yellow");
            selectedInterests.add(button.getText().toString());
            button.setBackgroundColor(getResources().getColor(com.google.android.libraries.places.R.color.quantum_yellowA700));
            button.setTextColor(getResources().getColor(R.color.black)); // changes the text color to black when the background is yellow
        } else {
            button.setTag("black");
            selectedInterests.remove(button.getText().toString());
            button.setBackgroundColor(getResources().getColor(R.color.black));
            button.setTextColor(getResources().getColor(R.color.white)); // changes the text color to white when the background is black
        }
    }


    public void onSubmitClick(View view) {

        if (selectedInterests.size() < 3) {
            Toast.makeText(ActivityInterests.this, "Please select at least 3 interests.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve User object
        User person = (User) getIntent().getSerializableExtra("User");

        // Create ProgressDialog
        ProgressDialog pd = new ProgressDialog(ActivityInterests.this);

        // Create Firebase User and add interests to Firestore
        pd.setMessage("Creating Account...");
        pd.show();

        mAuth.createUserWithEmailAndPassword(person.getEmail(), person.getPassword())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        pd.dismiss();

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
                        userData.put("profilePictureUrl","https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png");
                        userData.put("uid", uid);

                        db.collection("teenActivists").document(uid).set(userData)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(person.getName()).build();
                                        user.updateProfile(profileUpdates).addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            }
                                        });

                                        // All interests submitted successfully
                                        Intent intent = new Intent(ActivityInterests.this, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                    } else {
                                        Log.w(TAG, "set:failure", task1.getException());
                                        Toast.makeText(ActivityInterests.this, "Failed to set user data.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else {
                        pd.dismiss();

                        // Check the type of exception
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(ActivityInterests.this, "This email is already registered.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(ActivityInterests.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        /*if (selectedInterests.size() < 3) {
            Toast.makeText(ActivityInterests.this, "Please select at least 3 interests.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve the User object
        User person = (User) getIntent().getSerializableExtra("User");

        // Get the current FirebaseUser
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();

        // Create a map of user data
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", person.getEmail());
        userData.put("password", person.getPassword());
        userData.put("name", person.getName());
        userData.put("age", person.getAge());
        userData.put("region", person.getRegion());
        userData.put("city", person.getCity());
        userData.put("profilePictureUrl","https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png");
        userData.put("uid", uid);

        // Save the user data to Firestore
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
                    } else {
                        Log.w(TAG, "set:failure", task1.getException());
                        Toast.makeText(ActivityInterests.this, "Failed to set user data.", Toast.LENGTH_SHORT).show();
                    }
                });

        List<Task<Void>> tasks = new ArrayList<>();
        for (String interest : selectedInterests) {
            Task<Void> task = db.collection("teenActivists").document(mAuth.getCurrentUser().getUid())
                    .collection("interests").document(interest)
                    .set(new InterestData(interest));
            tasks.add(task);
        }

        Tasks.whenAll(tasks).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ActivityInterests.this, "All interests submitted successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ActivityInterests.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            } else {
                Toast.makeText(ActivityInterests.this, "Operation failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


*/
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

}

