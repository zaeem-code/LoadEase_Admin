package com.loadease.uberclone.adminpanels.Model;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loadease.uberclone.adminpanels.R;

import java.util.ArrayList;

public class DiscountnfoRCAdopter extends  RecyclerView.Adapter<DiscountnfoRCAdopter.ViewHolder> {
int i=0;
    private Context context;

    private ArrayList<Discountmodal> disModelList;
    Discountmodal item;
    public DiscountnfoRCAdopter(ArrayList<Discountmodal> disModelList, Context activity) {
        this.disModelList = disModelList;
        this.context=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View carditemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_detail_discount,parent,false);
        return new ViewHolder(carditemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        item= disModelList.get(position);
        if (item!=null){
holder.name.setText(item.getDisname());
            holder.msg.setText(item.getDiscountdetaials());
            holder.validation.setText("Valid till: "+item.getDiscvalidationdate());
            holder.Code.setText(item.getDiscode());
            holder.percentage.setText(item.getDiscdercentage()+" rs");
        }




    }

    @Override
    public int getItemCount() {
        return disModelList.size();
    }
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, validation,Code,percentage,msg;
        Switch buttonApproved;

        ViewHolder(View itemView) {
            super(itemView);

            itemView.findViewById(R.id.delete).setOnClickListener(this);
           name= itemView.findViewById(R.id.name);
            msg= itemView.findViewById(R.id.msg);
            Code= itemView.findViewById(R.id.Code);

            validation= itemView.findViewById(R.id.validation);
            percentage= itemView.findViewById(R.id.percentage);

        }




        @Override
        public void onClick(View view) {
            item= disModelList.get(getAdapterPosition());
            setdriverVerificaion(item);
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
            disModelList.remove(getAdapterPosition());
            notifyDataSetChanged();

        }
    }


private void setdriverVerificaion(Discountmodal item){
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef =  database.getReference("PromotionalDetails");

    try {
        myRef.child(item.getDisname()).removeValue();

    }catch (Exception e){
        Toast.makeText(context, "Failed to delete try it using Firebase Admin panel", Toast.LENGTH_SHORT).show();
    }
}
}
