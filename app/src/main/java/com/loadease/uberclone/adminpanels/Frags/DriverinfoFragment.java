package com.loadease.uberclone.adminpanels.Frags;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.loadease.uberclone.adminpanels.Activities.Home;
import com.loadease.uberclone.adminpanels.Model.DriverUser;
import com.loadease.uberclone.adminpanels.Model.DriverinfoRCAdopter;
import com.loadease.uberclone.adminpanels.R;

import java.util.ArrayList;
public class DriverinfoFragment extends Fragment {
RecyclerView driver_rcy;
    ProgressDialog dialog;
    int blocked=0;
    int approved=0;
    int pending=0;
    TextView blockedTV,ApprovedTv,PendingTV;



        DriverinfoRCAdopter driverinfoRCAdopter;
    ArrayList<DriverUser> driverinfo=new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public DriverinfoFragment(){

    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.drivers_details_activity, container, false);


    SuscribingTOfcm();
    driver_rcy=root.findViewById(R.id.driver_rcy);
    blockedTV=root.findViewById(R.id.blocked);
    ApprovedTv=root.findViewById(R.id.approved);
    PendingTV=root.findViewById(R.id.pending);

        return root;
}

    @Override
    public void onResume() {
        super.onResume();
        getuserDAta();
    }

    private void setuprc(){
        blockedTV.setText(String.valueOf(blocked));
        ApprovedTv.setText(String.valueOf(approved));
        PendingTV.setText(String.valueOf(pending));

        driver_rcy.setLayoutManager(new LinearLayoutManager(getActivity()));
        driverinfoRCAdopter=new DriverinfoRCAdopter(driverinfo,getActivity());

        driver_rcy.setAdapter(driverinfoRCAdopter);
        driverinfoRCAdopter.notifyDataSetChanged();
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
    private void getuserDAta(){
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

         blocked=0;
         approved=0;
         pending=0;
        driverinfo.clear();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("RidersProfile");
myRef.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
         if (snapshot.exists()) {
            for (DataSnapshot dsp : snapshot.getChildren()) {
                Log.v("Admin", "------> " + dsp);


                if (dsp.child("blocked").getValue().toString().trim().equals("true")) {
                    Log.v("testinegX","blocked");
                    blocked++;
                } else if (dsp.child("profile_status").getValue().toString().trim().equals("verified")) {

                    Log.v("testinegX","verified");
                    approved++;
                } else {
                    pending++;

                    Log.v("testinegX","Pending");
                }
                driverinfo.add(dsp.getValue(DriverUser.class));
//                Log.v("testingX",blocked+"; verfi"+approved+"; pending"+pending);
            }
        }

            setuprc();


    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
getuserDAta();
    }
});
    }



    private void SuscribingTOfcm(){




        FirebaseMessaging.getInstance().subscribeToTopic("Admin");
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( getActivity(),  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult)
            {
//            String newToken = instanceIdResult.getToken();

            Toast.makeText(getActivity(),"Success",Toast.LENGTH_LONG).show();

                FirebaseMessaging.getInstance().subscribeToTopic("Admin");
            }
        });


    }
}