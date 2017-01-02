package br.com.inhookahinc.inhookah.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import br.com.inhookahinc.inhookah.R;


public class DeciderMainActivity extends AppCompatActivity {

    private boolean logged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decider_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    public void onClickEntrarApp(View view){
        logged = isLoggedIn(this);
        if (logged) {
            finish();
            startActivity(new Intent(this, MenuActivity.class ));
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
                return true;
            }
             //testa login sem facebook
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            boolean loggedIn = prefs.getBoolean("Islogin", false);
            if (loggedIn) {
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //else
        return false;
    }

}
