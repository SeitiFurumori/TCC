package com.example.msa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashDevMedia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_dev_media);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mostrarLogin();
            }
        },2000);

    }

    private void mostrarLogin() {
        Intent intent = new Intent(SplashDevMedia.this, PaginaInicialActivity.class);
        startActivity(intent);
        finish();
    }
}