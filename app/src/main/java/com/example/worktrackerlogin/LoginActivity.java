package com.example.worktrackerlogin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText loginusername, loginPassword;
    Button loginButton;
    TextView signupRedirectText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        makeStatusBarTransparent();
        loginusername = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(view -> {
            if (!validateUsername() | !validatePassword()){

            } else {
                checkUser();
            }
        });

        signupRedirectText.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
    private void makeStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
    public Boolean validateUsername(){
        String val = loginusername.getText().toString();
        if (val.isEmpty()){
            loginusername.setError("Username cannot be empty");
            return false;
        } else {
            loginusername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String val = loginPassword.getText().toString();
        if (val.isEmpty()){
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser(){
        String userUsername = loginusername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://scl-filipinas-work-tracker-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    loginusername.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    if (Objects.equals(passwordFromDB, userPassword)){
                        loginusername.setError(null);

                        // Pass the data including image URI using intent
                        String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                        String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);
                        String addressFromDB = snapshot.child(userUsername).child("address").getValue(String.class);
                        String contactFromDB = snapshot.child(userUsername).child("contactn").getValue(String.class);
                        String territoryFromDB = snapshot.child(userUsername).child("territory").getValue(String.class);
                        String imageUriFromDB = snapshot.child(userUsername).child("imageUri").getValue(String.class);
                        String access = snapshot.child(userUsername).child("access").getValue(String.class);
                        Intent intent = null;

                        loginusername.setText("");
                        loginPassword.setText("");

                        assert access != null;
                        if (access.equals("employee")) {
                            intent = new Intent(LoginActivity.this, MainActivity.class);
                        }
                        else if (access.equals("admin")) {
                            intent = new Intent(LoginActivity.this, MainActivity2.class);
                        }
                        assert intent != null;
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("password", passwordFromDB);
                        intent.putExtra("address", addressFromDB);
                        intent.putExtra("contactn", contactFromDB);
                        intent.putExtra("territory", territoryFromDB);
                        intent.putExtra("imageUri", imageUriFromDB);
                        startActivity(intent);

                        /*
                            FOR SPECIFIC USER DATA
                         */
                        Intent activityIntent = getIntent();
                        String username = activityIntent.getStringExtra("username");

                        activityFragment fragment = new activityFragment();
                        Bundle args = new Bundle();
                        args.putString("username", username);
                        fragment.setArguments(args);

                        pogFragment fragment1 = new pogFragment();
                        Bundle args1 = new Bundle();
                        args1.putString("username", username);
                        fragment1.setArguments(args1);

                        salesFragment fragment2 = new salesFragment();
                        Bundle args2 = new Bundle();
                        args2.putString("username", username);
                        fragment2.setArguments(args2);

                    } else {
                        loginPassword.setError("Invalid Credentials");
                        loginPassword.requestFocus();
                    }
                } else {
                    loginusername.setError("User does not exist");
                    loginusername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
