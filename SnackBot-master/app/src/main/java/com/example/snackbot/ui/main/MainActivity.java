package com.example.snackbot.ui.main;

import android.content.Intent;
import android.os.Bundle;

import com.example.snackbot.data.AppStatus;
import com.example.snackbot.databinding.ActivityMainBinding;
import com.example.snackbot.ui.login.LoginActivity;
import com.example.snackbot.ui.start_delivery.StartDeliveryActivity;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.snackbot.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.snackbot.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setTitle("RoboSnacks");
        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(view -> Snackbar.make(view, "This feature has not yet been implemented", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        binding.buttonManageRobots.setOnClickListener(view -> Snackbar.make(view, "This feature has not yet been implemented", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        binding.buttonStartDelivery.setOnClickListener(view -> moveToStartDelivery());

        binding.buttonLogout.setOnClickListener(view -> logout());

        final TextView date = binding.textViewDate;
        date.setText(getDate());

        String username = "";
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            username = getIntent().getExtras().getString("username");
        }
        if (username != null && !username.isEmpty()) {
            username = username.split("@")[0];
            if (username.length() > 16) {
                username = username.substring(0, 15);
            }
            AppStatus.getInstance().setUsername(username);
        }

        final TextView tvUsername = binding.textViewUsername;
        String value = AppStatus.getInstance().getUsername() + "!";
        tvUsername.setText(value);
    }

    private CharSequence getDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        return df.format(c);
    }

    private void logout() {
        Intent i = new Intent(this, LoginActivity.class);
        this.startActivity(i);
    }

    private void moveToStartDelivery() {
        Intent i = new Intent(this, StartDeliveryActivity.class);
        this.startActivity(i);
    }

}