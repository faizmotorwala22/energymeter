package com.example.uom_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.uom_app.R;

public class IntroductoryActivity extends AppCompatActivity {



    ImageView logo, imgbg;
    TextView  nameText;
    LottieAnimationView lottieAnimationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_introductory);

            imgbg = findViewById(R.id.bgimage);
            logo = findViewById(R.id.logo1);
            nameText = findViewById(R.id.text2);

            imgbg.animate().translationY(-2400).setDuration(1000).setStartDelay(2000);
            logo.animate().translationY(1400).setDuration(1000).setStartDelay(2000);
            nameText.animate().translationY(500).setDuration(1000).setStartDelay(2000);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(IntroductoryActivity.this, LoadingActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }


}