package com.example.meet_workshop;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnboardingFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnboardingFragment1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    
    private Button Next;
    private Button Skip;




    public OnboardingFragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Onboardingfragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static OnboardingFragment1 newInstance(String param1, String param2) {
        OnboardingFragment1 fragment = new OnboardingFragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_onboardingfragment1, container, false);

        Next = view.findViewById(R.id.NextBtn);

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the next fragment
                NextFunction();
            }
        });

        Skip = view.findViewById(R.id.SkipBtn);

        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the other fragment
                SkipFunction();
            }
        });

        return view;
    }


    private void SkipFunction() {

        // Create a new instance of the OnboardingFragment2.
        OnboardingFragment2 onboardingFragment2 = new OnboardingFragment2();

        // Create a FragmentTransaction for the current FragmentManager.
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace the current fragment (this) with onboardingFragment2.
        transaction.replace(R.id.fragment_container, onboardingFragment2);

        // If you want to add the transaction to the back stack so the user can navigate back.
        transaction.addToBackStack(null);

        // Commit the transaction.
        transaction.commit();

    }

    private void NextFunction() {
        // Create a new instance of the OnboardingFragment2.
        OnboardingFragment2 onboardingFragment2 = new OnboardingFragment2();
        // Create a FragmentTransaction for the current FragmentManager.
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace the current fragment (this) with onboardingFragment2.
        transaction.replace(R.id.fragment_container, onboardingFragment2);

        // If you want to add the transaction to the back stack so the user can navigate back.
        transaction.addToBackStack(null);

        // Commit the transaction.
        transaction.commit();
    }
}