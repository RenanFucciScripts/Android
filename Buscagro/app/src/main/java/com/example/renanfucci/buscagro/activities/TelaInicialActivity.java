package com.example.renanfucci.buscagro.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.renanfucci.buscagro.R;
import com.example.renanfucci.buscagro.method.asyncTask.BackgroundTask;
import com.example.renanfucci.buscagro.method.io.IO;
import com.example.renanfucci.buscagro.method.io.RoundedImageView;
import com.example.renanfucci.buscagro.supportTab.SlidingTabLayout;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.concurrent.ExecutionException;

public class TelaInicialActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    private static final String TAG = TelaInicialActivity.class.getName();
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mPager = (ViewPager) findViewById(R.id.tela_inicial_view_pager);
        mPager.setAdapter(new MyPageAdapter(getSupportFragmentManager()));

        mTabs = (SlidingTabLayout) findViewById(R.id.tela_inicial_tab_layout);
        mTabs.setDistributeEvenly(true);
        mTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorInverse, null));
        mTabs.setViewPager(mPager);


        Drawable drawable = IO.getNavHeaderImg(this);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_tela_inicial);
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_cadastrar) {
            startActivity(new Intent(this, TelaCadastroActivity.class));
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
            ctx = this;
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


    class MyPageAdapter extends FragmentPagerAdapter {

        String[] tabs;

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
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

        private TextView textView;
        private String oferta_ou_demanda;
        private LinearLayout linearLayout;

        public static MyFragment getInstance(int position) {
            MyFragment myFragment = new MyFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            myFragment.setArguments(args);
            return myFragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.fragment_tela_inicial, container, false);
            textView = (TextView) layout.findViewById(R.id.fragment_inicial_titulo);
            Bundle bundle = getArguments();

            oferta_ou_demanda = getResources().getStringArray(R.array.tab_oferta_demanda)[bundle.getInt("position")];
            linearLayout = (LinearLayout) layout.findViewById(R.id.fragment_inicial_linearlayout);

            if (bundle != null) {


                textView.setText("Todas as " + oferta_ou_demanda + "s");

                try {
                    if (((LinearLayout) linearLayout).getChildCount() > 0)
                        ((LinearLayout) linearLayout).removeAllViews();


                    String[] str_params = {"selectall" + (oferta_ou_demanda.toLowerCase()) + "s"};
                    BackgroundTask task = new BackgroundTask(getContext());
                    task.execute(str_params);
                    task.get();
                    String str_result_consulta = task.getStr_result();

                    JsonParser parser = new JsonParser();
                    JsonArray jsonarray = parser.parse(str_result_consulta).getAsJsonArray();
                    for (int i = 0; i < jsonarray.size(); i++) {
                        JsonObject x = jsonarray.get(i).getAsJsonObject();
                        String nome = x.get(oferta_ou_demanda.toLowerCase() + "_nome").getAsString();
                        String qntd = x.get(oferta_ou_demanda.toLowerCase() + "_qntd").getAsString();
                        String validade = x.get(oferta_ou_demanda.toLowerCase() + "_validade").getAsString();
                        String user_id = x.get("user_id").getAsString();

                        String[] str_user_params = {"selectuserinfo", user_id};


                        BackgroundTask task_user = new BackgroundTask(getContext());
                        task_user.execute(str_user_params);
                        task_user.get();
                        String str_result_consulta_user = task_user.getStr_result();


                        JsonObject jsonobjuserinfo = parser.parse(str_result_consulta_user).getAsJsonArray().get(0).getAsJsonObject();

                        String str_user_nome = jsonobjuserinfo.get("user_nome").getAsString();
                        String str_user_email = jsonobjuserinfo.get("user_email").getAsString();
                        String str_user_tel = jsonobjuserinfo.get("user_tel").getAsString();


                        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        RelativeLayout view_item = (RelativeLayout) vi.inflate(R.layout.fragment_item_geral, null);

                        TextView view_nome = (TextView) view_item.findViewById(R.id.fragment_item_geral_produto_nome);
                        view_nome.setText("Produto: " + nome);
                        view_nome.setTextColor(Color.BLACK);

                        TextView view_qntd = (TextView) view_item.findViewById(R.id.fragment_item_geral_produto_qntd);
                        view_qntd.setText("Quantidade: " + qntd);
                        view_qntd.setTextColor(Color.BLACK);

                        TextView view_validade = (TextView) view_item.findViewById(R.id.fragment_item_geral_produto_validade);
                        view_validade.setText("Válido até: " + validade);
                        view_validade.setTextColor(Color.BLACK);

                        String produtor_consumedor = oferta_ou_demanda.contentEquals("Oferta") ? ("Produtor") : ("Consumidor");
                        TextView view_user_nome = (TextView) view_item.findViewById(R.id.fragment_item_geral_usuario_nome);
                        view_user_nome.setText(produtor_consumedor + ": " + str_user_nome);
                        view_user_nome.setTextColor(Color.BLACK);

                        TextView view_user_email = (TextView) view_item.findViewById(R.id.fragment_item_geral_usuario_email);
                        view_user_email.setText("E-mail: " + str_user_email);
                        view_user_email.setTextColor(Color.BLACK);

                        TextView view_user_tel = (TextView) view_item.findViewById(R.id.fragment_item_geral_usuario_tel);
                        view_user_tel.setText("Telefone: " + str_user_tel);
                        view_user_tel.setTextColor(Color.BLACK);

                        LinearLayout.LayoutParams layouParams = new LinearLayout.LayoutParams(
                                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        layouParams.setMargins(24, 24, 24, 24);
                        linearLayout.addView(view_item, layouParams);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            return layout;
        }
    }

}
