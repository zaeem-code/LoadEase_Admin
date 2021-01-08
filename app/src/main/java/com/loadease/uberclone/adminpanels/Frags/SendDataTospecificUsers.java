package com.loadease.uberclone.adminpanels.Frags;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loadease.uberclone.adminpanels.Model.DriverinfoRCAdopterforDiscounttospecific;
import com.loadease.uberclone.adminpanels.Model.PassengerUser;
import com.loadease.uberclone.adminpanels.Model.User;
import com.loadease.uberclone.adminpanels.R;

import java.util.ArrayList;


    public class SendDataTospecificUsers extends AppCompatActivity implements View.OnClickListener {
        RecyclerView rec;
        ProgressDialog dialog;

        DriverinfoRCAdopterforDiscounttospecific infoRCAdopter;

        ArrayList<PassengerUser> UsersArraylist =new ArrayList<>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_senddatatospecificusers);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
            dialog = new ProgressDialog(this);
            dialog.setMessage("Loading...");
            rec =findViewById(R.id.rcy);

findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        finish();
    }
});
            getuserDAta();
        }



        private void setuprc(){


            rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            infoRCAdopter =new DriverinfoRCAdopterforDiscounttospecific(UsersArraylist,getApplicationContext());

            rec.setAdapter(infoRCAdopter);
            infoRCAdopter.notifyDataSetChanged();
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }


        private void getuserDAta() {

            dialog.show();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("RidersInformation");
            try {
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UsersArraylist.clear();
                        for (DataSnapshot dsp : snapshot.getChildren()) {
                            Log.v("Admin", "------> " + dsp);

                            UsersArraylist.add(dsp.getValue(PassengerUser.class));
                        }

                        if (UsersArraylist.size() > 0) {
                            setuprc();

                        } else {

                            Toast.makeText(SendDataTospecificUsers.this, "No user available right now", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        getuserDAta();
                    }
                });
            } catch (Exception e) {
                Toast.makeText(SendDataTospecificUsers.this, "No user available right now", Toast.LENGTH_SHORT).show();

            }

        }
        @Override
        public void onClick(View view) {
            if (view.getId()==R.id.openrec){
                findViewById(R.id.layoutadd).setVisibility(View.GONE);

                findViewById(R.id.layoutrec).setVisibility(View.VISIBLE);

            }else {

                findViewById(R.id.layoutadd).setVisibility(View.VISIBLE);

                findViewById(R.id.layoutrec).setVisibility(View.GONE);

            }

    }
}
