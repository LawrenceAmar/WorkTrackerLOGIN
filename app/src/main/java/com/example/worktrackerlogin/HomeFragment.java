package com.example.worktrackerlogin;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find buttons by their IDs
        Button toSales = view.findViewById(R.id.tosales);
        Button toActivities = view.findViewById(R.id.toactivities);
        Button toPog = view.findViewById(R.id.topog);

        // Fetch the "username" passed to this Fragment
        String usernameUser = requireActivity().getIntent().getStringExtra("username");

        // Setup NavController for navigation actions
        NavController navController = Navigation.findNavController(view);

        // Set click listeners for buttons to navigate along with passing "username"
        toSales.setOnClickListener(v -> navigateAndPassUsername(navController, R.id.nav_salesrecord, usernameUser));
        toActivities.setOnClickListener(v -> navigateAndPassUsername(navController, R.id.nav_activitiesrecord, usernameUser));
        toPog.setOnClickListener(v -> navigateAndPassUsername(navController, R.id.nav_pogrecord, usernameUser));

        Window window = requireActivity().getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    private void navigateAndPassUsername(NavController navController, int destinationId, String username) {
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        navController.navigate(destinationId, bundle);
    }

}
