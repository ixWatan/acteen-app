package com.example.meet_workshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
        if (button.getTag() == null || "gray".equals(button.getTag())) {
            button.setBackgroundColor(getResources().getColor(R.color.green));
            button.setTag("green");
            String message = "Hello from " + button.getText().toString();
            db.collection("users").document(mAuth.getCurrentUser().getUid())
                    .collection("interests").document(button.getText().toString())
                    .set(new InterestData(message))
                    .addOnSuccessListener(documentReference -> {
                        // do something on success
                    })
                    .addOnFailureListener(e -> {
                        // do something on failure
                    });
        } else {
            button.setBackgroundColor(getResources().getColor(R.color.gray));
            button.setTag("gray");
            db.collection("users").document(mAuth.getCurrentUser().getUid())
                    .collection("interests").document(button.getText().toString())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // do something on success
                    })
                    .addOnFailureListener(e -> {
                        // do something on failure
                    });
        }
    }
}
