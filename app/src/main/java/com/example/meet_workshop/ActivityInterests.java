package com.example.meet_workshop;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.meet_workshop.homepage.homeactivist.HomeActivity;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
        if (button.getTag() == null || "gray".equals(button.getTag())) {
            button.setTag("green");
            selectedInterests.add(button.getText().toString());
            button.setBackgroundColor(getResources().getColor(R.color.green));
        } else {
            button.setTag("gray");
            selectedInterests.remove(button.getText().toString());
            button.setBackgroundColor(getResources().getColor(R.color.gray));
        }
    }

    public void onSubmitClick(View view) {
        if (selectedInterests.size() < 3) {
            Toast.makeText(ActivityInterests.this, "Please select at least 3 interests.", Toast.LENGTH_SHORT).show();
            return;
        }

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
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(ActivityInterests.this, "Operation failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}

