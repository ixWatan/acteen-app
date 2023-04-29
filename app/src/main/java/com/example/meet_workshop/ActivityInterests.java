package com.example.meet_workshop;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityInterests extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);
    }

    public void onButtonClick(View view) {
        Button button = (Button) view;
        if (button.getTag() == null || "gray".equals(button.getTag())) {
            button.setBackgroundColor(getResources().getColor(R.color.green));
            button.setTag("green");
        } else {
            button.setBackgroundColor(getResources().getColor(R.color.gray));
            button.setTag("gray");
        }
    }
}
