package com.example.worktrackerlogin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class salesRecyclerFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<salesDataClass> list;
    DatabaseReference databaseReference;
    salesAdapter adapter;
    String username = "";

    public salesRecyclerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales_recycler, container, false);

        recyclerView = view.findViewById(R.id.recycle_view);
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new salesAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);
        username = getArguments().getString("username");
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(username).child("sales");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    salesDataClass sale = dataSnapshot.getValue(salesDataClass.class);
                    list.add(sale);
                }
                // Reverse the list to display most recent data first
                Collections.reverse(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        return view;
    }
}
