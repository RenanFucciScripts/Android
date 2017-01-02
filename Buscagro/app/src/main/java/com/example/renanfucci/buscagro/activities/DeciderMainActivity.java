package com.example.renanfucci.buscagro.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.example.renanfucci.buscagro.R;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class DeciderMainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private boolean logged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        setContentView(R.layout.activity_decider_main);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onClickEntrarApp();
            }
        }, 5000);
    }

    public void onClickEntrarApp(){
        logged = isLoggedIn(this);
        if (logged) {
            finish();
            startActivity(new Intent(this, TelaInicialActivity.class ));
        } else {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }


    public boolean isLoggedIn(Context context){
        try {
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            //testa login com facebook
            if (accessToken != null) {
                Log.e(TAG, "isLoggedIn: accestoken: true");
                return true;
            }
            //testa login sem facebook
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            boolean loggedIn = prefs.getBoolean("Islogin", false);
            if (loggedIn) {
                return true;
            }
        }catch (Exception e){
            Log.e(TAG, e.getLocalizedMessage());
            e.printStackTrace();
        }
        //else
        return false;
    }

  /*  private void createhashFB(){
        try {

            PackageInfo info = getPackageManager().getPackageInfo("com.example.renanfucci.buscagro", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e(TAG, "onCreate: keyhash:\t"+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }

    }*/

}
