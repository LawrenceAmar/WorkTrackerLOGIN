package com.example.worktrackerlogin.profile;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.worktrackerlogin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ViewProfileFragment extends Fragment {

    TextView profileName, profileEmail, profileUsername, profileAddress, profileContact, profileTerritory, profilePassword;
    TextView titleName, titleEmail;
    private ImageView profileImageView;
    Button editButton;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static ViewProfileFragment newInstance(String param1, String param2) {
        ViewProfileFragment fragment = new ViewProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profileUsername = view.findViewById(R.id.profileUsername);
        profileAddress = view.findViewById(R.id.profileAddress);
        profileContact = view.findViewById(R.id.profileContact);
        profileTerritory = view.findViewById(R.id.profileTerritory);
        profilePassword = view.findViewById(R.id.profilePassword);
        titleName = view.findViewById(R.id.titleName);
        titleEmail = view.findViewById(R.id.titleEmail);
        profileImageView = view.findViewById(R.id.profileImg);
        editButton = view.findViewById(R.id.editButton);

        Log.d("ViewProfileFragment", "Edit button initialized: " + (editButton != null));
        editButton.setOnClickListener(v -> {
            EditProfileFragment editProfileFragment = new EditProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putString("name", profileName.getText().toString());
            bundle.putString("email", profileEmail.getText().toString());
            bundle.putString("username", profileUsername.getText().toString());
            bundle.putString("address", profileAddress.getText().toString());
            bundle.putString("contact", profileContact.getText().toString());
            bundle.putString("territory", profileTerritory.getText().toString());
            bundle.putString("password", profilePassword.getText().toString());
            editProfileFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, editProfileFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Make status bar transparent
        makeStatusBarTransparent();

        // Get username from arguments
        Bundle args = getArguments();
        if (args != null) {
            String userUsername = args.getString("username");
            // Query Firebase database for user data
            DatabaseReference reference = FirebaseDatabase.getInstance("https://scl-filipinas-work-tracker-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users");
            Query query = reference.orderByChild("username").equalTo(userUsername);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            // Retrieve user data from the snapshot
                            String name = dataSnapshot.child("name").getValue(String.class);
                            String email = dataSnapshot.child("email").getValue(String.class);
                            String username = dataSnapshot.child("username").getValue(String.class);
                            String password = dataSnapshot.child("password").getValue(String.class);
                            String address = dataSnapshot.child("address").getValue(String.class);
                            String contact = dataSnapshot.child("contactn").getValue(String.class);
                            String territory = dataSnapshot.child("territory").getValue(String.class);
                            String image = dataSnapshot.child("profileImageUrl").getValue(String.class);

                            // Set user data to TextViews
                            titleName.setText(name);
                            titleEmail.setText(email);
                            profileName.setText(name);
                            profileEmail.setText(email);
                            profileUsername.setText(username);
                            profilePassword.setText(password);
                            profileAddress.setText(address);
                            profileContact.setText(contact);
                            profileTerritory.setText(territory);

                            // Load profile image using Glide
                            Glide.with(ViewProfileFragment.this)
                                    .load(image)
                                    .apply(new RequestOptions().centerCrop())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(profileImageView);

                            // Add log statements to track the retrieved data
                            Log.d("ViewProfileActivity", "Name: " + name);
                            Log.d("ViewProfileActivity", "Email: " + email);
                            Log.d("ViewProfileActivity", "Username: " + username);
                            Log.d("ViewProfileActivity", "Password: " + password);
                            Log.d("ViewProfileActivity", "Address: " + address);
                            Log.d("ViewProfileActivity", "Contact: " + contact);
                            Log.d("ViewProfileActivity", "Territory: " + territory);
                            Log.d("ViewProfileActivity","ImageURI: "+ image);
                        }
                    } else {
                        Log.e("ViewProfileActivity", "No user found in the database with username: " + userUsername);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {
            Log.e("ViewProfileFragment", "Arguments are null");
        }
    }

    private void makeStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = requireActivity().getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(Color.rgb(28, 33, 32));
        }
    }
}
