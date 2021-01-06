package com.loadease.uberclone.adminpanels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.loadease.uberclone.adminpanels.Frags.DashboardFragment;
import com.loadease.uberclone.adminpanels.Frags.DriverinfoFragment;
import com.loadease.uberclone.adminpanels.Frags.PricingFragment;
import com.loadease.uberclone.adminpanels.Frags.VehicalmanaggmentFragment;

import static com.loadease.uberclone.adminpanels.R.id.fragment_container_view;

public class MainActivityBottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_bottom_navigation);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().add(fragment_container_view, new DashboardFragment()).commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()){
            case R.id.DashBoard:

                RemovepreviousFrags();
                getSupportFragmentManager().beginTransaction().add(fragment_container_view, new DashboardFragment()).commit();
                break;

            case R.id.Drivers:

RemovepreviousFrags();
getSupportFragmentManager().beginTransaction().add(fragment_container_view, new DriverinfoFragment()).commit();
                break;

            case R.id.Pricing:

                RemovepreviousFrags();
                getSupportFragmentManager().beginTransaction().add(fragment_container_view, new PricingFragment()).commit();
                break;


            case R.id.Vehical:

                RemovepreviousFrags();
                getSupportFragmentManager().beginTransaction().add(fragment_container_view, new VehicalmanaggmentFragment()).commit();
                break;

        }


        return true;
    }

    private void RemovepreviousFrags(){
        try {


        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }}catch (Exception e){

        }
           }

}