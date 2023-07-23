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

    private ProgressDialog pd;


    //1
    EditText email;

    //2
    EditText descriptionOfOrg;

    //3
    EditText password;

    //4
    EditText phoneNum;

    //5
    EditText orgname;

    //6
    EditText websiteLink;

    //7

    String  selectedType;

    String[] typeList = {"Environment" , "Volunteering", "Protests", "Women's Rights", "Human Rights","Racism", "LGBTQ+", "Animals", "Petitions", "Education"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;

    private TextView checkBoxText;

    private EditText password1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_organization);
        this.checkBoxText = findViewById(R.id.checkBoxText);
        this.checkBox = findViewById(R.id.simpleCheckBox);
        mAuth = FirebaseAuth.getInstance();

        password1 = findViewById(R.id.signup_pass);

        //1
        email = findViewById(R.id.signup_email);

        //2
        password = findViewById(R.id.signup_pass);

        //3
        orgname = findViewById(R.id.signup_orgname);

        //4
        phoneNum = findViewById(R.id.signup_phone);

        //5
        websiteLink = findViewById(R.id.OrgWebLink);

        //6


        // drop down menu for regions
        autoCompleteTextView = findViewById(R.id.autocomplete_Tv);
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item, typeList);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                selectedType = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(SignUpOrganizationActivity.this, "Region:" + selectedType, Toast.LENGTH_SHORT).show();


            }
        });

        //7
        descriptionOfOrg = findViewById(R.id.DescOfOrg);





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

        //1
        String emailStr = email.getText().toString();

        //2
        String passwordStr = password.getText().toString();

        //3
        String orgNameStr = orgname.getText().toString();

        //4
        String phoneNumStr = phoneNum.getText().toString();

        //5
        String orgTypeStr = selectedType;

        //6
        String descriptionStr = descriptionOfOrg.getText().toString();

        //7
        String webLink = websiteLink.getText().toString();


        Boolean checkBoxState = checkBox.isChecked();

        if (emailStr.isEmpty() || passwordStr.isEmpty() || orgNameStr.isEmpty() || phoneNumStr.isEmpty() || orgTypeStr.isEmpty() || webLink.isEmpty() || descriptionStr.isEmpty() || checkBoxState == false) {
            Toast.makeText(this, "Please enter Organization name, Type Of Organization, email, password, Phone Number, Website Link and Description Of The WebSite", Toast.LENGTH_SHORT).show();
        } else {
            create_user(emailStr, passwordStr, orgNameStr, phoneNumStr, orgTypeStr, webLink, descriptionStr);
        }
    }

    public void OpenTermsAndGuidelines(View view) {
        String url = getString(R.string.privacy_policy);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public void create_user(String email, String password, String name, String phoneNumStr, String orgTypeStr, String webLink, String descriptionStr) {
        pd = new ProgressDialog(this);
        pd.setMessage("Creating Account...");
        pd.show();
        {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String, Object> userData = new HashMap<>();


                            //1
                            userData.put("email", email);

                            //2
                            userData.put("password", password);

                            //3
                            userData.put("organization_name", name);

                            //4
                            userData.put("phone",phoneNumStr );

                            //5
                            userData.put("description", descriptionStr);

                            //6
                            userData.put("website Link", webLink);

                            //7
                            userData.put("orgType", orgTypeStr);

                            userData.put("profilePictureUrl", "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png");
                            userData.put("uid", uid);

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
                                            Toast.makeText(this, "You're in", Toast.LENGTH_SHORT).show();
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
}