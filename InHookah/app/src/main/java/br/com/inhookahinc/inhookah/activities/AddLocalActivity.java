package br.com.inhookahinc.inhookah.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import br.com.inhookahinc.inhookah.R;
import br.com.inhookahinc.inhookah.methods.asyncTask.FTPCliente;
import br.com.inhookahinc.inhookah.methods.io.IO;

public class AddLocalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Spinner spinner1;
    private String TAG= "add_local:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_local);
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
        getMenuInflater().inflate(R.menu.add_local, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


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




    public void onClickedBtnAdd(View view){

        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject manJson    = new JSONObject();

            String localName    = ((EditText) findViewById(R.id.editTextNomeLocal)).getText().toString();
            String localPhone   = ((EditText) findViewById(R.id.editTextTelefone)).getText().toString();
            String localAddress = ((EditText) findViewById(R.id.editTextEndereco)).getText().toString();

            HashMap<String, Boolean> locations = new HashMap<>();
            locations.put("locatioType_Bar", ((CheckBox) findViewById(R.id.checkBox_Bar)).isChecked());
            locations.put("locatioType_Tabacaria",((CheckBox) findViewById(R.id.checkBox_Tabacaria)).isChecked());
            locations.put("locatioType_Lounge", ((CheckBox) findViewById(R.id.checkBox_Lounge)).isChecked());
            locations.put("locatioType_NemSei", ((CheckBox) findViewById(R.id.checkBox_NemSei)).isChecked());
            locations.put("locatioType_LocalPublico",  ((CheckBox) findViewById(R.id.checkBox_LocalPublico)).isChecked());
            locations.put("locatioType_Restaurante",((CheckBox) findViewById(R.id.checkBox_Restaurante)).isChecked());


            Switch rent = ((Switch) findViewById(R.id.switchAluga));
            Switch sell = ((Switch) findViewById(R.id.switchVende));
            Switch allowUsage = ((Switch) findViewById(R.id.switchPermitirUso));
            Switch hasPlug = ((Switch) findViewById(R.id.switchPossuiTomada));

            if (localName.contentEquals("") ){
                Snackbar.make(view, getString(R.string.fill_name), Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }else if(localAddress.contentEquals("")){
                Snackbar.make(view, getString(R.string.fill_address), Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }else {
                manJson.put("allowUsage", (allowUsage.isChecked() ? "1" : "0")+"");
                /*
                 * TODO:Pegar dinamico do BD
                 * */
                manJson.put("id","1");
                /*
                 * TODO:Pegar dinamico do BD ou FACEBOOK
                 * */
                manJson.put("userId","1");
                manJson.put("localName", localName);
                for (String key : locations.keySet()) {
                    Log.e(key, locations.get(key)+"");
                    manJson.put(key, locations.get(key));
                }
                manJson.put("localAddress", localAddress);
                manJson.put("localPhone", localPhone);
                manJson.put("rent", (rent.isChecked() ? "1" : "0") + "");
                manJson.put("sell", (sell.isChecked() ? "1" : "0")+"");
                manJson.put("hasPlug", (hasPlug.isChecked() ? "1" : "0")+"");
                jsonObject.put("JSON Object", manJson);

                FTPCliente ftpCliente = new  FTPCliente(this, "upload", jsonObject);
                ftpCliente.execute();
                finish();
            }

        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }
    }

}
