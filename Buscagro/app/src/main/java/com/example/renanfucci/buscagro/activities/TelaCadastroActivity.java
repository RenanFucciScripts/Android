package com.example.renanfucci.buscagro.activities;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.renanfucci.buscagro.R;
import com.example.renanfucci.buscagro.method.asyncTask.BackgroundTask;
import com.example.renanfucci.buscagro.method.dao.Cliente;
import com.example.renanfucci.buscagro.method.io.IO;
import com.example.renanfucci.buscagro.method.io.RoundedImageView;
import com.example.renanfucci.buscagro.supportTab.SlidingTabLayout;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class TelaCadastroActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context ctx;

    private Toolbar toolbar;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    private static final String TAG = TelaCadastroActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mPager = (ViewPager) findViewById(R.id.tela_cadastro_view_pager);
        mPager.setAdapter(new MyPageAdapter(getSupportFragmentManager()));

        mTabs = (SlidingTabLayout) findViewById(R.id.tela_cadastro_tab_layout);
        mTabs.setDistributeEvenly(true);
        mTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorInverse, null));
        mTabs.setViewPager(mPager);

        Drawable drawable = IO.getNavHeaderImg(this);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_tela_cadastro);
        RoundedImageView roundedImageView = (RoundedImageView) headerView.findViewById(R.id.nav_header_image);
        if(drawable!=null){
            roundedImageView.setImageDrawable(drawable);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        ((TextView) headerView.findViewById(R.id.textView2)).setText(preferences.getString("Nome", " "));
        ((TextView) headerView.findViewById(R.id.textView)).setText(preferences.getString("Email"," "));

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

    public void finishMethood() {
        finish();
        startActivity(new Intent(this, TelaCadastroActivity.class));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, TelaInicialActivity.class));
        } else if (id == R.id.nav_cadastrar) {
        } else if (id == R.id.nav_consultar) {
            startActivity(new Intent(this, TelaConsultaActivity.class));
        } else if (id == R.id.nav_cancelar) {
            startActivity(new Intent(this, TelaCancelamentoActivity.class));
        } else if (id == R.id.nav_editar_cadastro) {
            startActivity(new Intent(this, EditarCadastroActivity.class));
        } else if (id == R.id.nav_log_off) {
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            if (accessToken != null) {
                LoginManager.getInstance().logOut();
            }

            IO.setarLogin(this, false);
            ctx =this;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    IO.restartApp(ctx);

                }
            }, 2000);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class MyPageAdapter extends FragmentPagerAdapter {

        public String[] tabs;

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tab_oferta_demanda);
        }

        @Override
        public Fragment getItem(int position) {
            MyFragment myFragment = MyFragment.getInstance(position);
            return myFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return tabs.length;
        }
    }

    public static class MyFragment extends Fragment {

        private TextView textView_cadastrar;
        private TextView textView_validade_da;
        private EditText editText_nome_produto;
        private EditText editText_qntd_produto;
        private Button button_cadastrar;
        private SeekBar seekbar_validade;
        private String oferta_ou_demanda;

        public static MyFragment getInstance(int position) {
            MyFragment myFragment = new MyFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            myFragment.setArguments(args);
            return myFragment;
        }

        private Activity getActivity(View v) {
            Context context = v.getContext();
            while (context instanceof ContextWrapper) {
                if (context instanceof Activity) {
                    return (Activity) context;
                }
                context = ((ContextWrapper) context).getBaseContext();
            }
            return null;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.fragment_cadastrar, container, false);
            Bundle bundle = getArguments();
            String aux;
            oferta_ou_demanda = getResources().getStringArray(R.array.tab_oferta_demanda)[bundle.getInt("position")];

            editText_nome_produto = (EditText) layout.findViewById(R.id.fragment_cadastrar_editText_nome_produto);
            editText_qntd_produto = (EditText) layout.findViewById(R.id.fragment_cadastrar_editText_qntd_produto);
            button_cadastrar = (Button) layout.findViewById(R.id.fragment_cadastrar_button_cadastrar);

            if (bundle != null) {
                seekbar_validade = (SeekBar) layout.findViewById(R.id.fragment_cadastrar_seekbar_validade_produto);
                seekbar_validade.setProgress(1);

                textView_cadastrar = (TextView) layout.findViewById(R.id.fragment_cadastrar_txtview_cadastar);
                aux = textView_cadastrar.getText().toString();
                aux += " " + oferta_ou_demanda;
                textView_cadastrar.setText(aux);

                textView_validade_da = (TextView) layout.findViewById(R.id.fragment_cadastrar_textView_validade_da);
                aux = textView_validade_da.getText().toString();
                aux += " " + oferta_ou_demanda + " 1/7 dias.";
                textView_validade_da.setText(aux);


                seekbar_validade.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    String aux1;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (progress == 0) {
                            seekBar.setProgress(1);
                            progress = 1;
                        }
                        aux1 = "Validade da";
                        aux1 += " " + oferta_ou_demanda + " " + progress + "/7 dias.";
                        textView_validade_da.setText(aux1);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                button_cadastrar.setOnClickListener(new View.OnClickListener() {
                    String nome_produto;
                    String qntd_produto;
                    int validade;

                    @Override
                    public void onClick(View v) {
                        nome_produto = editText_nome_produto.getText().toString();
                        qntd_produto = editText_qntd_produto.getText().toString();
                        validade = seekbar_validade.getProgress();

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar c = Calendar.getInstance();
                        c.add(c.DAY_OF_MONTH, validade);

                        String validade_str = dateFormat.format(c.getTime());
                        if (nome_produto.contentEquals("") || qntd_produto.contentEquals("")) {
                            Toast.makeText(v.getContext(), "Campos Vazios.", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                String[] userinfos = {"insert" + oferta_ou_demanda.toLowerCase(), nome_produto, qntd_produto, validade_str, Cliente.getId(v.getContext()) + ""};
                                BackgroundTask backgroundTask = new BackgroundTask(v.getContext());
                                backgroundTask.execute(userinfos);
                                backgroundTask.get();
                                String result = backgroundTask.getStr_result();
                                result = result.trim();
                                if (result.contentEquals("0")) {
                                    Toast.makeText(v.getContext(), "Inserido com Sucesso.", Toast.LENGTH_LONG).show();
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.v(TAG, "run: a");

                                        }
                                    }, 2000);
                                    Activity host = getActivity(v);
                                    host.finish();
                                    host.startActivity(new Intent(v.getContext(), TelaCadastroActivity.class));

                                } else {
                                    Toast.makeText(v.getContext(), "Sem acesso à Internet.", Toast.LENGTH_LONG).show();
                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });


            }
            return layout;
        }


    }

}
