package com.example.meet_workshop;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
public class ActivityInterests extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

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
                // Perform database operation for button
                if (button.getTag() == null || "gray".equals(button.getTag())) {
                    // The button is not selected yet, add information to the database
                    button.setTag("green");

                    // Add data to Firestore
                    String message = "Hello from " + button.getText().toString();
                    db.collection("users").document(mAuth.getCurrentUser().getUid())
                            .collection("interests").document(button.getText().toString())
                            .set(new InterestData(message))
                            .addOnSuccessListener(documentReference -> {
                                    // This is an example, you can implement your own logic here
                                    button.setBackgroundColor(getResources().getColor(R.color.green));
                                    Toast.makeText(ActivityInterests.this, "Operation successful", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                    // This is an example, you can implement your own logic here
                                    Toast.makeText(ActivityInterests.this, "Operation failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    // The button is already selected, remove information from the database
                    button.setTag("gray");

                    // Remove data from Firestore
                    db.collection("users").document(mAuth.getCurrentUser().getUid())
                            .collection("interests").document(button.getText().toString())
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                button.setBackgroundColor(getResources().getColor(R.color.green));
                                Toast.makeText(ActivityInterests.this, "Operation successful", Toast.LENGTH_SHORT).show();                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(ActivityInterests.this, "Operation failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
                break;
            case R.id.my_button1:
                // do something for button 2
                break;
            case R.id.my_button2:
                // do something for button 2
                break;
            case R.id.my_button3:
                // do something for button 2
                break;
            case R.id.my_button4:
                // do something for button 2
                break;
            case R.id.my_button5:
                // do something for button 2
                break;
            case R.id.my_button6:
                // do something for button 2
                break;
            case R.id.my_button7:
                // do something for button 2
                break;
            case R.id.my_button8:
                // do something for button 2
                break;
            case R.id.my_button9:
                // do something for button 2
                break;
            case R.id.my_button10:
                // do something for button 2
                break;
            case R.id.my_button11:
                // do something for button 2
                break;
            case R.id.my_button12:
                // do something for button 2
                break;
            case R.id.my_button13:
                // do something for button 2
                break;

            case R.id.my_button14:
                // do something for button 2
                break;

            case R.id.my_button15:
                // do something for button 2
                break;
            default:
                // if the id does not match any case
                break;
        }
    }

}
