package com.example.renanfucci.buscagro.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.renanfucci.buscagro.R;
import com.example.renanfucci.buscagro.method.asyncTask.BackgroundTask;
import com.example.renanfucci.buscagro.method.asyncTask.FBConnection;
import com.example.renanfucci.buscagro.method.io.IO;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private CallbackManager mCallbackManager;
    private static final String TAG = "Main: ";
    private ProfileTracker mProfileTracker;
    private LoginButton loginButton;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ctx = this;
        List<String> usersPermissions = new ArrayList<>();
        usersPermissions.add("user_friends");
        usersPermissions.add("email");

        mCallbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.main_FB_login);
        loginButton.setReadPermissions(usersPermissions);
        loginButton.registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.e(TAG, "onSuccess: " );
                        AccessToken accessToken = loginResult.getAccessToken();
                        Profile fbprofile = Profile.getCurrentProfile();
                        if (fbprofile != null) {
                            try {

                                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.i("LoginActivity", response.toString());
                                        // Get facebook data from login
                                        Bundle bFacebookData = getFacebookData(object);
                                    }
                                });

                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                                request.setParameters(parameters);
                                request.executeAsync();

                                FBConnection fbConnection = new FBConnection(MainActivity.this, fbprofile);
                                fbConnection.execute();
                                fbConnection.get();
                                finish();
                                startActivity(new Intent(MainActivity.this, TelaInicialActivity.class));
                            } catch (InterruptedException e) {
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                                finish();
                                startActivity(new Intent(MainActivity.this, TelaInicialActivity.class));
                            } catch (ExecutionException e) {
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                                finish();
                                startActivity(new Intent(MainActivity.this, TelaInicialActivity.class));
                            }
                        } else if (fbprofile == null) {

                            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    Log.i("LoginActivity", response.toString());
                                    // Get facebook data from login
                                    Bundle bFacebookData = getFacebookData(object);
                                }
                            });

                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                            request.setParameters(parameters);
                            request.executeAsync();

                            mProfileTracker = new ProfileTracker() {
                                @Override
                                protected void onCurrentProfileChanged(Profile profile, Profile profile2) {

                                    mProfileTracker.stopTracking();
                                    Log.e(TAG, "loading img");
                                    FBConnection fbConnection = new FBConnection(MainActivity.this, profile2);
                                    fbConnection.execute();
                                    try {
                                        fbConnection.get();
                                        finish();
                                        startActivity(new Intent(MainActivity.this, TelaInicialActivity.class));
                                    } catch (InterruptedException e) {
                                        Log.e(TAG, e.getMessage());
                                        e.printStackTrace();
                                        finish();
                                        startActivity(new Intent(MainActivity.this, TelaInicialActivity.class));
                                    } catch (ExecutionException e) {
                                        Log.e(TAG, e.getMessage());
                                        e.printStackTrace();
                                        finish();
                                        startActivity(new Intent(MainActivity.this, TelaInicialActivity.class));
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
                        Log.e(TAG, "onError: ");
                        Log.e(TAG, exception.getMessage());
                    }
                });
    }


    private Bundle getFacebookData(JSONObject object) {
        Log.e(TAG, "getFacebookData:" );
        Bundle bundle = new Bundle();

        try {
            String id = object.getString("id");

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));


            String user_id = object.getString("id");
            String user_nome = "";
            String user_email = "";
            String user_pwd = "";
            String user_tel = "";
            String user_endereco = "";

            user_nome += ((object.has("first_name")) ? (object.getString("first_name")) : " ");
            user_nome += ((object.has("last_name")) ? (" " + object.getString("last_name")) : "");
            user_email = (object.has("email") ? (object.getString("email")) : "");
            user_endereco = ((object.has("location")) ? object.getJSONObject("location").getString("name") : "");

            try {

                String[] params = {"cadastrarcomid",user_id,user_nome, user_email, user_pwd.hashCode() + "", user_tel, user_endereco};

                BackgroundTask backgroundTask = new BackgroundTask(ctx);
                backgroundTask.execute(params);
                backgroundTask.get();
                String str_result = backgroundTask.getStr_result();
                str_result = str_result.trim();
                String[] userinfos = {user_id, user_nome, user_email, user_pwd, user_tel, user_endereco};
                if (!str_result.contentEquals("-1")) {
                    IO.setarInfoUser(this, userinfos);

                } else if (str_result.contentEquals("-2")) {
                    IO.setarInfoUser(this, userinfos);
                } else {
                    Toast.makeText(this, "Sem Acesso à Internet :(", Toast.LENGTH_LONG).show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bundle;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: " );
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void onClickCadastro(View view) {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

    public void onClickEntrar(View view) {
        EditText editTextNome = (EditText) findViewById(R.id.mainlogin_editText_email);
        EditText editTextSenha = (EditText) findViewById(R.id.editText_Senha);
        if (editTextNome.getText().toString().contentEquals("") || editTextSenha.getText().toString().contentEquals("")) {
            Toast.makeText(this, "Insira Email e Senha.", Toast.LENGTH_LONG).show();
        } else {
            try {

                String email = editTextNome.getText().toString();
                String pwd = editTextSenha.getText().toString();
                String[] params = {"login", email, pwd.hashCode() + ""};

                BackgroundTask backgroundTask = new BackgroundTask(view.getContext());
                backgroundTask.execute(params);
                backgroundTask.get();

                String result = backgroundTask.getStr_result();
                result = result.trim();
                if (result.contentEquals("-1")) {
                    Toast.makeText(this, "Usuário ou Senha Incorreto.", Toast.LENGTH_LONG).show();
                } else {
                    String[] usersinfo = result.split("\\|");
                    IO.setarInfoUser(this, usersinfo);
                    IO.setarLogin(this, true);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();

                        }
                    }, 2000);
                    startActivity(new Intent(this, TelaInicialActivity.class));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
