package com.loadease.uberclone.adminpanels.Model;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loadease.uberclone.adminpanels.Common.Common;
import com.loadease.uberclone.adminpanels.Messages.FCM_send_msg;
import com.loadease.uberclone.adminpanels.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DriverinfoRCAdopterforDiscounttospecific extends  RecyclerView.Adapter<DriverinfoRCAdopterforDiscounttospecific.ViewHolder> {
int i=0;
    private Context context;

    Discountmodal itemfordis;
    private ArrayList<PassengerUser> usersarray;
    PassengerUser item;
    public DriverinfoRCAdopterforDiscounttospecific(ArrayList<PassengerUser> usersarray, Context activity) {
        this.usersarray = usersarray;
        this.context=activity;

        itemfordis= Common.itemforspecificusers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View carditemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_detail_passenger,parent,false);
        return new ViewHolder(carditemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        item= usersarray.get(position);
        if (item!=null){

            Picasso.get().load(item.getAvatarUrl()).into(holder.DriverPic);
            holder.name.setText(item.getName());


            }




    }

    @Override
    public int getItemCount() {
        return usersarray.size();
    }
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView DriverPic;
        TextView name, btn;
        Switch buttonApproved;

        ViewHolder(View itemView) {
            super(itemView);
            DriverPic = itemView.findViewById(R.id.pic_img);


            name = itemView.findViewById(R.id.name);

            btn = itemView.findViewById(R.id.send);
            btn.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            item= usersarray.get(getAdapterPosition());
         if (  btn.getText().toString().trim().equals("Send") ){

             promotionsSend(item,btn);

         }     else {
             Toast.makeText(context, "Already assigned", Toast.LENGTH_SHORT).show();
         }
                  }


    }

    private void promotionsSend(PassengerUser item, TextView btn){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef =  database.getReference("PromotionalDetailsSPECIFIC").child(item.getId());

        try {
            myRef.child(itemfordis.getDisname()).setValue(itemfordis).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(context, "Assigned", Toast.LENGTH_SHORT).show();

                   new FCM_send_msg(context,item.getId(),itemfordis.discountdetaials, item.getId()+"Dis");
                    btn.setText("Assigned");

                    notifyDataSetChanged();
                }
            });

        }catch (Exception e){
            Toast.makeText(context, "Failed to send, try it using Firebase Admin panel", Toast.LENGTH_SHORT).show();
        }
    }
}
