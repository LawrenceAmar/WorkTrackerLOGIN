package com.example.worktrackerlogin;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.worktrackerlogin.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarTransparent();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show());

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_viewprofile, R.id.nav_sales,
                R.id.nav_salesrecord, R.id.nav_pog, R.id.nav_pogrecord, R.id.nav_activities, R.id.nav_activitiesrecord,
                R.id.nav_productList, R.id.nav_cropList)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Get the navigation header view
        View headerView = navigationView.getHeaderView(0);

        // Update the TextViews with user's name and email
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String usernameUser = getIntent().getStringExtra("username");

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(usernameUser);
        userRef.child("profileImageUrl").get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                String profileImageUrl = dataSnapshot.getValue(String.class);
                ImageView navImg = headerView.findViewById(R.id.navImg);
                // Load profile image using Picasso or Glide
                Glide.with(MainActivity.this)
                        .load(profileImageUrl)
                        .apply(new RequestOptions().centerCrop()) // Apply centerCrop() option
                        .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache the image
                        .into(navImg);

            } else {
                // Profile image URL does not exist
                // Handle the case where no profile image is available
            }
        }).addOnFailureListener(e -> {
            // Failed to retrieve profile image URL
            Log.e("ProfileActivity", "Failed to retrieve profile image URL: " + e.getMessage());
        });

        TextView navNameTitle = headerView.findViewById(R.id.nav_nametitle);
        TextView navEmailTitle = headerView.findViewById(R.id.nav_emailtitle);

        navNameTitle.setText(name);
        navEmailTitle.setText(email);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_viewprofile) {
                Bundle bundle = new Bundle();
                bundle.putString("username", usernameUser);
                navController.navigate(R.id.nav_viewprofile, bundle);
            }
            else if (id == R.id.nav_sales) {
                Bundle bundle = new Bundle();
                bundle.putString("username", usernameUser);
                navController.navigate(R.id.nav_sales, bundle);
            }
            else if (id == R.id.nav_salesrecord) {
                Bundle bundle = new Bundle();
                bundle.putString("username", usernameUser);
                navController.navigate(R.id.nav_salesrecord, bundle);
            }
            else if (id == R.id.nav_pog) {
                Bundle bundle = new Bundle();
                bundle.putString("username", usernameUser);
                navController.navigate(R.id.nav_pog, bundle);
            }
            else if (id == R.id.nav_pogrecord) {
                Bundle bundle = new Bundle();
                bundle.putString("username", usernameUser);
                navController.navigate(R.id.nav_pogrecord, bundle);
            }
            else if (id == R.id.nav_activities) {
                Bundle bundle = new Bundle();
                bundle.putString("username", usernameUser);
                navController.navigate(R.id.nav_activities, bundle);
            }
            else if (id == R.id.nav_activitiesrecord) {
                Bundle bundle = new Bundle();
                bundle.putString("username", usernameUser);
                navController.navigate(R.id.nav_activitiesrecord, bundle);
            }
            else if (id == R.id.nav_cropList) {
                Bundle bundle = new Bundle();
                bundle.putString("username", usernameUser);
                navController.navigate(R.id.nav_cropList, bundle);
            }
            else if (id == R.id.nav_productList) {
                Bundle bundle = new Bundle();
                bundle.putString("username", usernameUser);
                navController.navigate(R.id.nav_productList, bundle);
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void makeStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(Color.BLACK);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
