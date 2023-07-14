package com.example.meet_workshop;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OnboardingActivity extends AppCompatActivity {
    private ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

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
