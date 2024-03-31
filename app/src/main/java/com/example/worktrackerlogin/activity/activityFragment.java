package com.example.worktrackerlogin.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.worktrackerlogin.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class activityFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    // Declare UI elements
    Spinner activityType, activityCrop;
    Button activitySave, activityUpload, activityDate;
    EditText cslName, farmerReach, contactPerson, contactNumber;

    // Strings to store selected values
    String storeDate = "";
    String storeType = "";
    String storeCrop = "";

    // For date picker
    private DatePickerDialog datePickerDialog;

    // Specific User
    private String username;

    public activityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_activities, container, false);

        // Retrieve username from arguments
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }

        // Initialize UI elements
        activityDate = view.findViewById(R.id.activityDate);
        activityType = view.findViewById(R.id.activityType);
        activityCrop = view.findViewById(R.id.activityCrop);
        activitySave = view.findViewById(R.id.activitySave);
        activityUpload = view.findViewById(R.id.activityUpload);
        cslName = view.findViewById(R.id.cslName);
        farmerReach = view.findViewById(R.id.farmerReach);
        contactPerson = view.findViewById(R.id.contactPerson);
        contactNumber = view.findViewById(R.id.contactNumber);

        if (contactNumber != null) {
            contactNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 11) {
                        contactNumber.setText(s.subSequence(0, 11));
                        contactNumber.setSelection(11);
                    }
                }
            });
        }

        activityDate.setText("");
        initDatePicker();

        /*
         Spinners
         */
        // Spinner for Activity Type
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.typeOfactivity,
                R.layout.spinner_layout
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityType.setAdapter(adapter);
        activityType.setOnItemSelectedListener(this);

        // Spinner for Crops
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.Crops,
                R.layout.spinner_layout
        );
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityCrop.setAdapter(adapter1);
        activityCrop.setOnItemSelectedListener(this);

        // Load previously selected values
        loadPreviousSelections();

        // UPLOAD BUTTON
        activityUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int validationCode = isValidInput();
                if (validationCode == 0) {

                    String storeCSL = cslName.getText().toString();
                    String storeReach = farmerReach.getText().toString();
                    String storeContact = contactPerson.getText().toString();
                    String storeNumber = contactNumber.getText().toString();

                    // Storing data to Firebase
                    storeDataToFirebase(username, storeCSL, storeReach, storeContact, storeNumber);

                    clearLocalData();

                    // Clear Spinners
                    activityDate.setText("");
                    activityType.setSelection(0);
                    activityCrop.setSelection(0);

                    // Clear EditText fields
                    cslName.setText("");
                    farmerReach.setText("");
                    contactPerson.setText("");
                    contactNumber.setText("");

                } else if (validationCode == 1) {
                    Toast.makeText(requireContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                } else if (validationCode == 2) {
                    Toast.makeText(requireContext(), "Please input a number for reach and contact number.", Toast.LENGTH_SHORT).show();
                } else if (validationCode == 3) {
                    Toast.makeText(requireContext(), "Reach must be a numerical value above 0.", Toast.LENGTH_SHORT).show();
                } else if (validationCode == 4) {
                    Toast.makeText(requireContext(), "Contact number must be valid.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // SAVE BUTTON
        activitySave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String storeCSL = cslName.getText().toString();
                String storeReach = farmerReach.getText().toString();
                String storeContact = contactPerson.getText().toString();
                String storeNumber = contactNumber.getText().toString();
                String currentUser = username;

                // Storing data locally
                storeDataLocally(currentUser, storeCSL, storeReach, storeContact, storeNumber);

                Toast.makeText(requireContext(), "Progress saved", Toast.LENGTH_SHORT).show();
            }
        });

        // For Date Button
        activityDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(v);
            }
        });

        return view;
    }

    // Method to check if all fields are in proper format
    private int isValidInput() {
        String csl = cslName.getText().toString().trim();
        String reach = farmerReach.getText().toString().trim();
        String person = contactPerson.getText().toString().trim();
        String number = contactNumber.getText().toString().trim();

        // Check if spinners are selected
        if (storeDate.isEmpty() || storeType.isEmpty() || storeCrop.isEmpty()) {
            return 1; // Indicates empty fields
        }

        // Check if EditText fields are empty
        if (csl.isEmpty() || reach.isEmpty() || person.isEmpty() || number.isEmpty()) {
            return 1; // Indicates empty fields
        }

        // Check if farmers reach and contact number are valid
        try {
            double reachValue = Double.parseDouble(reach);
            double numberValue = Double.parseDouble(number);

            // Additional check for values
            if (reachValue <= 0 || numberValue <= 0) {
                if (reachValue <= 0) {
                    return 3; // Indicates price <= 0
                }
                if (numberValue <= 0) {
                    return 4; // Indicates value <= 0
                }
            }
        } catch (NumberFormatException e) {
            return 2; // Indicates non-numerical values
        }
        return 0; // Indicates no error
    }

    // Store data to Firebase
    private void storeDataToFirebase(String username, String storeCSL, String storeReach, String storePerson, String storeNumber) {

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://scl-filipinas-work-tracker-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("users").child(username).child("Activities");

        int reach = Integer.parseInt(storeReach);

        activityDataClass activitydataclass = new activityDataClass(storeDate, storeCSL, storeType, storeCrop, storePerson, reach, storeNumber);

        myRef.push().setValue(activitydataclass);

        Toast.makeText(requireContext(), "Data uploaded", Toast.LENGTH_SHORT).show();
    }


    // Store data locally
    private void storeDataLocally(String username, String storeCSL, String storeReach, String storePerson, String storeNumber) {

        SharedPreferences activity_preference = requireContext().getSharedPreferences(username + "ActivityLocalData", requireContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = activity_preference.edit();

        editor.putString("storedDate", storeDate);
        editor.putString("storedCSL", storeCSL);
        editor.putString("storedReach", storeReach);
        editor.putString("storedPerson", storePerson);
        editor.putString("storedNumber", storeNumber);
        editor.putString("storedType", storeType);
        editor.putString("storedCrop", storeCrop);
        editor.apply();
    }

    // Load previously selected values
    private void loadPreviousSelections() {

        SharedPreferences activity_preference = requireContext().getSharedPreferences(username + "ActivityLocalData", requireContext().MODE_PRIVATE);

        storeDate = activity_preference.getString("storedDate", "");
        storeType = activity_preference.getString("storedType", "");
        storeCrop = activity_preference.getString("storedCrop", "");
        String storedCSL = activity_preference.getString("storedCSL", "");
        String storedReach = activity_preference.getString("storedReach", "");
        String storedPerson = activity_preference.getString("storedPerson", "");
        String storedNumber = activity_preference.getString("storedNumber", "");

        // Set the selections in spinners and date button
        if (!storeDate.isEmpty()) {
            activityDate.setText(storeDate);
        }
        if (!storeType.isEmpty()) {
            int productPosition = ((ArrayAdapter<String>) activityType.getAdapter()).getPosition(storeType);
            activityType.setSelection(productPosition);
        }
        if (!storeCrop.isEmpty()) {
            int customerPosition = ((ArrayAdapter<String>) activityCrop.getAdapter()).getPosition(storeCrop);
            activityCrop.setSelection(customerPosition);
        }

        // Set the stored values for customer name, unit, price, and value to their respective EditText fields
        if (!storedCSL.isEmpty()) {
            cslName.setText(storedCSL);
        }
        if (!storedReach.isEmpty()) {
            farmerReach.setText(storedReach);
        }
        if (!storedPerson.isEmpty()) {
            contactPerson.setText(storedPerson);
        }
        if (!storedNumber.isEmpty()) {
            contactNumber.setText(storedNumber);
        }
    }

    // Method to clear stored data locally
    private void clearLocalData() {
        SharedPreferences activity_preference = requireContext().getSharedPreferences(username + "ActivityLocalData", requireContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = activity_preference.edit();
        editor.clear();
        editor.apply();
    }

    // Spinner item selected listener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Retrieve selected values from spinners and date button
        if (parent.getId() == R.id.activityType) {
            if (activityType.getSelectedItem() != null) {
                storeType = activityType.getSelectedItem().toString();
            }
        } else if (parent.getId() == R.id.activityCrop) {
            if (activityCrop.getSelectedItem() != null) {
                storeCrop = activityCrop.getSelectedItem().toString();
            }
        }
    }

    // Spinner item non-selected listener
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * Methods for Date Button
     **/
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                storeDate = makeDateString(day, month, year);
                activityDate.setText(storeDate);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(requireContext(), style, dateSetListener, year, month, day);
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    // Format date string
    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    // Get month abbreviation
    private String getMonthFormat(int month) {
        String[] monthsArray = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        if (month >= 1 && month <= 12)
            return monthsArray[month - 1];
        else
            return "JAN";
    }
}
