package br.com.inhookahinc.inhookah.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.inhookahinc.inhookah.R;
import br.com.inhookahinc.inhookah.methods.asyncTask.FBConnection;
import br.com.inhookahinc.inhookah.methods.io.IO;

public class MaisActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AccessTokenTracker fbTracker;
    private CallbackManager mCallbackManager;
    private static String TAG= "Mais: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mais);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Drawable drawable = IO.getNavHeaderImg(this);
        if(drawable!=null) {
            navigationView.inflateHeaderView(R.layout.nav_header_menu).setBackground(drawable);
        }else{
            navigationView.inflateHeaderView(R.layout.nav_header_menu).setBackground(getResources().getDrawable(R.mipmap.bg_nav_default));
        }

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        //testa login com facebook
        if (accessToken != null) {
            RelativeLayout sairBox = (RelativeLayout) findViewById(R.id.sair_box);
            sairBox.setVisibility(View.GONE);
        }
        Log.e(TAG, LoginManager.getInstance().toString());

        List<String> usersPermissions =  new ArrayList<>();
        usersPermissions.add("user_friends");
        usersPermissions.add("email");

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.mais_login_fb);
        loginButton.setReadPermissions(usersPermissions);

        //LogIn
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken accessToken = loginResult.getAccessToken();
                        Profile fbprofile = Profile.getCurrentProfile();
                        if (fbprofile != null) {
                            FBConnection fbConnection = new FBConnection( MaisActivity.this, fbprofile);
                            fbConnection.execute();
                            try {
                                fbConnection.get();
                                IO.setarLogin(MaisActivity.this, false);//TODO: pensar no setarlogin FALSE
                                IO.restartApp(MaisActivity.this);
                            } catch (InterruptedException e) {
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                                IO.setarLogin(MaisActivity.this, false);//TODO: pensar no setarlogin FALSE
                                IO.restartApp(MaisActivity.this);
                            } catch (ExecutionException e) {
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                                IO.setarLogin(MaisActivity.this, false);//TODO: pensar no setarlogin FALSE
                                IO.restartApp(MaisActivity.this);
                            }
                        }
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e(TAG, exception.getMessage());
                    }
                });

        //LogOut
        fbTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                if (accessToken2 == null) {
                    Log.d("FB", "User Logged Out.");
                    IO.restartApp(MaisActivity.this);
                }
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mais, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_toolbar_add) {
            Intent intent = new Intent(this,AddLocalActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_toolbar_checkIn){
            CharSequence charSequence = getString(R.string.shareWith);
            Intent intentTarget =  new Intent(Intent.ACTION_SEND);
            Intent intent = Intent.createChooser(intentTarget, charSequence);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_buscar_local) {
            Intent intent = new Intent(this,MenuActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_add_local) {
            Intent intent = new Intent(this,AddLocalActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_promo) {
            Intent intent = new Intent(this,PromoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_perfil) {
            Intent intent = new Intent(this,PerfilActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_mais_sobre) {

        } else if (id == R.id.nav_share) {
            CharSequence charSequence = getString(R.string.shareWith);
            Intent intentTarget =  new Intent(Intent.ACTION_SEND);
            //intentTarget.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            intentTarget.setType("text/plain");
            Intent intent = Intent.createChooser(intentTarget, charSequence);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
