package com.loadease.uberclone.adminpanels.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loadease.uberclone.adminpanels.Model.DriverUser;
import com.loadease.uberclone.adminpanels.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class DriverDetailActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
boolean chkforfcm=false;
String blocked="",Appove="";
    TextView  driver_name, rating, pd_dob,gender_txt, pd_email, pd_ph_num, car_type, year_of_prod, num_plate,status_txt,blstatus_txt,blstatusblock_txt;
    Button confirm;
    EditText block_ed_tx;
    ImageView cnic_pic, vechicle_pic, Licence_pic;
    CircleImageView circ_img;

String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_detail);

        Ids();
    }
    private void getintnentextr() {


        id = getIntent().getStringExtra("UID").toString();
        try {
            chkforfcm = getIntent().getBooleanExtra("chk", false);

        } catch (Exception e) {

        }
        if (chkforfcm) {

            loaddatafromfirebase();
            ////to be con

        } else {

            driver_name.setText(getIntent().getStringExtra("Name").toString());
//        rating.setText( getIntent().getStringExtra("rating").toString());
            pd_dob.setText(getIntent().getStringExtra("DOB").toString());
            gender_txt.setText(getIntent().getStringExtra("gen").toString());
            pd_email.setText(getIntent().getStringExtra("Email").toString());
            pd_ph_num.setText(getIntent().getStringExtra("Phone").toString());
            car_type.setText(getIntent().getStringExtra("carType").toString());
            year_of_prod.setText(getIntent().getStringExtra("Yop").toString());
            num_plate.setText(getIntent().getStringExtra("carnum").toString());
            status_txt.setText(getIntent().getStringExtra("Status").toString());
            Appove=getIntent().getStringExtra("Status").toString();
            blocked = getIntent().getStringExtra("Blocked").toString();
            blstatusblock_txt.setText(blocked);
            Glide.with(getApplicationContext()).load(getIntent().getStringExtra("CNICURL").toString()).into(cnic_pic);
            Glide.with(getApplicationContext()).load(getIntent().getStringExtra("LICURL").toString()).into(Licence_pic);
            Glide.with(getApplicationContext()).load(getIntent().getStringExtra("VEHURL").toString()).into(vechicle_pic);
            Glide.with(getApplicationContext()).load(getIntent().getStringExtra("DPURL").toString()).into(circ_img);
        }

        if (blocked.equals("false")) {
            buttonblock.setChecked(false);
            buttonblock.setVisibility(View.GONE);
            Toast.makeText(this, blocked, Toast.LENGTH_SHORT).show();
        } else {
            buttonblock.setChecked(true);

        }


        if (Appove.equals("verified")) {
            buttonApproved.setChecked(true);
        } else {

            buttonApproved.setChecked(false);
        }


    }
    private void loaddatafromfirebase() {
        getuserDAta();
    }
    Switch buttonblock,buttonApproved;
    public void Ids() {

          buttonApproved = findViewById(R.id.approved);
        buttonApproved.setOnCheckedChangeListener(this);
        buttonApproved.setTextOff("Pending");
        buttonApproved.setTextOn("Approved");

          buttonblock= findViewById(R.id.block);
        buttonblock.setOnCheckedChangeListener(this);
        buttonblock.setTextOff("No");
        buttonblock.setTextOn("Yes");


        blstatusblock_txt=findViewById(R.id.blstatusblock_txt);
        status_txt=findViewById(R.id.status_txt);
        circ_img = findViewById(R.id.profile_image);
         driver_name = findViewById(R.id.name_txt);
        rating = findViewById(R.id.rated);
        gender_txt = findViewById(R.id.gender_txt);
        pd_dob = findViewById(R.id.dob_txt);
        pd_email = findViewById(R.id.email_txt);
        pd_ph_num = findViewById(R.id.ph_num);
        car_type = findViewById(R.id.car_typ_txt);
        year_of_prod = findViewById(R.id.year_prod_txt);
        num_plate = findViewById(R.id.num_plate_txt);

        confirm = findViewById(R.id.confirm_btn);
        confirm.setOnClickListener(this);
        findViewById(R.id.backdrawe).setOnClickListener(this);
        block_ed_tx = findViewById(R.id.ed_txt_block);

        cnic_pic = findViewById(R.id.cnic_pic);
        Licence_pic = findViewById(R.id.Licence_pic_img);
        vechicle_pic = findViewById(R.id.Vehicle_pic_img);

        getintnentextr();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if(id== R.id.confirm_btn){
            ExitAlert();
        }else{
            finish();
        }
    }
    private void ExitAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Blacklist Confirmation");
        dialog.setIcon(R.drawable.bg_drawer);
        dialog.setCancelable(false);
        dialog.setMessage("Are you sure you want to block : "+driver_name.getText().toString());

        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String txt = block_ed_tx.getText().toString().trim();
                if (!TextUtils.isEmpty(txt)) {
                    blockprocesss(txt);
                } else {
                    Toast.makeText(DriverDetailActivity.this, "Please Add a detail reasoning for blocking", Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }
    private void blockprocesss(String txt){


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("RidersProfile").child(id);
        myRef.child("blocked").setValue("true");
        myRef.child("blockedComments").setValue(txt);
        Toast.makeText(this, driver_name.getText().toString()+" is blocked Successfully", Toast.LENGTH_SHORT).show();
        finish();

    }
    private void zoom(Bitmap url){

        MyDialogFragment dialogFragment = new MyDialogFragment(url);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        dialogFragment.show(ft, "dialog");
    }

    public void onClickPIC(View view) {
        switch (view.getId()){
            case R.id.cnic_pic:
                try {
                    zoom(((BitmapDrawable)cnic_pic.getDrawable()).getBitmap());

                }catch (Exception e){

                }
            break;
            case R.id.Licence_pic_img: try {
                zoom(((BitmapDrawable)Licence_pic.getDrawable()).getBitmap());    }catch (Exception e){

        }
                break;
            case R.id.Vehicle_pic_img: try {
                zoom(((BitmapDrawable)vechicle_pic.getDrawable()).getBitmap());    }catch (Exception e){

    }
                break;
        }
    }
    private void getuserDAta(){
        ProgressDialog dialog;
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("RidersProfile").child(id);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dsp: snapshot.getChildren()){
                    Log.v("Admin","------> "+snapshot);


                DriverUser item=        snapshot.getValue(DriverUser.class);

//                rating.setText( item.ra).toString());
                driver_name.setText( item.getName());
                pd_dob.setText( item.getDOB());
                gender_txt.setText( item.getGender());
                pd_email.setText(item.getEmail());
                pd_ph_num.setText( item.getPhone());
                car_type.setText(item.getCarType());
                year_of_prod.setText(item.getYear_of_prodution());
                num_plate.setText( item.getCarnum());
                Appove=item.getProfile_status();
                status_txt .setText( Appove);
                blocked =item.getBlocked();
                blstatusblock_txt.setText(blocked);
                Glide.with(getApplicationContext()).load(item.getRider_cnic_pic_url()).into(cnic_pic);
                Glide.with(getApplicationContext()).load( item.getRider_licence_pic()).into(Licence_pic);
                Glide.with(getApplicationContext()).load( item.getRider_vehical_pic()).into(vechicle_pic);
                Glide.with(getApplicationContext()).load( item.getRider_pic_Url()).into(circ_img);



                if (blocked.equals("true")) {
                    buttonblock.setChecked(true);

                    buttonblock.setVisibility(View.VISIBLE);
                } else
                    {

                    buttonblock.setChecked(false);
                        buttonblock.setVisibility(View.GONE);
                }


                if (Appove.equals("verified")) {
                    buttonApproved.setChecked(true);
                } else
                    {

                    buttonApproved.setChecked(false);
                }

                if (dialog.isShowing()){
    dialog.dismiss();
}



        }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                getuserDAta();
            }
        });
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.approved:
                setdriverVerificaion(id, b);
                break;
            case R.id.block:
                setdriverBlock(id,b);
                break;
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

    private void setdriverBlock(String id, boolean b){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("RidersProfile").child(id);
        if (b){
            myRef.child("blocked").setValue("true");
        }else {

            myRef.child("blocked").setValue("false");
        }
    }
}