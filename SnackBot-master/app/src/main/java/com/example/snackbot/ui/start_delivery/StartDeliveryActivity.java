package com.example.snackbot.ui.start_delivery;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import com.example.snackbot.databinding.ActivityStartDeliveryBinding;
import com.example.snackbot.ui.main.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.snackbot.R;

public class StartDeliveryActivity extends AppCompatActivity implements HttpService.HttpServiceCallbacks {

    private AppBarConfiguration appBarConfiguration;
    private ActivityStartDeliveryBinding binding;
    private HttpService httpService;
    private boolean bound = false;
    private boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStartDeliveryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        //httpService = new HttpService(this);

        binding.buttonStartDeliveryPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (active) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StartDeliveryActivity.this);
                    builder.setTitle("Warning: Cancelling Delivery");
                    builder.setMessage("You are about to cancel the active delivery. " +
                            "This operation will take some time for the server to process and you will be unable " +
                            "to start another delivery until it is complete. Are you sure you wish to continue?");
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            httpService.cancelDelivery();
                            goToMainActivity();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                } else {
                    goToMainActivity();
                }
            }
        });

        binding.buttonStartDeliveryStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.buttonStartDeliveryStart.setEnabled(false);
                binding.progressBar.setVisibility(View.VISIBLE);
                httpService.startDelivery();
            }
        });

        binding.buttonStopDeliveryStop.setEnabled(false);
        binding.buttonStopDeliveryStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StartDeliveryActivity.this);
                builder.setTitle("Warning: Cancelling Delivery");
                builder.setMessage("You are about to cancel the active delivery. " +
                        "This operation will take some time for the server to process and you will be unable " +
                        "to start another delivery until it is complete. Are you sure you wish to continue?");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        httpService.cancelDelivery();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, HttpService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
        bound = false;
    }

    /** Callbacks for service binding, passed to bindService() */
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            HttpService.LocalBinder binder = (HttpService.LocalBinder) service;
            httpService = binder.getService();
            bound = true;
            httpService.setCallbacks(StartDeliveryActivity.this); // register
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

    private void goToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        this.startActivity(i);
    }

    @Override
    public void onDeliveryStarted(String result) {
        binding.buttonStopDeliveryStop.setEnabled(true);
        binding.progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        active = true;
    }

    @Override
    public void onDeliveryStopped(String result) {
        binding.buttonStartDeliveryStart.setEnabled(true);
        binding.buttonStopDeliveryStop.setEnabled(false);
        binding.progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        active = false;
    }

}