package com.example.sistematriage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intent = new Intent(Splash.this, MenuPrincipal.class);
                startActivity(intent);
                finish();

            }
        },2000);

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        intent = null;
        Runtime.getRuntime().gc();
    }
}