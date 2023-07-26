package com.example.meet_workshop.SlideShowAct;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.meet_workshop.R;
import com.example.meet_workshop.SignUpOrganizationActivity;
import com.example.meet_workshop.SlideShowOrg.SlideShowActivityOrg2;

public class SlideShowActivityAct1 extends AppCompatActivity {


    private Button Next;

    private Button Skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show_act1);

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
        Intent intent = new Intent(this, SlideShowActivityAct2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }
}