package com.example.meet_workshop.SlideShowAct;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meet_workshop.R;

public class SlideShowActivityAct2 extends AppCompatActivity {

    private Button Next;

    private Button Skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show_act2);

        Next = findViewById(R.id.NextBtn);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the profileImageButton click event
                NextFunction();
            }
        });


        Skip = findViewById(R.id.SkipBtn);
        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the profileImageButton click event
                SkipFunction();
            }
        });
    }

    private void SkipFunction() {
        Intent intent = new Intent(this, SlideShowAct5.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    private void NextFunction() {
        Intent intent = new Intent(this, SlideShowActivityAct3.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}