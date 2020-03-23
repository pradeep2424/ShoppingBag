package com.example.myapplication.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init()
    {
        Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {

                }
            }, 2000);
    }
}
