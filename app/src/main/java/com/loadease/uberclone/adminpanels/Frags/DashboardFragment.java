package com.loadease.uberclone.adminpanels.Frags;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.loadease.uberclone.adminpanels.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DashboardFragment extends Fragment {
    ProgressDialog dialog;

    int TotalRides,Continue,Completed,TargetRides;
TextView Totalrideperx,totalridestv,inprocesstv,complletetv,cancelltv,totalsale,totaldriveroutof,todate;
    com.budiyev.android.circularprogressbar.CircularProgressBar  totalpercentageprg;
    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_dashboard, container, false);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        Totalrideperx =root.findViewById(R.id.Totalrideper);
        totalridestv=root.findViewById(R.id.totalridestv);
        totalpercentageprg = root.findViewById(R.id.totalpercentageprg);


        cancelltv = root.findViewById(R.id.canceltv);

        complletetv = root.findViewById(R.id.completetv);
        inprocesstv = root.findViewById(R.id.inprocesstv);


        totalsale=root.findViewById(R.id.totalsale);
        totaldriveroutof=root.findViewById(R.id.totaldriveroutof);
        todate=root.findViewById(R.id.todate);
        getuserDAta();
        return root;

    }



    private void getuserDAta(){


        TotalRides=0;Continue=0;Completed=0;TargetRides=100;
        dialog.show();
        DatabaseReference db=FirebaseDatabase.getInstance().getReference("Trips");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    DatabaseReference db1=FirebaseDatabase.getInstance().getReference("Trips");
                    db1.child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snapshot1:snapshot.getChildren()) {
                                if (snapshot1.child("trip_status").getValue() != null) {
                                    if (snapshot1.child("trip_status").getValue().toString().trim().equals("complete")) {
                                        Log.v("qqq", "completed");
                                        Completed++;
                                    } else {

                                        Log.v("qqq", "continue");
                                        Continue++;
                                    }

                                    TotalRides++;
                                }
                            }
                            setdataup();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void  setdataup(){
Log.v("datachk",Completed+"/"+TargetRides+" ="+(Completed/TargetRides)*100);
        totalpercentageprg.setMaximum(TargetRides+.01f);
        totalpercentageprg.setProgress(Completed+.01f);


        Totalrideperx.setText(((Completed*100   /TargetRides))+"%");

        totalridestv.setText(String.valueOf(TotalRides));
inprocesstv.setText(String.valueOf(Continue));
        complletetv.setText(String.valueOf(Completed));
        inprocesstv.setText(String.valueOf(Continue));
        cancelltv.setText(String.valueOf(0));
        getuserDriversdata();

    }

int online,total;

    private void getuserDriversdata(){
     total=0;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("RidersProfile");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dsp: snapshot.getChildren()){

                    total++;


                }
                ccountdrivers();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                getuserDriversdata();
            }
        });

        ///

    }

    private void ccountdrivers(){
        online=0;
        DatabaseReference db=FirebaseDatabase.getInstance().getReference("onlineDriver").child("count");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                online= Integer.parseInt(snapshot.getValue().toString().trim());
                totaldriveroutof.setText(online+"/"+total);
                getTotalprice();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    Double totalprice=0.0;
    private void getTotalprice(){

        String curr_Time="";
        SimpleDateFormat _24HourSDF;
        SimpleDateFormat _12HourSDF;
        Date _24HourDt;
        LocalTime localTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            ZoneId z = ZoneId.of("Asia/Karachi");
            localTime = LocalTime.now(z);

            Locale locale_en_US = Locale.forLanguageTag("PK");
            DateTimeFormatter formatterUS = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale_en_US);
            String output = localTime.format(formatterUS);
            LocalDate locale_date = LocalDate.now(z);
            Locale locale_SAU_date = Locale.forLanguageTag("PK");
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(locale_SAU_date);
            String output2 = locale_date.format(formatter);
            todate.setText(output2);
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("TotalPrice").child(output2);
            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dsp : snapshot.getChildren()) {

                            totalprice = totalprice + Double.parseDouble(dsp.child("price").getValue().toString().trim());

                        }
                    }
                totalsale.setText(String.valueOf(Math.round(totalprice)));


                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

}