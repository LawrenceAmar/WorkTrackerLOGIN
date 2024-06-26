package com.example.worktrackerlogin.lists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.worktrackerlogin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class listProductFragment extends Fragment {

    private TextView status;
    private ListView productList;

    public listProductFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_product, container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://scl-filipinas-work-tracker-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference productListRef = database.getReference("ProductList");

        productList = view.findViewById(R.id.plListView);
        status = view.findViewById(R.id.status);

        fetchAndDisplayData(productListRef);

        return view;
    }

    private void fetchAndDisplayData(DatabaseReference ref) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> values = new ArrayList<>();
                retrieveData(dataSnapshot, values);

                // Update ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_list_item_1, values);
                productList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                status.setText("Failed to load data");
            }
        });
    }

    private void retrieveData(DataSnapshot dataSnapshot, ArrayList<String> values) {
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            if (snapshot.getValue() instanceof String) {
                // If the snapshot value is a string, add it to the list
                values.add(snapshot.getValue(String.class));
            } else {
                // If the snapshot value is not a string, recursively traverse its children
                retrieveData(snapshot, values);
            }
        }
    }
}
