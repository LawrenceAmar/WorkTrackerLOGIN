package com.example.worktrackerlogin;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class EditProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    EditText editName, editEmail, editUsername, editPassword, editAddress, editContact;
    Button saveButton;
    String nameUser, emailUser, usernameUser, addressUser, contactUser, territoryUser, passwordUser;
    String selectedTerritory = "";
    DatabaseReference reference;
    private String imageUrl;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView profileImageView;
    private Button changeImageButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        Spinner spinner = view.findViewById(R.id.Territory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Territories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        reference = FirebaseDatabase.getInstance("https://scl-filipinas-work-tracker-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users");

        editName = view.findViewById(R.id.editName);
        editEmail = view.findViewById(R.id.editEmail);
        editUsername = view.findViewById(R.id.editUsername);
        editAddress = view.findViewById(R.id.editAddress);
        editContact = view.findViewById(R.id.editContact);
        editPassword = view.findViewById(R.id.editPassword);

        profileImageView = view.findViewById(R.id.profileImg);
        changeImageButton = view.findViewById(R.id.changeImageButton);
        changeImageButton.setOnClickListener(v -> openFileChooser());

        saveButton = view.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(view1 -> {

            DatabaseReference userRef = reference.child(usernameUser); // Reference to the user's data

            // Create a map to hold the updates
            Map<String, Object> updates = new HashMap<>();

            if (isNameChanged()) {
                updates.put("name", editName.getText().toString());
            }
            if (isUsernameChanged()) {
                updates.put("username", editUsername.getText().toString());
            }
            if (isEmailChanged()) {
                updates.put("email", editEmail.getText().toString());
            }
            if (isAddressChanged()) {
                updates.put("address", editAddress.getText().toString());
            }
            if (isContactChanged()) {
                updates.put("contactn", editContact.getText().toString());
            }
            if (isTerritoryChanged()) {
                updates.put("territory", selectedTerritory);
            }
            if (isPasswordChanged()) {
                updates.put("password", editPassword.getText().toString());
            }

            // Check if there are any updates
            if (!updates.isEmpty()) {
                // Perform all updates atomically
                userRef.updateChildren(updates)
                        .addOnSuccessListener(aVoid -> {
                            // All updates were successful
                            if (isImageChanged()) {
                                // If image is changed, upload the new image
                                uploadImage();
                                logoutAndNavigateToLogin();
                            } else {
                                logoutAndNavigateToLogin();
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Failed to perform updates
                            Toast.makeText(requireContext(), "Failed to save changes: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                // No changes found
                Toast.makeText(requireContext(), "No Changes Found", Toast.LENGTH_SHORT).show();
            }
        });
        showData(view);
        return view;
    }
    private void logoutAndNavigateToLogin() {
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish(); // Finish the current activity to prevent going back to it
    }

    public boolean isUsernameChanged(){
        String newUsername = editUsername.getText().toString();

        if (!usernameUser.equals(newUsername)) {
            reference.child(usernameUser).child("username").setValue(newUsername);
            usernameUser = newUsername;
            return true;
        }

        return false;
    }
    public boolean isImageChanged() {
        // Check if either imageUri or imageUrl is not null
        if (imageUri != null || imageUrl != null) {
            // Check if both imageUri and imageUrl are not null and different
            if (imageUri != null && imageUrl != null && !imageUrl.equals(imageUri.toString())) {
                return true; // Return true if they are different
            }
            // If either imageUri or imageUrl is not null, consider it as a change
            return true;
        }
        // If both imageUri and imageUrl are null, no change
        return false;
    }
    public boolean isNameChanged(){
        if (!nameUser.equals(editName.getText().toString())){
            reference.child(usernameUser).child("name").setValue(editName.getText().toString());
            nameUser = editName.getText().toString();
            return true;
        } else{
            return false;
        }
    }

    public boolean isEmailChanged(){
        if (!emailUser.equals(editEmail.getText().toString())){
            reference.child(usernameUser).child("email").setValue(editEmail.getText().toString());
            emailUser = editEmail.getText().toString();
            return true;
        } else{
            return false;
        }
    }

    public boolean isAddressChanged(){
        String newAddress = editAddress.getText().toString();

        if (!addressUser.equals(newAddress)) {
            reference.child(usernameUser).child("address").setValue(newAddress);
            addressUser = newAddress;
            return true;
        }

        return false;
    }

    public boolean isContactChanged(){
        String newContact = editContact.getText().toString();

        if (!contactUser.equals(newContact)) {
            reference.child(usernameUser).child("contactn").setValue(newContact);
            contactUser = newContact;
            return true;
        }

        return false;
    }

    public boolean isTerritoryChanged() {
        String newTerritory = selectedTerritory;
        // Check if the selected territory is different from the current territoryUser
        if (!territoryUser.equals(newTerritory)) {
            // Update the territoryUser with the newTerritory
            territoryUser = newTerritory;
            // Update the territory in the Firebase Realtime Database
            reference.child(usernameUser).child("territory").setValue(newTerritory)
                    .addOnSuccessListener(aVoid -> {
                        // Territory updated successfully
                        Toast.makeText(requireContext(), "Territory updated", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Failed to update territory
                        Toast.makeText(requireContext(), "Failed to update territory: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
            return true; // Territory changed
        }
        return false; // No changes in territory
    }

    public boolean isPasswordChanged(){
        if (!passwordUser.equals(editPassword.getText().toString())){
            reference.child(usernameUser).child("password").setValue(editPassword.getText().toString());
            passwordUser = editPassword.getText().toString();
            return true;
        } else{
            return false;
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
            uploadImage();
        }
    }

    private void uploadImage() {
        if (imageUri != null) {
            // Initialize Firebase Storage reference
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("profile_images");
            // Generate unique file name for the image
            String fileName = System.currentTimeMillis() + "." + getFileExtension(imageUri);
            // Reference to the image file in Firebase Storage
            StorageReference fileRef = storageRef.child(fileName);
            // Upload image to Firebase Storage
            fileRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the download URL of the uploaded image
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Save the download URL locally
                            imageUrl = uri.toString();
                            // Update the profile image URL in Firebase Realtime Database
                            DatabaseReference userRef = reference.child(usernameUser);
                            userRef.child("profileImageUrl").setValue(imageUrl)
                                    .addOnSuccessListener(aVoid -> {
                                        // Image URL updated successfully
                                        Toast.makeText(requireContext(), "Profile image updated, Please Login Again", Toast.LENGTH_SHORT).show();
                                        // After updating the image, logout and navigate to login
                                        logoutAndNavigateToLogin();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Failed to update image URL
                                        Toast.makeText(requireContext(), "Failed to update profile image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }).addOnFailureListener(e -> {
                            // Handle failure to get image URL
                            Toast.makeText(requireContext(), "Failed to get image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Handle unsuccessful uploads
                        Toast.makeText(requireContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = requireContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void showData(View rootView) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            String userUsername = arguments.getString("username");
            Log.d("EditProfileFragment", "User Username: " + userUsername);

            DatabaseReference reference = FirebaseDatabase.getInstance("https://scl-filipinas-work-tracker-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users");
            Query query = reference.orderByChild("username").equalTo(userUsername);
            query.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Log.d("EditProfileFragment", "DataSnapshot: " + dataSnapshot.getValue());
                            nameUser = dataSnapshot.child("name").getValue(String.class);
                            emailUser = dataSnapshot.child("email").getValue(String.class);
                            usernameUser = dataSnapshot.child("username").getValue(String.class);
                            addressUser = dataSnapshot.child("address").getValue(String.class);
                            contactUser = dataSnapshot.child("contactn").getValue(String.class);
                            territoryUser = dataSnapshot.child("territory").getValue(String.class);
                            passwordUser = dataSnapshot.child("password").getValue(String.class);
                            String image = dataSnapshot.child("profileImageUrl").getValue(String.class);

                            // Set the retrieved data to EditText fields
                            editName.setText(nameUser);
                            editEmail.setText(emailUser);
                            editUsername.setText(usernameUser);
                            editAddress.setText(addressUser);
                            editContact.setText(contactUser);
                            editPassword.setText(passwordUser);

                            // Set the selected territory in the spinner
                            Spinner spinner = rootView.findViewById(R.id.Territory);
                            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.Territories, android.R.layout.simple_spinner_item);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);
                            if (territoryUser != null) {
                                int position = adapter.getPosition(territoryUser);
                                if (position != -1) {
                                    spinner.setSelection(position);
                                }
                            }

                            // Load profile image using Glide
                            Glide.with(EditProfileFragment.this)
                                    .load(image)
                                    .apply(new RequestOptions().centerCrop())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(profileImageView);
                        }
                    } else {
                        Log.e("EditProfileFragment", "No user data found for username: " + userUsername);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("EditProfileFragment", "Database query cancelled: " + error.getMessage());
                }
            });
        } else {
            Log.e("EditProfileFragment", "Arguments are null");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedTerritory = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
