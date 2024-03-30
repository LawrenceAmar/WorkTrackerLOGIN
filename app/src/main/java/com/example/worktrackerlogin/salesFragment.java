package com.example.worktrackerlogin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class salesFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    // Declare UI elements
    Spinner customerSpin, productSpin;
    Button uploadButton, saveButton, dateButton;
    EditText priceText, valueText, unitText, customerNameText;

    // Strings to store selected values
    String storeDate = "";
    String storeProduct = "";
    String storeCustomer = "";
    String username = "";

    // For date picker
    private DatePickerDialog datePickerDialog;

    public salesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sales_activity, container, false);

        // Initialize UI elements
        priceText = view.findViewById(R.id.sales_price);
        valueText = view.findViewById(R.id.sales_value);
        unitText = view.findViewById(R.id.sales_unit);
        customerNameText = view.findViewById(R.id.sales_customerName);
        productSpin = view.findViewById(R.id.sales_product);
        customerSpin = view.findViewById(R.id.sales_customer);
        dateButton = view.findViewById(R.id.sales_date);
        uploadButton = view.findViewById(R.id.sales_upload);
        saveButton = view.findViewById(R.id.sales_save);

        dateButton.setText("");
        initDatePicker();

        username = getArguments().getString("username");
        /*
         Spinners
         */
        // Spinner for Customer
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.Customer,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customerSpin.setAdapter(adapter);
        customerSpin.setOnItemSelectedListener(this);

        // Spinner for Product
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.Products,
                android.R.layout.simple_spinner_item
        );
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productSpin.setAdapter(adapter1);
        productSpin.setOnItemSelectedListener(this);

        // Add TextWatcher to unitText
        unitText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateValue();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Add TextWatcher to priceText
        priceText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateValue();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Load previously selected values
        loadPreviousSelections();

        // UPLOAD BUTTON
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int validationCode = isValidInput();
                if (validationCode == 0) {

                    String storeUnit = unitText.getText().toString();
                    String storePrice = priceText.getText().toString();
                    String storeValue = valueText.getText().toString();
                    String customerName = customerNameText.getText().toString();

                    // Storing data to Firebase
                    storeDataToFirebase(storeUnit, storePrice, storeValue, customerName);

                    // Clear stored data locally
                    clearLocalData();

                    // Clear Spinners
                    dateButton.setText("");
                    productSpin.setSelection(0);
                    customerSpin.setSelection(0);

                    // Clear EditText fields
                    priceText.setText("");
                    valueText.setText("");
                    unitText.setText("");
                    customerNameText.setText("");


                } else if (validationCode == 1) {
                    Toast.makeText(requireContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                } else if (validationCode == 2) {
                    Toast.makeText(requireContext(), "Please input a number for price and value.", Toast.LENGTH_SHORT).show();
                } else if (validationCode == 3) {
                    Toast.makeText(requireContext(), "Price must be a numerical value above 0.", Toast.LENGTH_SHORT).show();
                } else if (validationCode == 4) {
                    Toast.makeText(requireContext(), "Value must be a numerical value above 0.", Toast.LENGTH_SHORT).show();
                } else if (validationCode == 5) {
                    Toast.makeText(requireContext(), "Units must be a numerical value above 0.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // SAVE BUTTON
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String storeUnit = unitText.getText().toString();
                String storePrice = priceText.getText().toString();
                String storeValue = valueText.getText().toString();
                String customerName = customerNameText.getText().toString();

                // Storing data locally
                storeDataLocally(storeUnit, storePrice, storeValue, customerName);

                Toast.makeText(requireContext(), "Progress saved", Toast.LENGTH_SHORT).show();
            }
        });

        // For Date Button
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(v); // Pass the clicked View as an argument
            }
        });

        return view;
    }

    // Method to check if all fields are in proper format
    private int isValidInput() {
        String unit = unitText.getText().toString().trim();
        String price = priceText.getText().toString().trim();
        String value = valueText.getText().toString().trim();
        String customerName = customerNameText.getText().toString().trim();

        // Check if spinners are selected
        if (storeDate.isEmpty() || storeCustomer.isEmpty() || storeProduct.isEmpty()) {
            return 1; // Indicates empty fields
        }

        // Check if EditText fields are empty
        if (unit.isEmpty() || price.isEmpty() || value.isEmpty() || customerName.isEmpty()) {
            return 1; // Indicates empty fields
        }

        // Check if price, value, and unit are numerical and above 0
        try {
            double priceValue = Double.parseDouble(price);
            double valueValue = Double.parseDouble(value);
            double unitValue = Double.parseDouble(unit);

            // Additional check for values
            if (priceValue <= 0 || valueValue <= 0 || unitValue <= 0) {
                if (unitValue <= 0) {
                    return 5; // Indicates units <= 0
                }
                if (priceValue <= 0) {
                    return 3; // Indicates price <= 0
                }
                if (valueValue <= 0) {
                    return 4; // Indicates value <= 0
                }
            }
        } catch (NumberFormatException e) {
            return 2; // Indicates non-numerical values
        }
        return 0; // Indicates no error
    }

    // Store data to Firebase
    private void storeDataToFirebase(String storeUnit, String storePrice, String storeValue, String customerName) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://scl-filipinas-work-tracker-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("users");

        int unit = Integer.parseInt(storeUnit);
        double price = Double.parseDouble(storePrice);
        double value = Double.parseDouble(storeValue);

        salesDataClass salesdataclass = new salesDataClass(storeDate, storeCustomer, storeProduct, unit, price, value, customerName);

        myRef.child(username).child("sales").push().setValue(salesdataclass);

        Toast.makeText(requireContext(), "Data uploaded", Toast.LENGTH_SHORT).show();
    }

    // Store data locally
    private void storeDataLocally(String storeUnit, String storePrice, String storeValue, String customerName) {

        SharedPreferences sales_preferences = requireActivity().getSharedPreferences("SalesLocalData", requireActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sales_preferences.edit();

        editor.putString("storedDate", storeDate);
        editor.putString("storedProduct", storeProduct);
        editor.putString("storedCustomer", storeCustomer);
        editor.putString("storedUnit", storeUnit);
        editor.putString("storedPrice", storePrice);
        editor.putString("storedValue", storeValue);
        editor.putString("storedCustomerName", customerName);
        editor.apply();
    }

    // Load previously selected values
    private void loadPreviousSelections() {

        SharedPreferences sales_preferences = requireActivity().getSharedPreferences("SalesLocalData", requireActivity().MODE_PRIVATE);

        storeDate = sales_preferences.getString("storedDate", "");
        storeProduct = sales_preferences.getString("storedProduct", "");
        storeCustomer = sales_preferences.getString("storedCustomer", "");
        String storedUnit = sales_preferences.getString("storedUnit", "");
        String storedPrice = sales_preferences.getString("storedPrice", "");
        String storedValue = sales_preferences.getString("storedValue", "");
        String storedCustomerName = sales_preferences.getString("storedCustomerName", "");

        // Set the selections in spinners and date button
        if (!storeDate.isEmpty()) {
            dateButton.setText(storeDate);
        }
        if (!storeProduct.isEmpty()) {
            int productPosition = ((ArrayAdapter<String>) productSpin.getAdapter()).getPosition(storeProduct);
            productSpin.setSelection(productPosition);
        }
        if (!storeCustomer.isEmpty()) {
            int customerPosition = ((ArrayAdapter<String>) customerSpin.getAdapter()).getPosition(storeCustomer);
            customerSpin.setSelection(customerPosition);
        }

        // Set the stored values for customer name, unit, price, and value to their respective EditText fields
        if (!storedUnit.isEmpty()) {
            unitText.setText(storedUnit);
        }
        if (!storedPrice.isEmpty()) {
            priceText.setText(storedPrice);
        }
        if (!storedValue.isEmpty()) {
            valueText.setText(storedValue);
        }
        if (!storedCustomerName.isEmpty()) {
            customerNameText.setText(storedCustomerName);
        }
    }

    // Method to clear stored data locally
    private void clearLocalData() {
        SharedPreferences sales_preferences = requireActivity().getSharedPreferences("SalesLocalData", requireActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sales_preferences.edit();
        editor.clear();
        editor.apply();
    }

    // Spinner item selected listener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Retrieve selected values from spinners and date button
        if (parent.getId() == R.id.sales_customer) {
            if (customerSpin.getSelectedItem() != null) {
                storeCustomer = customerSpin.getSelectedItem().toString();
            }
        } else if (parent.getId() == R.id.sales_product) {
            if (productSpin.getSelectedItem() != null) {
                storeProduct = productSpin.getSelectedItem().toString();
            }
        }
        calculateValue();
    }

    // Spinner item non-selected listener
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void calculateValue() {

        String unitString = unitText.getText().toString();
        String priceString = priceText.getText().toString();

        if (!unitString.isEmpty() && !priceString.isEmpty()) {
            double unit = Double.parseDouble(unitString);
            double price = Double.parseDouble(priceString);

            double value = unit * price;

            valueText.setText(String.format("%.2f", value));
        } else {
            valueText.setText("0.00");
        }
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
                dateButton.setText(storeDate);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(requireContext(), style, dateSetListener, year, month, day); // Corrected instantiation
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
