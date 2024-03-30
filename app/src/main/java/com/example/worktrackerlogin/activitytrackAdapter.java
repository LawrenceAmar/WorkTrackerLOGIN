package com.example.worktrackerlogin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class activitytrackAdapter extends RecyclerView.Adapter<activitytrackAdapter.MyViewHolder> {
    Context context;
    ArrayList<activityDataClass> list;

    public activitytrackAdapter(Context context, ArrayList<activityDataClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.fragment_activities_cardview,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        activityDataClass data = list.get(position);

        holder.date.setText(data.getActDate());
        holder.name.setText(data.getActName());
        holder.type.setText(data.getActType());
        holder.crop.setText(data.getActCrop());
        holder.reach.setText(String.valueOf(data.getActReach()));
        holder.person.setText(data.getActContactPerson());
        holder.number.setText(data.getActNumber());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date, type, name, crop, reach, person, number;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.datetext);
            name = itemView.findViewById(R.id.nametext);
            type = itemView.findViewById(R.id.typetext);
            crop = itemView.findViewById(R.id.croptext);
            reach = itemView.findViewById(R.id.reachtext);
            person = itemView.findViewById(R.id.contacttext);
            number = itemView.findViewById(R.id.numbertext);
        }
    }
}
