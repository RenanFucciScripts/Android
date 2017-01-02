package br.com.inhookahinc.inhookah.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import br.com.inhookahinc.inhookah.R;
import br.com.inhookahinc.inhookah.methods.io.IO;

public class PerfilActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
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
            ((CollapsingToolbarLayout) findViewById(R.id.perfil_toolbar_layout)).setBackground(drawable);
        }else{
            navigationView.inflateHeaderView(R.layout.nav_header_menu).setBackground(getResources().getDrawable(R.mipmap.bg_nav_default));
            ((CollapsingToolbarLayout) findViewById(R.id.perfil_toolbar_layout)).setBackground(getResources().getDrawable(R.mipmap.bg_nav_default));
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
        getMenuInflater().inflate(R.menu.perfil, menu);
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
            Intent intent = new Intent(this,MenuActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_add_local) {
            Intent intent = new Intent(this,AddLocalActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_promo) {
            Intent intent = new Intent(this,PromoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_perfil) {

        } else if (id == R.id.nav_mais_sobre) {
            Intent intent = new Intent(this,MaisActivity.class);
            startActivity(intent);

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
