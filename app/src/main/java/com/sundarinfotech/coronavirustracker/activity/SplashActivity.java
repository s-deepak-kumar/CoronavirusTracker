package com.sundarinfotech.coronavirustracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sundarinfotech.coronavirustracker.R;
import com.sundarinfotech.coronavirustracker.util.NetworkHelper;

public class SplashActivity extends AppCompatActivity {

    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        timer();
    }

    public void timer(){
        countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {
                long lSec = l/1000;
                //tSkipTitle.setText("Wait "+lSec+ " Seconds");
            }

            @Override
            public void onFinish() {
                NetworkHelper.setContext(SplashActivity.this);
                if (NetworkHelper.CheckNetwork()) {
                    Intent intent = new Intent(SplashActivity.this, HomepageActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(SplashActivity.this, "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
                    timer();
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (countDownTimer != null) {
            countDownTimer.start();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
