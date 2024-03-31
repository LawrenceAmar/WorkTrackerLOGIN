package com.example.worktrackerlogin.profile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.worktrackerlogin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText signupName,signupUsername, signupEmail,signupAddress,signupContact,signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    String selectedTerritory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        makeStatusBarTransparent();

        // territory spinner
        Spinner spinner = findViewById(R.id.Territory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Territories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //declarations
        signupName = findViewById(R.id.signup_name);
        signupUsername = findViewById(R.id.signup_username);
        signupEmail = findViewById(R.id.signup_email);
        signupAddress = findViewById(R.id.signup_address);
        signupContact = findViewById(R.id.signup_contactn);

        if (signupContact != null) {
            // Set input type to only accept numbers
            signupContact.setInputType(InputType.TYPE_CLASS_NUMBER);
            // Add text watcher to enforce 11-digit limit
            signupContact.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    // Enforce 11-digit limit
                    if (s.length() > 11) {
                        signupContact.setText(s.subSequence(0, 11));
                        signupContact.setSelection(11);
                    }
                }
            });
        }

        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(view -> {

            FirebaseDatabase database = FirebaseDatabase.getInstance("https://scl-filipinas-work-tracker-default-rtdb.asia-southeast1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference("users");

            String name = signupName.getText().toString();
            String username = signupUsername.getText().toString();
            String email = signupEmail.getText().toString();
            String address = signupAddress.getText().toString();
            String contact = signupContact.getText().toString();
            String password = signupPassword.getText().toString();
            String territory = selectedTerritory;
            String access = "employee";

            // Check if the name contains numbers
            if (containsNumbers(name)) {
                signupName.setError("Name should not contain numbers");
                return; // Exit the method
            }
            if (!isValidEmail(email)) {
                signupEmail.setError("Invalid email address");
                return; // Exit the method
            }
            if (!isValidPhilippinePhoneNumber(contact)) {
                signupContact.setError("Invalid Philippine phone number");
                return; // Exit the method
            }


            // Check if the username already exists in the database
            myRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Username already exists
                        signupUsername.setError("Username is already taken");
                    } else {
                        // Username is available, proceed with signup
                        HelperClass helperClass = new HelperClass(name, username, email, address,contact, territory, password, access);
                        myRef.child(username).setValue(helperClass);

                        Toast.makeText(SignupActivity.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                    Toast.makeText(SignupActivity.this, "Error checking username availability", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private boolean containsNumbers(String s) {
        // Iterate through each character of the string and check if it's a number
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                return true; // Found a digit, return true
            }
        }
        return false; // No digits found
    }
    private boolean isValidEmail(String email) {
        // Regular expression pattern to validate email address
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }
    private boolean isValidPhilippinePhoneNumber(String phoneNumber) {
        // Regular expression pattern to validate Philippine phone number
        String phoneNumberPattern = "(09|\\+639)\\d{9}";
        return phoneNumber.matches(phoneNumberPattern);
    }

    private void makeStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(Color.TRANSPARENT);
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

