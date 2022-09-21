package com.feeder.fishfeederwatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.wear.widget.BoxInsetLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.feeder.fishfeederwatch.databinding.ActivityMainBinding;

public class MainActivity extends Activity {

    Button feedButton, infoButton, okButton, cancelButton;
    FrameLayout progressContainer;
    LinearLayout confirmContainer;
    BoxInsetLayout mainContainer;
    FrameLayout container;
    RequestQueue queue;
    TextView progressText;
    Vibrator vibrator;
    private TextView mTextView;
    private ActivityMainBinding binding;

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (!isNetworkConnected()) {
            Toast.makeText(getApplicationContext(), "You are not connected to network", Toast.LENGTH_SHORT).show();
        }
        infoButton = findViewById(R.id.infoButton);
        feedButton = findViewById(R.id.feedButton);
        mainContainer = findViewById(R.id.mainContainer);
        okButton = findViewById(R.id.btn_ok);
        cancelButton = findViewById(R.id.btn_cancel);
        container = findViewById(R.id.container);
        progressContainer = findViewById(R.id.progressContainer);
        progressText = findViewById(R.id.progressText);
        confirmContainer = findViewById(R.id.confirmContainer);
        queue = Volley.newRequestQueue(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        infoButton.setOnClickListener(view -> {
            vibrator.vibrate(10);
            if (!isNetworkConnected()) {
                Toast.makeText(getApplicationContext(), "Please connect to network", Toast.LENGTH_SHORT).show();
            } else {
                container.setVisibility(View.GONE);
                progressText.setText("Getting Feed Status");
                progressContainer.setVisibility(View.VISIBLE);
                infoFunc();
            }
        });
        feedButton.setOnClickListener(view -> {
            vibrator.vibrate(10);
            if (!isNetworkConnected()) {
                Toast.makeText(getApplicationContext(), "Please connect to network", Toast.LENGTH_SHORT).show();
            } else {
                confirmContainer.setVisibility(View.VISIBLE);
                container.setVisibility(View.GONE);
                okButton.setOnClickListener(v -> {
                    vibrator.vibrate(10);
                    progressText.setText("Starting Feed Operation");
                    progressContainer.setVisibility(View.VISIBLE);
                    confirmContainer.setVisibility(View.GONE);
                    feedFunc();
                });
                cancelButton.setOnClickListener(v -> {
                    vibrator.vibrate(10);
                    container.setVisibility(View.VISIBLE);
                    confirmContainer.setVisibility(View.GONE);
                });
            }
        });
        mTextView = binding.text;
    }

    private void infoFunc() {
        String url = R.string.feeder_url + "/feeder/feedInfo";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            progressContainer.setVisibility(View.GONE);
            vibrator.vibrate(10);
            Intent myIntent = new Intent(MainActivity.this, NextActivity.class);
            myIntent.putExtra("response", response);
            MainActivity.this.startActivity(myIntent);
            container.setVisibility(View.VISIBLE);
        }, error -> {
            vibrator.vibrate(10);
            progressContainer.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "Feeder Service is Unavailable", Toast.LENGTH_SHORT).show();
        });
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void feedFunc() {
        String url = R.string.feeder_url + "/feeder/manualFeed";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            vibrator.vibrate(10);
            container.setVisibility(View.VISIBLE);
            progressContainer.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
        }, error -> {
            vibrator.vibrate(10);
            container.setVisibility(View.VISIBLE);
            progressContainer.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "Feeder Service is Unavailable", Toast.LENGTH_SHORT).show();
        });
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}