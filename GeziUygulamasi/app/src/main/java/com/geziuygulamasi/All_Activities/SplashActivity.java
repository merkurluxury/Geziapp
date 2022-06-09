package com.geziuygulamasi.All_Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.geziuygulamasi.All_Utils.Functions;
import com.geziuygulamasi.All_Utils.Variables;
import com.geziuygulamasi.All_Views.MainCompatActivity;
import com.geziuygulamasi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends MainCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    @Override
    public void onBackPressed() {

    }

    private void initView(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            Variables.userEmail = firebaseUser.getEmail();
            Variables.userEmailFirebase = Objects.requireNonNull(Variables.userEmail).split("@")[0];
            Variables.userName = Variables.userEmailFirebase.toUpperCase().substring(0, 2);
        }
        Functions.handlerDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, Variables.userEmail.equals("") ? LoginActivity.class : MainActivity.class));
            finish();
        }, 2000);
    }
}