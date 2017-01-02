package com.book.homelist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.book.homelist.fragments.ListItensFragmentList;
import com.book.homelist.fragments.ListUsuariosFragmentList;
import com.book.homelist.pojo.Grupo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListItemsActivity extends AppCompatActivity {

    public static final String EXTRA_GRUPO = "extraGrupo";
    private Grupo grupo;

    private FirebaseDatabase database;
    private DatabaseReference gruposRef;
    private DatabaseReference usersRef;
    private DatabaseReference itensListaRef;

    protected Toolbar toolbar;

    protected TabLayout mTabs;
    protected ViewPager mPager;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        grupo = (Grupo) getIntent().getSerializableExtra(EXTRA_GRUPO);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabs = (TabLayout) findViewById(R.id.tabs);
        mPager = (ViewPager) findViewById(R.id.viewpager);

        setupToolbar(grupo.getNomeGrupo());

        setupViewPager(mPager);

        mTabs.setupWithViewPager(mPager);

        initFirebase();

        mAuth = FirebaseAuth.getInstance();
    }

    private void initFirebase() {
        database = FirebaseDatabase.getInstance();
        // gruposRef = database.getReference("grupos").child(mAuth.getCurrentUser().getUid());
        gruposRef = database.getReference("grupos");
        usersRef = database.getReference("usuarios");
        itensListaRef = database.getReference("grupos").child(grupo.getId()).child("itens");

        itensListaRef.keepSynced(true);
    }

    public void setupToolbar(String titulo) {
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar toolbar = getSupportActionBar();


        if (toolbar != null) {
            toolbar.setDisplayHomeAsUpEnabled(true);
        }

        if (toolbar != null) {

            toolbar.setTitle(titulo);
            toolbar.setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        gruposRef.child(grupo.getId()).child("usuarios-grupo").child(mAuth.getCurrentUser().getUid()).child("admin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isAdmin = (boolean) dataSnapshot.getValue();

                if (!isAdmin) {
                    getMenuInflater().inflate(R.menu.menu_leave_grupo, menu);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.leave_group) {

            new AlertDialog.Builder(ListItemsActivity.this)
                    .setTitle("Deixar o grupo")
                    .setMessage("Deseja realmente deixar o grupo " + grupo.getNomeGrupo() + "?")
                    .setIcon(R.drawable.ic_input_black_24dp)
                    .setPositiveButton("sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            gruposRef.child(grupo.getId()).child("usuarios-grupo").child(mAuth.getCurrentUser().getUid()).removeValue();
                            Toast.makeText(ListItemsActivity.this, "Você saiu do grupo.", Toast.LENGTH_LONG).show();
                            ListItemsActivity.this.finish();
                        }
                    }).setNegativeButton("não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();

        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {

        String[] tabs = getResources().getStringArray(R.array.tabs);

        ListItensFragmentList listItensFragmentList = ListItensFragmentList.newInstance();
        ListUsuariosFragmentList listUsuariosFragmentList = ListUsuariosFragmentList.newInstance();


        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_GRUPO, grupo);

        listItensFragmentList.setArguments(bundle);
        listUsuariosFragmentList.setArguments(bundle);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(listItensFragmentList, tabs[0]);
        adapter.addFragment(listUsuariosFragmentList, tabs[1]);
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
