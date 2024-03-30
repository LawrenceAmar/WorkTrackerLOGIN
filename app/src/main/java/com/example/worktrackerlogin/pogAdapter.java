package com.example.worktrackerlogin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class pogAdapter extends RecyclerView.Adapter<pogAdapter.MyViewHolder> {
    Context context;
    ArrayList<pogDataClass> list;

    public pogAdapter(Context context, ArrayList<pogDataClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.fragment_pog_cardview,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        pogDataClass data = list.get(position);

        holder.year.setText(data.getYear());
        holder.month.setText(data.getMonth());
        holder.technology.setText(data.getTech());
        holder.brand.setText(data.getBrand());
        holder.customerName.setText(data.getCustomer());

        holder.UnitBINV.setText(String.valueOf(data.getBegInv()));
        holder.UnitEINV.setText(String.valueOf(data.getEndInv()));
        holder.UnitPOG.setText(String.valueOf(data.getPogUnits()));

        holder.kgBINV.setText(String.valueOf(data.getBegInvKgs()));
        holder.kgEINV.setText(String.valueOf(data.getEndInvKgs()));
        holder.kgPOG.setText(String.valueOf(data.getPogKgs()));

        holder.ctnBINV.setText(String.valueOf(data.getBegInvCtn()));
        holder.ctnEINV.setText(String.valueOf(data.getEndInvCtn()));
        holder.ctnPOG.setText(String.valueOf(data.getPogCtn()));


        holder.valBINV.setText(String.valueOf(data.getBegInvVal()));
        holder.valEINV.setText(String.valueOf(data.getEndInvVal()));
        holder.valPOG.setText(String.valueOf(data.getPogVal()));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView year, month, technology, brand, customerName;
        TextView UnitBINV, UnitEINV, UnitPOG;
        TextView kgBINV, kgEINV, kgPOG;
        TextView ctnBINV, ctnEINV, ctnPOG;
        TextView valBINV, valEINV, valPOG;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            year = itemView.findViewById(R.id.textyear);
            month = itemView.findViewById(R.id.textmonth);
            technology = itemView.findViewById(R.id.texttechnology);
            brand = itemView.findViewById(R.id.textbrand);
            customerName = itemView.findViewById(R.id.textcustomer);


            UnitBINV = itemView.findViewById(R.id.textUnitBINV);
            UnitEINV = itemView.findViewById(R.id.textUnitEINV);
            UnitPOG = itemView.findViewById(R.id.textUnitPOG);

            kgBINV = itemView.findViewById(R.id.textKgBINV);
            kgEINV = itemView.findViewById(R.id.textKgEINV);
            kgPOG = itemView.findViewById(R.id.textKgPOG);

            ctnBINV = itemView.findViewById(R.id.textCtnBINV);
            ctnEINV = itemView.findViewById(R.id.textCtnEINV);
            ctnPOG = itemView.findViewById(R.id.textCtnPOG);

            valBINV = itemView.findViewById(R.id.textValBINV);
            valEINV = itemView.findViewById(R.id.textValEINV);
            valPOG = itemView.findViewById(R.id.textValPOG);
        }
    }
}
