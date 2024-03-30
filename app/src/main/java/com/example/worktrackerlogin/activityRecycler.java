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

public class activityRecycler extends Fragment {

    RecyclerView recyclerView;
    ArrayList<activityDataClass> list;
    DatabaseReference databaseReference;
    activitytrackAdapter adapter;

    // Specific User
    private String username;

    public activityRecycler() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_activities_recycler, container, false);

        recyclerView = view.findViewById(R.id.recycle_view);

        // Get the currently signed-in user's username from the arguments
        Bundle args = getArguments();
        if (args != null) {
            username = args.getString("username");
        }
        if (username != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(username).child("Activities");
        }

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new activitytrackAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    activityDataClass user = dataSnapshot.getValue(activityDataClass.class);
                    list.add(user);
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
