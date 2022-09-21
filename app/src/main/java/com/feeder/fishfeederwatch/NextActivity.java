package com.feeder.fishfeederwatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.feeder.fishfeederwatch.databinding.ActivityNextBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NextActivity extends Activity {

    TextView morningCount, eveningCount, manualCount, morningTime, eveningTime, manualTime;
    private ActivityNextBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNextBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        manualCount = findViewById(R.id.manualCount);
        eveningCount = findViewById(R.id.eveningCount);
        morningCount = findViewById(R.id.morningCount);
        manualTime = findViewById(R.id.manualTime);
        eveningTime = findViewById(R.id.eveningTime);
        morningTime = findViewById(R.id.morningTime);
        Intent intent = getIntent();
        Toast.makeText(NextActivity.this, "Feed Status Fetched Successfully", Toast.LENGTH_SHORT).show();
        String value = intent.getStringExtra("response");
        try {
            JSONArray respArray = new JSONArray(value);
            int n = respArray.length();
            for (int i = 0; i < n; i++) {
                JSONObject obj = respArray.getJSONObject(i);
                String type = obj.getString("type");
                if (type.equals("morningAuto")) {
                    morningTime.setText(obj.getString("time"));
                    morningCount.setText(obj.getString("count"));
                } else if (type.equals("eveningAuto")) {
                    eveningTime.setText(obj.getString("time"));
                    eveningCount.setText(obj.getString("count"));
                } else {
                    manualTime.setText(obj.getString("time"));
                    manualCount.setText(obj.getString("count"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}