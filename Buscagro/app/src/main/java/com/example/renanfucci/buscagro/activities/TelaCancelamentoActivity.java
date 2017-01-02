package com.example.renanfucci.buscagro.activities;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.concurrent.ExecutionException;

public class TelaCancelamentoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context ctx;

    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    private static final String TAG = TelaCancelamentoActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cancelamento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mPager = (ViewPager) findViewById(R.id.tela_cancelamento_view_pager);
        mPager.setAdapter(new MyPageAdapter(getSupportFragmentManager()));

        mTabs = (SlidingTabLayout) findViewById(R.id.tela_cancelamento_tab_layout);
        mTabs.setDistributeEvenly(true);
        mTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorInverse, null));
        mTabs.setViewPager(mPager);

        Drawable drawable = IO.getNavHeaderImg(this);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_tela_cancelamento);
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
            startActivity(new Intent(this, TelaInicialActivity.class));

        } else if (id == R.id.nav_cadastrar) {
            startActivity(new Intent(this, TelaCadastroActivity.class));

        } else if (id == R.id.nav_consultar) {
            startActivity(new Intent(this, TelaConsultaActivity.class));
        } else if (id == R.id.nav_cancelar) {

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


    class MyPageAdapter extends FragmentPagerAdapter {

        String[] tabs;

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

        private TextView textView;
        private TextView textview_cancelar;
        private LinearLayout linearLayout;
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
        public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.fragment_cancelamento, container, false);
            try {
                Bundle bundle = getArguments();
                oferta_ou_demanda = getResources().getStringArray(R.array.tab_oferta_demanda)[bundle.getInt("position")];
                textview_cancelar = (TextView) layout.findViewById(R.id.fragment_cancelar_textview_cancelar);
                linearLayout = (LinearLayout) layout.findViewById(R.id.fragment_cancelar_linearlayout);
                View child;

                if (bundle != null) {

                    final String[] userinfos = {"selectuser" + oferta_ou_demanda.toLowerCase() + "s", Cliente.getId(container.getContext()) + ""};

                    final BackgroundTask backgroundTask = new BackgroundTask(container.getContext());
                    backgroundTask.execute(userinfos);
                    backgroundTask.get();

                    String result = backgroundTask.getStr_result();
                    if (result.contentEquals("-1")) {
                        Toast.makeText(container.getContext(), "Sem acesso à Internet.", Toast.LENGTH_LONG).show();
                    } else {
                        textview_cancelar.setText("Cancelar " + oferta_ou_demanda + "s");

                        JsonParser parser = new JsonParser();
                        JsonArray jsonarray = parser.parse(result).getAsJsonArray();
                        for (int i = 0; i < jsonarray.size(); i++) {

                            JsonObject x = jsonarray.get(i).getAsJsonObject();
                            String nome = x.get(oferta_ou_demanda.toLowerCase() + "_nome").getAsString();
                            String qntd = x.get(oferta_ou_demanda.toLowerCase() + "_qntd").getAsString();
                            String validade = x.get(oferta_ou_demanda.toLowerCase() + "_validade").getAsString();
                            String id = x.get(oferta_ou_demanda.toLowerCase() + "_id").getAsString();


                            LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            RelativeLayout view_item = (RelativeLayout) vi.inflate(R.layout.fragment_item_user, null);

                            TextView view_nome = (TextView) view_item.findViewById(R.id.fragment_item_user_nome);
                            view_nome.setText("Produto: " + nome);
                            view_nome.setTextColor(Color.BLACK);

                            TextView view_qntd = (TextView) view_item.findViewById(R.id.fragment_item_user_qntd);
                            view_qntd.setText("Quantidade: " + qntd);
                            view_qntd.setTextColor(Color.BLACK);

                            TextView view_validade = ((TextView) view_item.findViewById(R.id.fragment_item_user_Validade));
                            view_validade.setText("Válido até: " + validade);
                            view_validade.setTextColor(Color.BLACK);

                            ImageView imageview = (ImageView) view_item.findViewById(R.id.fragment_item_user_imgview_del);
                            imageview.setTag(id);
                            imageview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        String id_del = v.getTag().toString();
                                        String[] usersinfo_del = {"delete" + oferta_ou_demanda.toLowerCase(), id_del};
                                        BackgroundTask backgroundTask1 = new BackgroundTask(v.getContext());
                                        backgroundTask1.execute(usersinfo_del);
                                        backgroundTask1.get();

                                        String str_result_del = backgroundTask1.getStr_result();
                                        str_result_del = str_result_del.trim();

                                        if (str_result_del.contentEquals("0")) {
                                            Toast.makeText(v.getContext(), "Cancelada com Sucesso!", Toast.LENGTH_LONG).show();
                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Activity ahosto = (Activity) container.getContext();
                                                    ahosto.finish();
                                                    ahosto.startActivity(new Intent(container.getContext(), TelaCancelamentoActivity.class));
                                                }
                                            }, 4000);
                                        } else {
                                            Toast.makeText(v.getContext(), "Sem acesso à Internet.", Toast.LENGTH_LONG).show();
                                        }

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });

                            LinearLayout.LayoutParams layouParams = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            layouParams.setMargins(24, 12, 24, 12);
                            linearLayout.addView(view_item, layouParams);
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return layout;
        }
    }

}
