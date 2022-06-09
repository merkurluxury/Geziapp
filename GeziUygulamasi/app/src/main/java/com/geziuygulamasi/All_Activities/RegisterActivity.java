package com.geziuygulamasi.All_Activities;

import static com.geziuygulamasi.All_Utils.Functions.isValidEmailAddress;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.geziuygulamasi.All_Utils.Functions;
import com.geziuygulamasi.All_Views.MainCompatActivity;
import com.geziuygulamasi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.Objects;

public class RegisterActivity extends MainCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
        EditText userEmailAddressInput = findViewById(R.id.userEmailAddress);
        EditText userPasswordInput = findViewById(R.id.userPassword);
        EditText userRetypePasswordInput = findViewById(R.id.userRetypePassword);

        findViewById(R.id.registerButton).setOnClickListener(v -> {
            String userEmailAddress = userEmailAddressInput.getText().toString();
            String userPassword = userPasswordInput.getText().toString();
            String userRetypePassword = userRetypePasswordInput.getText().toString();

            if(isValidEmailAddress(userEmailAddress)){
                if(userPassword.length() > 5){
                    if(userPassword.equals(userRetypePassword)){
                        Functions.showLoadingDialog(this);
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmailAddress, userPassword).addOnCompleteListener(task -> {
                            Functions.cancelLoadingDialog();
                            if (!task.isSuccessful()) {
                                try {
                                    throw Objects.requireNonNull(task.getException());
                                }
                                catch (FirebaseAuthUserCollisionException ignored){
                                    Toast.makeText(this, getString(R.string.already_using_email), Toast.LENGTH_LONG).show();
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else{
                                Toast.makeText(this, getString(R.string.welcome_user), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(this, SplashActivity.class));
                                finish();
                            }
                        });
                    }
                    else
                        Toast.makeText(this, getString(R.string.no_match_password), Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(this, getString(R.string.short_password), Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(this, getString(R.string.please_enter_valid_email), Toast.LENGTH_LONG).show();
        });
        findViewById(R.id.redirectLogin).setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }
}