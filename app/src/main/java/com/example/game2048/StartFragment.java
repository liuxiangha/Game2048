package com.example.game2048;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.game2048.databinding.FragmentStartBinding;

import org.jetbrains.annotations.NotNull;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {

    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentStartBinding startBinding = FragmentStartBinding.inflate(inflater, container, false);

        System.out.println("Get argument: " + getArguments().getString("userId"));
        final String userId     =   getArguments().getString("userId");
        final String userName   =   getArguments().getString("userName");
        startBinding.toGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", userId);
                    bundle.putString("userName", userName);
                    Navigation.findNavController(v).navigate(R.id.action_startFragment_to_mainGame, bundle);
                } else {
                    Navigation.findNavController(v).navigate(R.id.action_startFragment_to_mainGame);
                }
            }
        });

        startBinding.boardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", userId);
                    bundle.putString("userName", userName);
                    Navigation.findNavController(v).navigate(R.id.action_startFragment_to_boardFragment, bundle);
                } else {
                    NavDirections navDirections = StartFragmentDirections.
                            actionStartFragmentToBoardFragment();
                    Navigation.findNavController(v).navigate(navDirections);
                }
            }
        });

        return startBinding.getRoot();
    }
}
