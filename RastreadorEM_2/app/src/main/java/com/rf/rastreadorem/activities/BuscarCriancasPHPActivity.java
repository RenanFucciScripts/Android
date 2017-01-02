package com.rf.rastreadorem.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rf.rastreadorem.R;
import com.rf.rastreadorem.dao.Crianca;
import com.rf.rastreadorem.io.IO;
import com.rf.rastreadorem.io.Save_File;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Activity da Tela de Buscar Crianças via PHP
 *
 * @author RenanFucci
 */
public class BuscarCriancasPHPActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String STR_URL_GET_CRIANCAS_PHP = "http://harpia.profalexaraujo.com.br/get_criancas_harpia.php";
    private static final CharSequence URL_DOWNLOAD_PHP = "url_download";
    private final String TAG = BuscarCriancasPHPActivity.class.getName();
    private ImageButton mImageButton_buscar_crianca_php;
    private EditText mEditText_cod_crianca;
    private ProgressBar mProgressBar_busca_crianca_php;
    private Context ctx;
    private LinearLayout mLinearLayout_criancas_php;
    private ArrayList<Crianca> criancas;
    private JsonArray jsonarray;
    private String str_erro;
    private File fl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_criancas_php);
        ctx = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mProgressBar_busca_crianca_php = (ProgressBar) findViewById(R.id.progressBar_buscar_crianca_php);
        mProgressBar_busca_crianca_php.setVisibility(View.GONE);

        mLinearLayout_criancas_php = (LinearLayout) findViewById(R.id.linearLayout_criancas_php);
        mEditText_cod_crianca = (EditText) findViewById(R.id.editText_cod_crianca_php);
        mImageButton_buscar_crianca_php = (ImageButton) findViewById(R.id.imageButton_buscar_php);
        mImageButton_buscar_crianca_php.setOnClickListener((click) -> {
            if (click != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(click.getWindowToken(), 0);
            }
            String cod_crianca_editText = mEditText_cod_crianca.getText().toString();
            if (cod_crianca_editText.contentEquals("")) {
                Toast.makeText(ctx, ctx.getResources().getString(R.string.campos_vazios), Toast.LENGTH_LONG).show();
            } else {
                mProgressBar_busca_crianca_php.setVisibility(View.VISIBLE);
                new Handler().postDelayed(() -> {
                    buscarCriancasSERVER(cod_crianca_editText);
                }, 500);
            }

        });
        mEditText_cod_crianca.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    String cod_crianca_editText = mEditText_cod_crianca.getText().toString();
                    if (cod_crianca_editText.contentEquals("")) {
                        Toast.makeText(ctx, ctx.getResources().getString(R.string.campos_vazios), Toast.LENGTH_LONG).show();
                    } else {
                        mProgressBar_busca_crianca_php.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(() -> {
                            buscarCriancasSERVER(cod_crianca_editText);
                        }, 500);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método buscarCriancasSERVER</strong><br>
     * Método AsyncTask para buscar, a partir de um código de criança, imagens do Servidor, pegar os
     * dados dessas imagens no formato JSON e mostrá-los na tela em forma de lista por imagem.
     * No qual, essa lista disponibiliza um link para download para cada um dos itens dessa lista,
     * isto é, para cada imagem.<br>
     * Obs.: A apresentação gráfica dos resultado é dinâmico e relativo ao que está no script
     * PHP no servidor, se seguido o padrão que está descrito no DOC do PHP.<br>
     * @param cod_crianca_editText String com o código da criança;
     */
    private void buscarCriancasSERVER(String cod_crianca_editText) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                Log.e(TAG, "doInBackground: db");
                Log.e(TAG, "doInBackground: cod: " + cod_crianca_editText);
                try {

                    URL url = new URL(STR_URL_GET_CRIANCAS_PHP);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    String encodeFormat = "UTF-8";
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, encodeFormat));
                    String data = URLEncoder.encode("cod_crianca", encodeFormat) + "=" + URLEncoder.encode(cod_crianca_editText, encodeFormat);
                    Log.e(TAG, "doInBackground: data: " + data);
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();

                    BufferedReader br;
                    StringBuilder sb;
                    String line = "";
                    String str_result;
                    if (200 <= httpURLConnection.getResponseCode() && httpURLConnection.getResponseCode() <= 299) {
                        br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
                        sb = new StringBuilder();
                        while ((line = br.readLine()) != null) {
                            sb.append(line + '\n');
                        }
                        str_result = sb.toString();
                    } else {
                        br = new BufferedReader(new InputStreamReader((httpURLConnection.getErrorStream())));
                        sb = new StringBuilder();
                        while ((line = br.readLine()) != null) {
                            sb.append(line + '\n');
                        }
                        str_result = sb.toString();
                    }

                    Log.e(TAG, "doInBackground: strconsult: \n" + str_result);
                    if (str_result.contentEquals("-1")) {
                        str_erro = ctx.getResources().getString(R.string.cod_crianca_inexistente_ou_vazia);
                    } else if (str_result.contentEquals("-2")) {
                        str_erro = ctx.getResources().getString(R.string.param_post_errado);
                    } else if (str_result.contentEquals("-3")) {
                        str_erro = ctx.getResources().getString(R.string.erro_web_service);
                    } else {
                        JsonParser parser = new JsonParser();
                        jsonarray = parser.parse(str_result).getAsJsonArray();
                        return true;
                    }
                } catch (MalformedURLException ex) {
                    Log.e(TAG, "EX: " + ex.getLocalizedMessage());
                    ex.printStackTrace();
                } catch (Exception e) {
                    Log.e(TAG, "EX: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
                str_erro = ctx.getResources().getString(R.string.erro_desconhecido);
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                mProgressBar_busca_crianca_php.setVisibility(View.GONE);
                if (aBoolean) {
                    mLinearLayout_criancas_php.setBackgroundColor(Color.parseColor("#ffffff"));
                    if (((LinearLayout) mLinearLayout_criancas_php).getChildCount() > 0)
                        ((LinearLayout) mLinearLayout_criancas_php).removeAllViews();
                    for (int i = 0; i < jsonarray.size(); i++) {
                        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        RelativeLayout mRelativeLayout = (RelativeLayout) vi.inflate(R.layout.fragment_crianca_sem_layout, null);
                        ImageButton mImageButton_download = (ImageButton) mRelativeLayout.findViewById(R.id.imageButton_download);
                        mImageButton_download.setVisibility(View.GONE);

                        Set<Map.Entry<String, JsonElement>> entrySet = jsonarray.get(i).getAsJsonObject().entrySet();
                        Iterator<Map.Entry<String, JsonElement>> entryIterator = entrySet.iterator();
                        LinearLayout auxRelativeLayout = (LinearLayout) mRelativeLayout.findViewById(R.id.linearLayout_sem_crianca);
                        while (entryIterator.hasNext()) {
                            Map.Entry<String, JsonElement> next = entryIterator.next();
                            String key = next.getKey();
                            String value = next.getValue().getAsString();
                            Log.e(TAG, "onPostExecute: key: " + key + "  value: " + value);

                            TextView aux_textView_key = new TextView(ctx);
                            aux_textView_key.setTextAppearance(ctx, android.R.style.TextAppearance_Medium);
                            TextView aux_textView_value = new TextView(ctx);
                            if (key.contentEquals(URL_DOWNLOAD_PHP)) {
                                aux_textView_key.setVisibility(View.GONE);
                                aux_textView_value.setVisibility(View.GONE);
                                mImageButton_download.setVisibility(View.VISIBLE);

                                mImageButton_download.setOnClickListener((click) -> {
                                    mProgressBar_busca_crianca_php.setVisibility(View.VISIBLE);
                                    new AsyncTask<Void,Void,Boolean>(){

                                        @Override
                                        protected Boolean doInBackground(Void... voids) {
                                            Save_File save_file = new Save_File();
                                            boolean aBoolean = save_file.Save_File(ctx, value);
                                            fl = save_file.getFl();
                                            return aBoolean;
                                        }

                                        @Override
                                        protected void onPostExecute(Boolean aBoolean) {
                                            mProgressBar_busca_crianca_php.setVisibility(View.GONE);
                                            if(aBoolean){
                                                Toast.makeText(ctx, ctx.getResources().getString(R.string.salvo_sucesso), Toast.LENGTH_LONG).show();
                                                Intent shareintent = new Intent(Intent.ACTION_SEND);
                                                String mimeType = getMimeType(fl);
                                                Log.e(TAG, "onPostExecute: mime: "+mimeType );
                                                Uri uri = Uri.fromFile(fl);
                                                Log.e(TAG, "onPostExecute: uri: "+uri );
                                                shareintent.setType(mimeType);
                                                shareintent.putExtra(Intent.EXTRA_TEXT, fl.getName());
                                                shareintent.putExtra(Intent.EXTRA_STREAM, uri);
                                                startActivity(Intent.createChooser(shareintent, "Compartilhar:"));

                                            }else{
                                                Toast.makeText(ctx, ctx.getResources().getString(R.string.impossivel_fzr_download), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }.execute();
                                });
                            } else {
                                aux_textView_key.setText(key+": ");
                                aux_textView_key.setTextColor(Color.BLACK);
                                LinearLayout.LayoutParams layouParams = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                                layouParams.setMargins(24, 8, 0, 8);
                                auxRelativeLayout.addView(aux_textView_key, layouParams);
                                layouParams = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                                aux_textView_value.setText("-\t"+value);
                                aux_textView_value.setTextColor(Color.BLACK);
                                layouParams.setMargins(64, 8, 0, 8);
                                auxRelativeLayout.addView(aux_textView_value, layouParams);
                            }
                        }
                        LinearLayout.LayoutParams layouParams = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//                    layouParams.setMargins(24, 24, 24, 24);
                        mLinearLayout_criancas_php.addView(mRelativeLayout, layouParams);
                    }
                } else {
                    Toast.makeText(ctx, str_erro, Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
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
        getMenuInflater().inflate(R.menu.buscar_criancas_ph, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_busca_crianca_php) {
//            startActivity(new Intent(this, BuscarCriancasPHPActivity.class));
        } else if (id == R.id.nav_exit) {
            ctx = this;
            IO.setarLogin(this, false);
            new Handler().postDelayed(() -> {
                IO.restartApp(ctx);
            }, 2000);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método getMimeType</strong><br>
     * Método para pegar o MimeType do arquivo disponibilizado no método
     * {@link #buscarCriancasSERVER(String) buscarCriancasSERVER()}
     * e escolhido pelo usuário pela interface para download.<br>
     * @param fl File arquivo escolhido para download;
     */
    public String getMimeType(File fl){
        return (MimeTypeMap.getFileExtensionFromUrl(fl.getAbsolutePath()) ==null)? (null)
                : (MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap
                    .getFileExtensionFromUrl(fl.getAbsolutePath())));
    }
}
