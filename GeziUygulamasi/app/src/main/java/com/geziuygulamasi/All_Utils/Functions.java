package com.geziuygulamasi.All_Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.geziuygulamasi.All_Activities.MainActivity;
import com.geziuygulamasi.All_Fragments.PlaceDetailsFragment;
import com.geziuygulamasi.All_Models.Place;
import com.geziuygulamasi.R;

import java.util.Objects;

public class Functions {
    private static Dialog dialog;
    public static boolean appRuntimeRunning() {
        return ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED);
    }

    public static void handlerDelayed(Runnable runnable, int delayMillis){
        new Handler().postDelayed(() -> {
            if(appRuntimeRunning() && runnable != null)
                runnable.run();
        }, delayMillis);
    }

    public static boolean isValidEmailAddress(String emailAddress){
        return (!TextUtils.isEmpty(emailAddress) && Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches());
    }

    public static boolean isShowingLoader(){
        return dialog != null && dialog.isShowing();
    }

    public static void showLoadingDialog(Activity activity) {
        if(isShowingLoader())
            return;

        cancelLoadingDialog();
        if(appRuntimeRunning() && !activity.isFinishing() && !activity.isDestroyed()) {
            dialog = new Dialog(activity);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_loading);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    public static void cancelLoadingDialog() {
        if(appRuntimeRunning() && isShowingLoader() ) {
            Context context = ((ContextWrapper) Objects.requireNonNull(dialog).getContext()).getBaseContext();
            if (context instanceof Activity) {
                if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed())
                    dialog.dismiss();
            } else
                dialog.dismiss();
        }
        dialog = null;
    }

    public static void openPlaceDetailsPage(Place place){
        Bundle bundle = new Bundle();
        bundle.putSerializable("place", place);
        PlaceDetailsFragment placeDetailsFragment = new PlaceDetailsFragment();
        placeDetailsFragment.setArguments(bundle);
        MainActivity.fragmentManager.beginTransaction().add(R.id.container, placeDetailsFragment).addToBackStack("place_details_" + place.getCategoryId()).commit();
    }

    public static void hideSoftKeyboard(AppCompatActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null)
            view = new View(activity);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}