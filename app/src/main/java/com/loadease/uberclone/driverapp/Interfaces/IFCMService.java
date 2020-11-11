package com.loadease.uberclone.driverapp.Interfaces;

import com.loadease.uberclone.driverapp.Model.FCMResponse;
import com.loadease.uberclone.driverapp.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({
        "Content-Type:application/json",
        "Authorization:key=AAAAOmcjWXA:APA91bFX_6U0IGMsMV3dNlNBrvKXcEZvZs4vW1SLaGOcgSHyA0VJ9irlmXRj56f8hjCeAHxUuhotvfUy9ZFnkg5qs9tQjhqCO3viv1q0dNEo76OotmGfbVnV3o86JIhyJlQH3EgnfFoJ"
    })
    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body Sender body);
}
