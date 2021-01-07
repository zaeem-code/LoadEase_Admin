package com.loadease.uberclone.adminpanels.Frags;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loadease.uberclone.adminpanels.Activities.Home;
import com.loadease.uberclone.adminpanels.Common.Common;
import com.loadease.uberclone.adminpanels.Messages.FCM_send_msg;
import com.loadease.uberclone.adminpanels.Model.Discountmodal;
import com.loadease.uberclone.adminpanels.Model.DiscountnfoRCAdopter;
import com.loadease.uberclone.adminpanels.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

//
//public class DriverinfoFragment extends Fragment {
public class PromotionsAndDiscountsFragment extends AppCompatActivity implements View.OnClickListener {
RecyclerView rec;
    ProgressDialog dialog;
    EditText name,percentage,validtill,code,etMsg;
    String nametv,percentagetv,validtilltv,codetv,etMsgtv;

    FirebaseDatabase database;
    DatabaseReference myRef;
    DiscountnfoRCAdopter discountnfoRCAdopter;
    ArrayList<Discountmodal> DiscountArraylist =new ArrayList<>();

    Discountmodal item;
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        View root = inflater.inflate(R.layout.drivers_details_activity, container, false);
//        driver_rcy=root.findViewById(R.id.driver_rcy);
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_discount);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    getSupportActionBar().hide();
    item=new Discountmodal();
             database = FirebaseDatabase.getInstance();
         myRef = database.getReference("PromotionalDetails");
    findViewById(R.id.openrec).setOnClickListener(this);
    findViewById(R.id.closerec).setOnClickListener(this);
    dialog = new ProgressDialog(this);
    dialog.setMessage("Loading...");
    rec =findViewById(R.id.rcy);
    name=findViewById(R.id.etName);
    validtill=findViewById(R.id.etValiddate);
    percentage=findViewById(R.id.etPercentage);
    code=findViewById(R.id.etCode);
    etMsg=findViewById(R.id.etMsg);
    findViewById(R.id.sendtoall).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            clickedsendtoall();
        }
    });
    findViewById(R.id.senttospecfic).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            clickedSendtospecific();
        }
    });


//        return root;
new Home().add_PricingValue();
}



    private void setuprc(){


        rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        discountnfoRCAdopter =new DiscountnfoRCAdopter(DiscountArraylist,getApplicationContext());

        rec.setAdapter(discountnfoRCAdopter);
        discountnfoRCAdopter.notifyDataSetChanged();

        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }



    public Boolean checkDateFormat(String date){
        if (date == null || !date.matches("^(1[0-9]|0[1-9]|3[0-1]|2[1-9])/(0[1-9]|1[0-2])/[0-9]{4}$"))
            return false;
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
        try {
            format.parse(date);
            return true;
        }catch (ParseException e){
            return false;
        }
    }

private void clickedsendtoall(){
    nametv=name.getText().toString().trim();
    percentagetv=percentage.getText().toString().trim();
    validtilltv=validtill.getText().toString().trim();



     boolean bol= checkDateFormat(validtilltv);



    codetv=code.getText().toString().trim();
    etMsgtv=etMsg.getText().toString().trim();

    if (!TextUtils.isEmpty(nametv) &&!TextUtils.isEmpty(percentagetv) && bol==true && !TextUtils.isEmpty(codetv)&& !TextUtils.isEmpty(etMsgtv)){
        dialog.show();

        item.setDiscode(codetv);
        item.setDiscdercentage(percentagetv);

        item.setDiscvalidationdate(validtilltv);
        item.setDisname(nametv);
        item.setDiscountdetaials(etMsgtv);
        myRef.child(codetv).setValue(item).addOnCompleteListener(new
                                                           OnCompleteListener<Void>() {
                                                               @Override
                                                               public void onComplete(@NonNull Task<Void> task) {

                                                                   nametv="";
                                                                   codetv="";
                                                                   percentagetv="";
                                                                   validtilltv="";
                                                                   etMsgtv="";
                                                                   name.setText("");
                                                                   percentage.setText("");
                                                                   validtill.setText("");
                                                                   code.setText("");
                                                                   etMsg.setText("");

                                                                   new FCM_send_msg(getApplicationContext(),"Disc",item.getDiscountdetaials());
getuserDAta();

                                                               }
                                                           });
    }else {
        Toast.makeText(this, "All the Fields must ne empty", Toast.LENGTH_SHORT).show();
    }
}

    private void clickedSendtospecific(){
        nametv=name.getText().toString().trim();
        percentagetv=percentage.getText().toString().trim();
        validtilltv=validtill.getText().toString().trim();

        codetv=code.getText().toString().trim();
        etMsgtv=etMsg.getText().toString().trim();

        if (!TextUtils.isEmpty(nametv) &&!TextUtils.isEmpty(percentagetv) &&!TextUtils.isEmpty(validtilltv) && !TextUtils.isEmpty(codetv)&& !TextUtils.isEmpty(etMsgtv)){
            dialog.show();

            item.setDiscode(codetv);
            item.setDiscdercentage(percentagetv);
            item.setDiscvalidationdate(validtilltv);
            item.setDisname(nametv);
            item.setDiscountdetaials(etMsgtv);
            Common.itemforspecificusers=item;
            startActivity(new Intent(this,SendDataTospecificUsers.class));
        }else {
            Toast.makeText(this, "All the Fields must ne empty", Toast.LENGTH_SHORT).show();
        }
    }
    private void getuserDAta(){
        dialog.show();
        try {


myRef.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        for (DataSnapshot dsp: snapshot.getChildren()){
            Log.v("Admin","------> "+dsp);

    DiscountArraylist.add(dsp.getValue(Discountmodal.class));
}

        setuprc();

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        getuserDAta();



    }
});
        }catch (Exception e){
            if (dialog.isShowing()){
                dialog.dismiss();
            }

        }}


    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.openrec){
            getuserDAta();
            findViewById(R.id.layoutadd).setVisibility(View.GONE);

            findViewById(R.id.layoutrec).setVisibility(View.VISIBLE);

        }else {

            findViewById(R.id.layoutadd).setVisibility(View.VISIBLE);

            findViewById(R.id.layoutrec).setVisibility(View.GONE);

        }
    }
}