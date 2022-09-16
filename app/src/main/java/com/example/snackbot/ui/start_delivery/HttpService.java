package com.example.snackbot.ui.start_delivery;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.snackbot.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HttpService extends Service {
    private final IBinder binder = new LocalBinder();
    private HttpServiceCallbacks httpServiceCallbacks;

    /**
     * Class used for the client Binder
     */
    public class LocalBinder extends Binder {
        HttpService getService() {
            // Return this instance of LocalService so clients can call public methods
            return HttpService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setCallbacks(HttpServiceCallbacks callbacks) {
         httpServiceCallbacks = callbacks;
    }

    public void startDelivery() {
        Call<String> call = RetrofitClient.getInstance().getMyApi().startDelivery();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                String result = response.toString();
                if (httpServiceCallbacks != null) {
                    httpServiceCallbacks.onDeliveryStarted(result);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                String result = t.getLocalizedMessage();
                if (httpServiceCallbacks != null) {
                    httpServiceCallbacks.onDeliveryStopped(result);
                }
            }
        });
    }

    public void cancelDelivery() {
        Call<String> call = RetrofitClient.getInstance().getMyApi().cancelDelivery();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                String result = response.toString();
                if (httpServiceCallbacks != null) {
                    httpServiceCallbacks.onDeliveryStopped(result);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                String result = t.getLocalizedMessage();
                if (httpServiceCallbacks != null) {
                    httpServiceCallbacks.onDeliveryStopped(result);
                }
            }
        });
    }

    public interface HttpServiceCallbacks{
        void onDeliveryStarted(String result);
        void onDeliveryStopped(String result);
    }
}