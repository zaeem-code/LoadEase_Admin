package com.loadease.uberclone.adminpanels.Messages;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCM_send_msg {


    final
    String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final String serverKey = "key=" + "AAAAOmcjWXA:APA91bFX_6U0IGMsMV3dNlNBrvKXcEZvZs4vW1SLaGOcgSHyA0VJ9irlmXRj56f8hjCeAHxUuhotvfUy9ZFnkg5qs9tQjhqCO3viv1q0dNEo76OotmGfbVnV3o86JIhyJlQH3EgnfFoJ";
    final  String contentType = "application/json";
    String   TOPIC = "/topics/";
String key;
    JSONObject notification = new JSONObject();
    JSONObject notifcationBody = new JSONObject();
    public FCM_send_msg(Context context, String topic, String Message, String Title) {
        TOPIC=TOPIC+topic;

        try {

            notifcationBody.put("title", Title);
            notifcationBody.put("message",Message);

            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e("hassan", "fcm exp: " + e.getMessage() );
        }
        sendNotification(notification,context);
    }






    private void sendNotification(JSONObject notification, Context context) {
Log.v("FCMMMM","ok");
       RequestQueue requestQueue = null;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("hassan", "onResponse: " + response.toString());

                        Toast.makeText(context,"A notification has been sent to Driver", Toast.LENGTH_LONG).show();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                              Log.i("hassan", "hassan: volley Didn't work"+error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        if (requestQueue==null){

            requestQueue= Volley.newRequestQueue(context);
            requestQueue.add(jsonObjectRequest);
        }else {
            requestQueue.add(jsonObjectRequest);}
    }
}




