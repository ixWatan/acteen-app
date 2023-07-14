package com.example.meet_workshop;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class OnboardingPagerAdapter extends FragmentPagerAdapter {

    private boolean isLoggedIn;

    public OnboardingPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    public OnboardingPagerAdapter(FragmentManager fm, boolean isLoggedIn) {
        super(fm);
        this.isLoggedIn = isLoggedIn;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // Return the fragment corresponding to the given position
        switch (position) {
            case 0:
                return new OnboardingFragment1();
            case 1:
                return new OnboardingFragment2();
            case 2:
                return new OnboardingFragment3();
            case 3:
                return new OnboardingFragment4();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Return the total number of fragments
        return 4;
    }




}
