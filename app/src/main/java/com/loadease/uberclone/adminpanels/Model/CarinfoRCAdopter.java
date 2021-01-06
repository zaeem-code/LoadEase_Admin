package com.loadease.uberclone.adminpanels.Model;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loadease.uberclone.adminpanels.Activities.DriverDetailActivity;
import com.loadease.uberclone.adminpanels.Messages.FCM_send_msg;
import com.loadease.uberclone.adminpanels.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CarinfoRCAdopter extends  RecyclerView.Adapter<CarinfoRCAdopter.ViewHolder> {
int i=0;
    private Context context;

    private ArrayList<DriverUser> cartitemModelList;
    DriverUser item;
    public CarinfoRCAdopter(ArrayList<DriverUser> cartitemModelList, Context activity) {
        this.cartitemModelList = cartitemModelList;
        this.context=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View carditemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_detail_car,parent,false);
        return new ViewHolder(carditemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        item=cartitemModelList.get(position);
        if (item!=null){



            Picasso.get().load(item.getRider_pic_Url()).into(holder.DriverPic);
            holder.name.setText(item.getName());
            holder.id.setText(item.getCarnum());
            holder.Ridetype.setText(item.getCarType());
            if (item.getBlocked().equals("true")){

                holder.stts.setText("Blocked");
            }else{
                holder.stts.setText(item.getProfile_status());
            }
           Log.v("noti","ye size ha "+cartitemModelList.size());

        }

    }

    @Override
    public int getItemCount() {
        return cartitemModelList.size();
    }
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView DriverPic;
        TextView name, id,stts,Ridetype;

        ViewHolder(View itemView) {
            super(itemView);
            DriverPic = itemView.findViewById(R.id.pic_img);
            name = itemView.findViewById(R.id.name);
            Ridetype = itemView.findViewById(R.id.Ridetype);
            stts = itemView.findViewById(R.id.stts);
            id = itemView.findViewById(R.id.id);
            itemView.findViewById(R.id.lytx).setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view) {
            item=cartitemModelList.get(getAdapterPosition());

                    context.startActivity(new Intent(context, DriverDetailActivity.class)

                    .putExtra("Name",item.getName())
                                    .putExtra("UID",item.getId())
                                    .putExtra("Name",item.getName())
                                    .putExtra("Email",item.getEmail())
                                    .putExtra("Phone",item.getPhone())
                                    .putExtra("DOB",item.getDOB())
                                    .putExtra("Status",item.getProfile_status())
                                    .putExtra("Blocked",item.getBlocked())
                                    .putExtra("carType",item.getCarType())
                                    .putExtra("carnum",item.getCarnum())
                                    .putExtra("gen",item.getGender())
                                    .putExtra("Yop",item.getYear_of_prodution())
                                    .putExtra("CNICURL",item.getRider_cnic_pic_url())
                                    .putExtra("LICURL",item.getRider_licence_pic())
                                    .putExtra("VEHURL",item.getRider_vehical_pic())
                                    .putExtra("DPURL",item.getRider_pic_Url())
                                    .putExtra("getAverageRating",item.getAverageRating())
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                    );
                  }


    }

}
