package com.example.snackbot;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    String BASE_URL = "http://192.168.0.102:8000/api/";
    @GET("runStartScript")
    Call<String> startDelivery();

    @GET("runStopScript")
    Call<String> cancelDelivery();
}