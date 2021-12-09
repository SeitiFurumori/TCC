package com.example.msa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class PaginaInicialActivity extends AppCompatActivity {
private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_inicial);
        verificaAutenticação();
        bottomNavigationView = findViewById(R.id.bottonNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId())
            {
                case R.id.home:
                    fragment = new HomeFragment();
                    break;
                case R.id.adicionar:
                    fragment = new AdicionarFragment();
                    break;
                case R.id.mais:
                    fragment = new MaisFragment();
                    break;
                case R.id.perfil:
                    fragment = new PerfilFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
            return true;
        }
    };

    private void verificaAutenticação() {
        if (FirebaseAuth.getInstance().getUid() == null) {
            Intent intent = new Intent(PaginaInicialActivity.this, MainActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

        }
    }
}