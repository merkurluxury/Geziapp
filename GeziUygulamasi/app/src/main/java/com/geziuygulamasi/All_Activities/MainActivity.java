package com.geziuygulamasi.All_Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

import com.geziuygulamasi.All_Fragments.MainFragment;
import com.geziuygulamasi.All_Utils.Variables;
import com.geziuygulamasi.All_Views.MainCompatActivity;
import com.geziuygulamasi.R;

public class MainActivity extends MainCompatActivity {
    @SuppressLint("StaticFieldLeak")
    private static Activity activity;
    @SuppressLint("StaticFieldLeak")
    private static MainFragment mainFragment;
    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        activity = this;
        mainFragment = new MainFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, mainFragment).commit();
    }

    @Override
    public void onBackPressed() {
        backPressAction();
    }

    public static void backPressAction(){
        int backStackEntryCount = fragmentManager.getBackStackEntryCount();
        if(backStackEntryCount > 1)
            activity.setTitle(Variables.selectedCategory);
        else
            activity.setTitle(activity.getString(R.string.app_name));
        if (!mainFragment.onBackPressed()) {
            if (backStackEntryCount > 0)
                fragmentManager.popBackStack();
        }
    }
}