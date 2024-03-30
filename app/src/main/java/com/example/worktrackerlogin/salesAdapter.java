package com.example.worktrackerlogin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class salesAdapter extends RecyclerView.Adapter<salesAdapter.MyViewHolder> {
    Context context;
    ArrayList<salesDataClass> list;

    public salesAdapter(Context context, ArrayList<salesDataClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.fragment_sales_cardview,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        salesDataClass data = list.get(position);

        holder.date.setText(data.getSalesDate());
        holder.name.setText(data.getCustomerName());
        holder.type.setText(data.getCustomerType());
        holder.product.setText(data.getProduct());
        holder.units.setText(String.valueOf(data.getUnit()));
        holder.price.setText("Php " + String.valueOf(data.getPrice()));
        holder.value.setText("Php " + String.valueOf(data.getValue()));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date, type, name, product, units, price, value;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.textdate);
            name = itemView.findViewById(R.id.textname);
            type = itemView.findViewById(R.id.texttype);
            product = itemView.findViewById(R.id.textproduct);
            units = itemView.findViewById(R.id.textunit);
            price = itemView.findViewById(R.id.textprice);
            value = itemView.findViewById(R.id.textvalue);

        }
    }
}
