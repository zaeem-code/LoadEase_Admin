package com.loadease.uberclone.adminpanels.Frags;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loadease.uberclone.adminpanels.R;

import java.util.HashMap;


public class PricingFragment extends Fragment implements OnCompleteListener {
TextView truckedit,pickupedit,mazdaedit,shazoreedit,apply;
    EditText Base,time,dis;
    ProgressDialog dialog;
    public PricingFragment() {
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
        View root=inflater.inflate(R.layout.fragment_pricing, container, false);
        truckedit=root.findViewById(R.id.truckedit);
        pickupedit=root.findViewById(R.id.pickupedit);
        mazdaedit=root.findViewById(R.id.mazdaedit);
        shazoreedit=root.findViewById(R.id.shazoreedit);

         dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");



        truckedit.setOnClickListener(this::Editpricelistner);
        shazoreedit.setOnClickListener(this::Editpricelistner);
        mazdaedit.setOnClickListener(this::Editpricelistner);
        pickupedit.setOnClickListener(this::Editpricelistner);
        apply=root.findViewById(R.id.apply);
        apply.setOnClickListener(this::Editpricelistner);


        Base=root.findViewById(R.id.basefare);
        time=root.findViewById(R.id.timefare);
        dis=root.findViewById(R.id.distancefare);



        root.findViewById(R.id.shahzorelyt).setOnClickListener(this::Editpricelistner);
        root.findViewById(R.id.pickuplyt).setOnClickListener(this::Editpricelistner);
        root.findViewById(R.id.mazdalyt).setOnClickListener(this::Editpricelistner);
        root.findViewById(R.id.trucklyt).setOnClickListener(this::Editpricelistner);

        truckedit.setTextColor(Color.RED);
        get_PricingValue(vehicalchk);
        return root;

    }

String vehicalchk="Truck";

    public void Editpricelistner(View view) {
        switch (view.getId()){
            case R.id.truckedit:
            case R.id.trucklyt:

                truckedit.setTextColor(Color.RED);
                shazoreedit.setTextColor(Color.WHITE);
                pickupedit.setTextColor(Color.WHITE);
                mazdaedit.setTextColor(Color.WHITE);
                apply.setVisibility(View.VISIBLE);
                vehicalchk="Truck";
                get_PricingValue(vehicalchk);
            break;
            case R.id.mazdaedit:
            case R.id.mazdalyt:
                mazdaedit.setTextColor(Color.RED);
                shazoreedit.setTextColor(Color.WHITE);
                truckedit.setTextColor(Color.WHITE);
                pickupedit.setTextColor(Color.WHITE);
                apply.setVisibility(View.VISIBLE);
                vehicalchk="Mazda";
                get_PricingValue(vehicalchk);
                break;
            case R.id.pickupedit:
            case R.id.pickuplyt:

                pickupedit.setTextColor(Color.RED);
                shazoreedit.setTextColor(Color.WHITE);
                truckedit.setTextColor(Color.WHITE);
                mazdaedit.setTextColor(Color.WHITE);
                apply.setVisibility(View.VISIBLE);
                vehicalchk="Pickup";
                get_PricingValue(vehicalchk);
                break;
            case R.id.shazoreedit:
            case R.id.shahzorelyt:

                truckedit.setTextColor(Color.WHITE);
                pickupedit.setTextColor(Color.WHITE);
                mazdaedit.setTextColor(Color.WHITE);
                shazoreedit.setTextColor(Color.RED);
                apply.setVisibility(View.VISIBLE);
                vehicalchk="Shahzore";
                get_PricingValue(vehicalchk);
                break;

            case R.id.apply:

//                shazoreedit.setTextColor(Color.WHITE);
//                truckedit.setTextColor(Color.WHITE);
//                pickupedit.setTextColor(Color.WHITE);
//                mazdaedit.setTextColor(Color.WHITE);
                 add_PricingValue(vehicalchk);
                break;
        }
    }



    public void get_PricingValue(String vehical)
    {
        dialog.show();
        DatabaseReference db= FirebaseDatabase.getInstance().getReference("pricingValue").child(vehical);

db.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        Base.setText(snapshot.child("baseFare").getValue().toString());
        time.setText(snapshot.child("timeRate").getValue().toString());
        dis.setText(snapshot.child("distanceRate").getValue().toString());
        if (dialog.isShowing()){
            dialog.dismiss();
        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        if (dialog.isShowing()){
            dialog.dismiss();
        }

    }
});

    }
    public void add_PricingValue(String vehicalchk)
    {

if (!TextUtils.isEmpty(Base.getText()) &&!TextUtils.isEmpty(time.getText()) &&
        !TextUtils.isEmpty(dis.getText())){

    dialog.show();
    DatabaseReference db= FirebaseDatabase.getInstance().getReference("pricingValue");
        HashMap hashMap=new HashMap();
        hashMap.put("baseFare",Base.getText().toString());
        hashMap.put("timeRate",time.getText().toString());
        hashMap.put("distanceRate",dis.getText().toString());
switch (vehicalchk){
    case "Truck":
        db.child("Truck").updateChildren(hashMap).addOnCompleteListener(this);

        break;
    case "Pickup":
        db.child("Pickup").updateChildren(hashMap).addOnCompleteListener(this);
        break;
    case "Mazda":
        db.child("Mazda").updateChildren(hashMap).addOnCompleteListener(this);
        break;
    case "Shahzore":
        db.child("Shahzore").updateChildren(hashMap).addOnCompleteListener(this);
        break;
}

        Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();

        apply.setVisibility(View.GONE);

    }else {
    Toast.makeText(getActivity(), "All the fields are required", Toast.LENGTH_SHORT).show();}
    }


    @Override
    public void onComplete(@NonNull Task task) {
        if (task.isComplete()){
            if (dialog.isShowing()) {dialog.dismiss();}

            Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();

            apply.setVisibility(View.GONE);
        }else {
            if (dialog.isShowing()) {dialog.dismiss();}

            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();

        }
    }
}

