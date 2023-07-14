package com.example.meet_workshop;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.meet_workshop.homepage.homeactivist.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OnboardingActivity extends AppCompatActivity {
    private ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if(currentUser != null) {
            Intent intent = new Intent(OnboardingActivity.this, HomeActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(OnboardingActivity.this, "Not logged in before", Toast.LENGTH_SHORT).show();
        }

        viewPager = findViewById(R.id.viewPager);

        OnboardingPagerAdapter adapter = new OnboardingPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // Add a page change listener to handle button visibility and navigation
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Empty implementation
            }

            @Override
            public void onPageSelected(int position) {
                // Show the "Let's Start" button on the last page
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Empty implementation
            }
        });

    }


}
