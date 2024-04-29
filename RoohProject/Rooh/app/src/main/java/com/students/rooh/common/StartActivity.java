package com.students.rooh.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.students.rooh.R;

public class StartActivity extends AppCompatActivity {

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        handler = new Handler();
        runnable = () -> {
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        };
        handler.postDelayed(runnable, 2000L);
    }

    @Override
    protected void onPause() {
        if (isFinishing()) {
            handler.removeCallbacks(runnable);
        }
        super.onPause();
    }
}