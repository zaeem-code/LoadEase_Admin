package com.loadease.uberclone.adminpanels.Frags;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loadease.uberclone.adminpanels.Model.DriverUser;
import com.loadease.uberclone.adminpanels.Model.CarinfoRCAdopter;
import com.loadease.uberclone.adminpanels.R;

import java.util.ArrayList;

public class VehicalmanaggmentFragment extends Fragment {
    RecyclerView driver_rcy;
    com.budiyev.android.circularprogressbar.CircularProgressBar prgblock, prgonline, prgoffline;
    TextView tvblocked, tvonlineper, tvofflineper, inprocesxs, completed, cancelled, totaltv;
    ProgressDialog dialog;

    CarinfoRCAdopter carinfoRCAdopter;
    ArrayList<DriverUser> driverinfo = new ArrayList<>();

    public VehicalmanaggmentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_vehicalmanagment, container, false);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");


        prgoffline = root.findViewById(R.id.prgoffline);
        prgonline = root.findViewById(R.id.prgonline);
        prgblock = root.findViewById(R.id.prgblock);

        driver_rcy = root.findViewById(R.id.driver_rcy);

        tvblocked = root.findViewById(R.id.blockedper);
        tvofflineper = root.findViewById(R.id.offlineper);
        tvonlineper = root.findViewById(R.id.onlineper);

        inprocesxs = root.findViewById(R.id.unverifiedtxt);
        completed = root.findViewById(R.id.textcomp);
        cancelled = root.findViewById(R.id.canceltxt);
        totaltv = root.findViewById(R.id.total);


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        VehicalVerificationchk();
    }


    int blocked = 0, verified = 0, Inprogress = 0, total = 0, online = 0;


    private void VehicalVerificationchk() {
        driverinfo.clear();
        blocked = 0;
        verified = 0;
        Inprogress = 0;
        total = 0;
        online = 0;
        dialog.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("RidersProfile");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot dsp : snapshot.getChildren()) {
                        Log.v("Admin", "------> " + dsp);


                        driverinfo.add(dsp.getValue(DriverUser.class));
                        if (dsp.child("blocked").getValue().equals("true")) {
                            blocked++;
                        }
                        else
                        if (dsp.child("profile_status").getValue().equals("verified")) {
                            verified++;
                        } else {
                            Inprogress++;
                        }
                        total++;


                    }
                }
                getCArrDAta();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                VehicalVerificationchk();
            }
        });

        ///

    }
    private void getCArrDAta() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DriversInformation");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot dsp : snapshot.getChildren()) {
                        Log.v("Admin", "------> " + dsp);

                        if (dsp.child("profile_status").getValue().toString().trim().equals("incomplete")) {

                            Inprogress++;
                        }


                    }
                }

                ccounOnlinetdrivers();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                VehicalVerificationchk();
            }
        });

        ///

    }

    private void ccounOnlinetdrivers() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("onlineDriver").child("count");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //
                if (snapshot.exists()) {
                    online = Integer.parseInt(String.valueOf(snapshot.getChildrenCount()));

                }

                setdataup();
                //
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setdataup() {
        try {

            prgblock.setMaximum(total + .01f);
            prgblock.setProgress(blocked + .01f);


            prgoffline.setMaximum(total + .01f);
            prgoffline.setProgress(total - online + .01f);


            prgonline.setMaximum(total + .01f);
            prgonline.setProgress(online + .01f);

            /// blocked per
            if (total!=0){
            tvblocked.setText((((blocked * 100) / total)) + "%");
            tvonlineper.setText((((online * 100) / total)) + "%");
            tvofflineper.setText(((((total - online) * 100)/ total)) + "%");}

            inprocesxs.setText(String.valueOf(Inprogress));
            completed.setText(String.valueOf(verified));
            cancelled.setText(String.valueOf(blocked));
            totaltv.setText(String.valueOf(total));


            driver_rcy.setLayoutManager(new LinearLayoutManager(getActivity()));
            carinfoRCAdopter = new CarinfoRCAdopter(driverinfo, getActivity());

            driver_rcy.setAdapter(carinfoRCAdopter);
            carinfoRCAdopter.notifyDataSetChanged();
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        } catch (Exception e) {

        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

    }

}

