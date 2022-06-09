package com.geziuygulamasi.All_Views;

import androidx.appcompat.app.AppCompatActivity;

public class MainCompatActivity extends AppCompatActivity {

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}