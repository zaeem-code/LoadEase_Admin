package com.loadease.uberclone.adminpanels.Service;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.loadease.uberclone.adminpanels.Common.Common;
import com.loadease.uberclone.adminpanels.Messages.DriverRequestReceived;
import com.loadease.uberclone.adminpanels.Model.Token;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.Random;

public class firebaseMessaging extends FirebaseMessagingService{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);

        Log.v("FCMMM","in");
        if(remoteMessage.getData().get("title").equals("New Driver"))
        {
            Log.v("FCMMM","ok");
//            Pickup pickup=new Gson().fromJson(remoteMessage.getNotification().getBody(), Pickup.class);
            new notification_genrater(getApplicationContext(),remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), remoteMessage.getData().get("Key"));
        }






        Map<String, String> datrecover=remoteMessage.getData();

        if (datrecover!=null)
        {

            if (datrecover.get("title").equals("RequestDriver"))
            {


                DriverRequestReceived driverRequestReceived=new DriverRequestReceived();
                driverRequestReceived.setKey(datrecover.get("RiderKey"));
                driverRequestReceived.setPickupLocation(datrecover.get("PickupLocation"));
                driverRequestReceived.setPuckupLocationString(datrecover.get("PickupLocationString"));
                driverRequestReceived.setDestinationLocation(datrecover.get("DestinationLocation"));
                driverRequestReceived.setDestinationLocationString(datrecover.get("DestinationLocationString"));



                EventBus.getDefault().postSticky(driverRequestReceived);



            }
            else
            {

                Common.showNotification(this,new Random().nextInt(),
                        datrecover.get("title"),
                        datrecover.get("body"),
                        null

                );

            }


        }
        else
        {

            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }









    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference tokens=db.getReference(Common.token_tbl);

        Token token=new Token(s);
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)tokens.child(FirebaseAuth.getInstance().getUid())
                .setValue(token);
    }












}
