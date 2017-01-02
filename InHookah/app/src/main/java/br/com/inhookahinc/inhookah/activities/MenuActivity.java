package br.com.inhookahinc.inhookah.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import br.com.inhookahinc.inhookah.R;
import br.com.inhookahinc.inhookah.methods.io.IO;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG ="Menu:\n\t ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(Build.VERSION.SDK_INT >= 16) {
            Drawable drawable = IO.getNavHeaderImg(this);
            if (drawable != null) {//Se existe a foto do user
                navigationView.inflateHeaderView(R.layout.nav_header_menu).setBackground(drawable);
            } else {//senao, pega a padrão
                navigationView.inflateHeaderView(R.layout.nav_header_menu).setBackground(getResources().getDrawable(R.mipmap.bg_nav_default));
            }
        }
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
        getMenuInflater().inflate(R.menu.menu, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_buscar_local) {

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
            Intent intent = new Intent(this,MaisActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            CharSequence charSequence = getString(R.string.shareWith);
            String link= "\"https://play.google.com/store/apps/details?id=" + appPackageName+"\"";
            String message = "<a href="+link+"> InHookah </a>";

            Intent intentTarget=new Intent(Intent.ACTION_SEND);
            intentTarget.setType("text/plain");
            intentTarget.putExtra(Intent.EXTRA_TEXT, message);

            Intent intent = Intent.createChooser(intentTarget, charSequence);
            intent.putExtra(Intent.EXTRA_TEXT, message);

            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
