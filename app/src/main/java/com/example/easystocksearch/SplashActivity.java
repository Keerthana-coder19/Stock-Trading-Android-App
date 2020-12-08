package com.example.easystocksearch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    Handler timerhandler = new Handler();
    Runnable runnable;
    int delay = 1500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_EasyStockSearch_Launcher);
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.splash);
        timerhandler.postDelayed(runnable = new Runnable() {
            public void run() {

                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, delay);

    }
}
