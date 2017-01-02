package br.com.inhookahinc.inhookah.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.inhookahinc.inhookah.R;
import br.com.inhookahinc.inhookah.methods.asyncTask.FBConnection;
import br.com.inhookahinc.inhookah.methods.io.IO;

public class MainActivity extends AppCompatActivity {

    private CallbackManager mCallbackManager;
    private static final String TAG="Main: ";
    private ProgressBar mprogressBar;
    private ProfileTracker mProfileTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> usersPermissions =  new ArrayList<>();
        usersPermissions.add("user_friends");
        usersPermissions.add("email");

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.button_login_fb);
        loginButton.setReadPermissions(usersPermissions);

        mprogressBar = (ProgressBar) findViewById(R.id.main_progress_bar);
        mprogressBar.setVisibility(View.GONE);


        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        mprogressBar.setVisibility(View.VISIBLE);
                        AccessToken accessToken = loginResult.getAccessToken();
                        Log.e(TAG, accessToken.getUserId());
                        Profile fbprofile = Profile.getCurrentProfile();
                        if (fbprofile != null) {
                            Log.e(TAG, "loading img");
                            FBConnection fbConnection = new FBConnection( MainActivity.this, fbprofile);
                            fbConnection.execute();
                            try {
                                fbConnection.get();
                                finish();
                                startActivity(new Intent(MainActivity.this, MenuActivity.class));
                            } catch (InterruptedException e) {
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                                finish();
                                startActivity(new Intent(MainActivity.this, MenuActivity.class));
                            } catch (ExecutionException e) {
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                                finish();
                                startActivity(new Intent(MainActivity.this, MenuActivity.class));
                            }
                        }else if(fbprofile==null){
                            mProfileTracker = new ProfileTracker() {
                                @Override
                                protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                                    Log.e("facebook - profile", profile2.getFirstName());
                                    mProfileTracker.stopTracking();
                                    Log.e(TAG, "loading img");
                                    FBConnection fbConnection = new FBConnection( MainActivity.this, profile2);
                                    fbConnection.execute();
                                    try {
                                        fbConnection.get();
                                        finish();
                                        startActivity(new Intent(MainActivity.this, MenuActivity.class));
                                    } catch (InterruptedException e) {
                                        Log.e(TAG, e.getMessage());
                                        e.printStackTrace();
                                        finish();
                                        startActivity(new Intent(MainActivity.this, MenuActivity.class));
                                    } catch (ExecutionException e) {
                                        Log.e(TAG, e.getMessage());
                                        e.printStackTrace();
                                        finish();
                                        startActivity(new Intent(MainActivity.this, MenuActivity.class));
                                    }
                                }
                            };
                            mProfileTracker.startTracking();

                        }
                    }


                    @Override
                    public void onCancel() {
                        Log.e(TAG, "onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e(TAG, exception.getMessage());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void onClickEntrar(View view){
        EditText editTextNome =  (EditText) findViewById(R.id.editText_Nome);
        EditText editTextSenha =  (EditText) findViewById(R.id.editText_Senha);
        if(editTextNome.getText().toString().contentEquals("") || editTextSenha.getText().toString().contentEquals("")){
            Snackbar.make(view, "Insira Usuário e Senha", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }
        else{
            Log.e("dados", editTextNome.getText().toString() + editTextSenha.getText().toString());
            if(editTextNome.getText().toString().contentEquals("renan")&& editTextSenha.getText().toString().contentEquals("root")){
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                prefs.edit().putBoolean("Islogin", true).apply();
                finish();

                startActivity(new Intent(this,MenuActivity.class));
            }else{
                Toast.makeText(view.getContext(), "Usuário ou Senha incorreto.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onClickCadastro(View view){
        startActivity(new Intent(this,CadastroActivity.class));
    }
}
