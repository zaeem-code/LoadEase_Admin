package com.loadease.uberclone.adminpanels.Model;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loadease.uberclone.adminpanels.Activities.DriverDetailActivity;
import com.loadease.uberclone.adminpanels.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DriverinfoRCAdopter extends  RecyclerView.Adapter<DriverinfoRCAdopter.ViewHolder> {

    private Context context;

    private ArrayList<DriverUser> cartitemModelList;
    DriverUser item;
    public DriverinfoRCAdopter(ArrayList<DriverUser> cartitemModelList, Context activity) {
        this.cartitemModelList = cartitemModelList;
        this.context=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View carditemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_detail_rider,parent,false);
        return new ViewHolder(carditemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        item=cartitemModelList.get(position);
        if (item!=null){

            if (item.getProfile_status().equals("Nverified")) {
holder.buttonApproved.setChecked(false);

            }   else if (item.getProfile_status().equals("verified")){

                holder.buttonApproved.setChecked(true);

            }
            if (item.getBlocked().equals("true")){
                holder.buttonApproved.setVisibility(View.GONE);
                holder.stts.setVisibility(View.VISIBLE);


            }else {
                holder.buttonApproved.setVisibility(View.VISIBLE);
                holder.stts.setVisibility(View.GONE);

            }

            Picasso.get().load(item.getRider_pic_Url()).into(holder.DriverPic);
            holder.name.setText(item.getName());
        }

    }

    @Override
    public int getItemCount() {
        return cartitemModelList.size();
    }
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        ImageView DriverPic;
        TextView name, RideLineces,Documentspic,stts;
        Switch buttonApproved;

        ViewHolder(View itemView) {
            super(itemView);  DriverPic = itemView.findViewById(R.id.pic_img);
            DriverPic.setOnClickListener(this);


            name = itemView.findViewById(R.id.name);

            stts = itemView.findViewById(R.id.stts);

            RideLineces = itemView.findViewById(R.id.ride_licencetv);
            RideLineces.setOnClickListener(this);
            Documentspic = itemView.findViewById(R.id.doc_cartv);
            Documentspic.setOnClickListener(this);
            buttonApproved = itemView.findViewById(R.id.approved);
            buttonApproved.setOnCheckedChangeListener(this);
            buttonApproved.setTextOff("Pending");
            buttonApproved.setTextOn("Approved");
        }

        @Override
        public void onClick(View view) {
            item=cartitemModelList.get(getAdapterPosition());
            switch (view.getId()){
                case R.id.ride_licencetv:
                    case R.id.doc_cartv:
                case R.id.pic_img:
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
                    break;

            }
                  }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            item=cartitemModelList.get(getAdapterPosition());

            Toast.makeText(context, "switch :"+item.getName(), Toast.LENGTH_SHORT).show();
                setdriverVerificaion(item.getId(),b);


        }
    }

private void setdriverVerificaion(String id, boolean b){
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("RidersProfile").child(id);
    if (b){
        myRef.child("profile_status").setValue("verified");

    }else {

        myRef.child("profile_status").setValue("Nverified");
    }
}
}
