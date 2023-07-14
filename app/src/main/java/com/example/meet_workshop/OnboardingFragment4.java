package com.example.meet_workshop;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class OnboardingFragment4 extends Fragment {

    // Rest of the code

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_onboarding4, container, false);


        // Find and assign the startButton within the inflated layout
        Button startButton = rootView.findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the MainActivity
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

                // Finish the current activity (OnboardingActivity) if needed
                getActivity().finish();
            }
        });

        return rootView;
    }
}
