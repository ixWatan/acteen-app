package com.example.meet_workshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    private FirebaseAuth mAuth;
    private EditText emailEditText;

    private ProgressDialog pd;

    String[] regionsList = {"Jerusalem", "Northern District", "Haifa", "West Bank", "Central District", "Tel Aviv", "Southern District"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;


    private CheckBox checkBox;

    private Boolean checkBoxState;

    private TextView checkBoxText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private EditText ageEditText;
    private EditText cityEditText;
    private EditText password;

    String selectedRegion;

    private User person = new User(null, null, null, null, null, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();


        // drop down menu for regions
        autoCompleteTextView = findViewById(R.id.autocomplete_Tv);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, regionsList);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                selectedRegion = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(SignUpActivity.this, "Region:" + selectedRegion, Toast.LENGTH_SHORT).show();


            }
        });


        this.checkBoxText = findViewById(R.id.checkBoxText);
        this.checkBox = findViewById(R.id.simpleCheckBox);
        this.emailEditText = findViewById(R.id.signup_email);
        this.passwordEditText = findViewById(R.id.signup_pass);
        this.nameEditText = findViewById(R.id.signup_name);
        this.ageEditText = findViewById(R.id.signup_age);
        this.cityEditText = findViewById(R.id.signup_city);
        password = findViewById(R.id.signup_pass);

        ProgressDialog pd;

    }

    public void goToHome(View view) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String age = ageEditText.getText().toString();
        String city = cityEditText.getText().toString();

        Boolean checkBoxState = checkBox.isChecked();

        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || age.isEmpty() || selectedRegion.isEmpty() || city.isEmpty() || checkBoxState == false) {
            Toast.makeText(this, "Please enter email, password, name .., Or accept terms and conditions", Toast.LENGTH_SHORT).show();
        } else {
            this.person.setEmail(email);
            this.person.setPassword(password);
            this.person.setName(name);
            this.person.setAge(age);
            this.person.setCity(city);
            this.person.setRegion(selectedRegion);


            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    String regionsList = adapterView.getItemAtPosition(position).toString();

                }
            });

            createUser(person);
        }
    }

    private void checkPostsCollectionExistence() {

    }

    public void GoMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void OpenTermsAndGuidelines(View view) {
        String url = getString(R.string.privacy_policy);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }


    public void createUser(User person) {
        Intent intent = new Intent(SignUpActivity.this, ActivityInterests.class);
        intent.putExtra("User", person);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//----------------------------------------------------------------------------------------
       /*pd = new ProgressDialog(this);
       pd.setMessage("Creating Account...");
       pd.show();
       mAuth.createUserWithEmailAndPassword(person.getEmail(), person.getPassword())
               .addOnCompleteListener(this, task -> {
                   if (task.isSuccessful()) {
                       FirebaseUser user = mAuth.getCurrentUser();
                       String uid = user.getUid();

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
                       Intent intent = new Intent(SignUpActivity.this, ActivityInterests.class);
                       intent.putExtra("User", person);
                       startActivity(intent);
                       overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                   } else {
                       // Check the type of exception
                       if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                           Toast.makeText(SignUpActivity.this, "This email is already registered.", Toast.LENGTH_SHORT).show();
                       } else {
                           Log.w(TAG, "createUserWithEmail:failure", task.getException());
                           Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                       }
                   }

                   pd.dismiss();

               });*/


        //-----------------------------------------------------------------------------------------------


        /*pd = new ProgressDialog(this);
        pd.setMessage("Creating Account...");
        pd.show();
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
                        userData.put("profilePictureUrl","https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png");
                        userData.put("uid", uid);


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
                                        Intent intent = new Intent(SignUpActivity.this, ActivityInterests.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


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

                    pd.dismiss();

                });*/
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}


