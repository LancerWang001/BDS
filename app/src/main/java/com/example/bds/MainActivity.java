package com.example.bds;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.bds.mainfragments.LoginFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.preAuth, new LoginFragment())
                .commit();
    }
}