package com.example.worktrackerlogin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.worktrackerlogin.profile.HelperClass;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<HelperClass> userList; // Change User to HelperClass

    public void setUserList(List<HelperClass> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        HelperClass user = userList.get(position); // Change User to HelperClass
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView userNameTextView;
        private Button button1;
        private Button button2;
        private Button button3;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userTextView);
            button1 = itemView.findViewById(R.id.pogButton);
            button2 = itemView.findViewById(R.id.salesButton);
            button3 = itemView.findViewById(R.id.activitiesButton);
        }

        public void bind(HelperClass user) { // Change User to HelperClass
            userNameTextView.setText(user.getName());
            // Set onClickListeners for buttons
            // Handle button click actions
        }
    }
}
