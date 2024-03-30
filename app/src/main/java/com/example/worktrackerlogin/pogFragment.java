package com.example.worktrackerlogin;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class pogFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    // Declare UI elements
    Spinner pogMonthSpin, pogTechnologySpin, pogBrandSpin;
    Button uploadButton, saveButton;
    EditText textyear, textcustomer, begInv, endInv;

    // Strings to store selected values
    String storeMonth = "";
    String storeTech = "";
    String storeBrand = "";

    public pogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pog, container, false);

        // Initialize UI elements
        pogMonthSpin = view.findViewById(R.id.pogMonth);
        pogTechnologySpin = view.findViewById(R.id.pogTechnology);
        pogBrandSpin = view.findViewById(R.id.pogBrand);
        textyear = view.findViewById(R.id.textyear);
        if (textyear != null) {
            textyear.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 4) {
                        textyear.setText(s.subSequence(0, 4));
                        textyear.setSelection(4);
                    }
                }
            });
        }
        textcustomer = view.findViewById(R.id.textcustomer);
        begInv = view.findViewById(R.id.begInv);
        endInv = view.findViewById(R.id.endInv);
        uploadButton = view.findViewById(R.id.pog_upload);
        saveButton = view.findViewById(R.id.pog_save);

        /*
         Spinners
         */
        // Spinner for MONTH
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireActivity(),
                R.array.Months,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pogMonthSpin.setAdapter(adapter);
        pogMonthSpin.setOnItemSelectedListener(this);

        // Spinner for TECHNOLOGY
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                requireActivity(),
                R.array.Technology,
                android.R.layout.simple_spinner_item
        );
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pogTechnologySpin.setAdapter(adapter1);
        pogTechnologySpin.setOnItemSelectedListener(this);

        // Spinner for BRAND
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                requireActivity(),
                R.array.Brands,
                android.R.layout.simple_spinner_item
        );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pogBrandSpin.setAdapter(adapter2);
        pogBrandSpin.setOnItemSelectedListener(this);

        // Load previously selected values
        loadPreviousSelections();

        // UPLOAD BUTTON
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int validationCode = isValidInput();
                if (validationCode == 0) {

                    String storeYear = textyear.getText().toString();
                    String storeCustomer = textcustomer.getText().toString();

                    storeDataFirebase(storeYear, storeCustomer);

                    clearLocalData();

                } else if (validationCode == 1) {
                    Toast.makeText(requireContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // SAVE BUTTON
        saveButton.setOnClickListener(view1 -> {

            String storeYear = textyear.getText().toString();
            String storeCustomer = textcustomer.getText().toString();
            String storeBINV = begInv.getText().toString();
            String storeEINV = endInv.getText().toString();

            // Storing data locally
            storeDataLocally(storeYear, storeCustomer, storeBINV, storeEINV);

            Toast.makeText(requireContext(), "Progress saved", Toast.LENGTH_SHORT).show();
        });
        return view;
    }

    /**
     * Methods for Spinners, Buttons, Storage Database
     **/
    // Method to check if all fields are in proper format
    private int isValidInput() {
        String storeYear = textyear.getText().toString().trim();
        String storeCustomer = textcustomer.getText().toString().trim();
        String storeBINV = begInv.getText().toString().trim();
        String storeEINV = endInv.getText().toString().trim();

        // Check if spinners are selected
        if (storeMonth.isEmpty() || storeTech.isEmpty() || storeBrand.isEmpty()) {
            return 1; // Indicates empty fields
        }

        // Check if EditText fields are empty
        if (storeYear.isEmpty() || storeCustomer.isEmpty() || storeBINV.isEmpty() || storeEINV.isEmpty()) {
            return 1; // Indicates empty fields
        }

        return 0; // Indicates no error
    }

    // Store data to Firebase
    private void storeDataFirebase(String storeYear, String storeCustomer) {
        String begInvStr = begInv.getText().toString().trim();
        String endInvStr = endInv.getText().toString().trim();

        int begInvUnits = Integer.parseInt(begInvStr);
        int endInvUnits = Integer.parseInt(endInvStr);

        double begInvValue = Double.parseDouble(begInvStr);
        double endInvValue = Double.parseDouble(endInvStr);

        String brandSpinnerValue = pogBrandSpin.getSelectedItem().toString().toLowerCase(); // or toUpperCase()

        DatabaseReference pogValuesRef = FirebaseDatabase.getInstance("https://scl-filipinas-work-tracker-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("POG-Values");

        pogValuesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean brandFound = false;
                for (DataSnapshot brandSnapshot : dataSnapshot.getChildren()) {
                    String brandName = brandSnapshot.getKey().toLowerCase();
                    if (brandName.equalsIgnoreCase(brandSpinnerValue)) {
                        brandFound = true;
                        double kgs = brandSnapshot.child("kg").getValue(Double.class);
                        double priceUnit = brandSnapshot.child("price-unit").getValue(Double.class);
                        double wv = brandSnapshot.child("wv").getValue(Double.class);
                        double ctn = brandSnapshot.child("ctn").getValue(Double.class);

                        // Perform calculations
                        double pogUnits = begInvValue - endInvValue;

                        double begInvKgs = Double.parseDouble(String.format("%.9f", begInvValue * kgs * wv));
                        double endInvKgs = Double.parseDouble(String.format("%.9f", endInvValue * kgs * wv));
                        double pogKgs = Double.parseDouble(String.format("%.9f", begInvKgs - endInvKgs));

                        double begInvCtn = Double.parseDouble(String.format("%.9f", begInvValue / ctn));
                        double endInvCtn = Double.parseDouble(String.format("%.9f", endInvValue / ctn));
                        double pogCtn = Double.parseDouble(String.format("%.9f", begInvCtn - endInvCtn));

                        double begInvVal = Double.parseDouble(String.format("%.9f", begInvValue * priceUnit));
                        double endInvVal = Double.parseDouble(String.format("%.9f", endInvValue * priceUnit));
                        double pogVal = Double.parseDouble(String.format("%.9f", begInvVal - endInvVal));


                        DatabaseReference pogRef = FirebaseDatabase.getInstance("https://scl-filipinas-work-tracker-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                .getReference("POG");

                        pogDataClass pogdataclass = new pogDataClass(storeYear, storeMonth, storeTech, brandName, storeCustomer, begInvUnits, endInvUnits, pogUnits, begInvKgs, endInvKgs, pogKgs, begInvCtn, endInvCtn, pogCtn, begInvVal, endInvVal, pogVal);

                        pogRef.push().setValue(pogdataclass);

                        // Clear Spinners
                        pogMonthSpin.setSelection(0);
                        pogTechnologySpin.setSelection(0);
                        pogBrandSpin.setSelection(0);

                        // Clear EditText fields
                        textyear.setText("");
                        textcustomer.setText("");
                        begInv.setText("");
                        endInv.setText("");

                        Toast.makeText(requireContext(), "Data uploaded", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if (!brandFound) {
                    Toast.makeText(requireContext(), "Data not found for selected brand", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Toast.makeText(requireContext(), "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Store data locally
    private void storeDataLocally(String storeYear, String storeCustomer, String storeBINV, String storeEINV) {

        SharedPreferences pog_preferences = requireActivity().getSharedPreferences("POGLocalData", requireActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pog_preferences.edit();

        editor.putString("storedYear", storeYear);
        editor.putString("storedMonth", storeMonth);
        editor.putString("storedTech", storeTech);
        editor.putString("storedBrand", storeBrand);
        editor.putString("storedCustomer", storeCustomer);
        editor.putString("storedBINV", storeBINV);
        editor.putString("storedEINV", storeEINV);
        editor.apply();
    }

    // Load previously selected values
    private void loadPreviousSelections() {

        SharedPreferences pog_preferences = requireActivity().getSharedPreferences("POGLocalData", requireActivity().MODE_PRIVATE);

        String storedYear = pog_preferences.getString("storedYear", "");
        storeMonth = pog_preferences.getString("storedMonth", "");
        storeTech = pog_preferences.getString("storedTech", "");
        storeBrand = pog_preferences.getString("storedBrand", "");
        String storedCustomer = pog_preferences.getString("storedCustomer", "");
        String storedBINV = pog_preferences.getString("storedBINV", "");
        String storedEINV = pog_preferences.getString("storedEINV", "");

        // Set the selections in spinners
        if (!storeMonth.isEmpty()) {
            int productPosition = ((ArrayAdapter<String>) pogMonthSpin.getAdapter()).getPosition(storeMonth);
            pogMonthSpin.setSelection(productPosition);
        }
        if (!storeTech.isEmpty()) {
            int customerPosition = ((ArrayAdapter<String>) pogTechnologySpin.getAdapter()).getPosition(storeTech);
            pogTechnologySpin.setSelection(customerPosition);
        }
        if (!storeBrand.isEmpty()) {
            int customerPosition = ((ArrayAdapter<String>) pogBrandSpin.getAdapter()).getPosition(storeBrand);
            pogBrandSpin.setSelection(customerPosition);
        }

        // Set the stored values for EditText fields
        if (!storedYear.isEmpty()) {
            textyear.setText(storedYear);
        }
        if (!storedCustomer.isEmpty()) {
            textcustomer.setText(storedCustomer);
        }
        if (!storedBINV.isEmpty()) {
            begInv.setText(storedBINV);
        }
        if (!storedEINV.isEmpty()) {
            endInv.setText(storedEINV);
        }
    }

    // Method to clear stored data locally
    private void clearLocalData() {
        SharedPreferences pog_preferences = requireActivity().getSharedPreferences("POGLocalData", requireActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pog_preferences.edit();
        editor.clear();
        editor.apply();
    }

    // Spinner item selected listener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Retrieve selected values from spinners
        if (parent.getId() == R.id.pogMonth) {
            if (pogMonthSpin.getSelectedItem() != null) {
                storeMonth = pogMonthSpin.getSelectedItem().toString();
            }
        } else if (parent.getId() == R.id.pogTechnology) {
            if (pogTechnologySpin.getSelectedItem() != null) {
                storeTech = pogTechnologySpin.getSelectedItem().toString();
            }
        } else if (parent.getId() == R.id.pogBrand) {
            if (pogBrandSpin.getSelectedItem() != null) {
                storeBrand = pogBrandSpin.getSelectedItem().toString();
            }
        }
    }

    // Spinner item non-selected listener
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
