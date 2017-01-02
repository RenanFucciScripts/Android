package com.book.homelist;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.book.homelist.adapter.GrupoHolder;
import com.book.homelist.adapter.ListaAdapter;
import com.book.homelist.dialog.CriarGrupoDialog;
import com.book.homelist.pojo.Grupo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private AlertDialog.Builder grupoDialog;
    private LinearLayoutManager mManager;

    private RecyclerView mRecyclerLista;

    private FirebaseDatabase database;
    private DatabaseReference gruposRef;
    private DatabaseReference usersRef;
    private FirebaseRecyclerAdapter<Grupo, GrupoHolder> mAdapter;
    private TextView txtSemGrupos;
    private ImageView imgSemGrupos;
    private ProgressDialog mProgress;
    private FloatingActionButton fabAddGrupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        grupoDialog = new AlertDialog.Builder(this);
        txtSemGrupos = (TextView) findViewById(R.id.txt_sem_grupos);
        imgSemGrupos = (ImageView) findViewById(R.id.img_sem_grupos);
        fabAddGrupo = (FloatingActionButton) findViewById(R.id.fab_add_grupo);

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    initFirebase();
                    initUI();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    finish();
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                }
                // ...
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void checkAdapterIsEmpty(int count) {
        if (count == 0) {
            imgSemGrupos.setVisibility(View.VISIBLE);
            txtSemGrupos.setVisibility(View.VISIBLE);
            txtSemGrupos.setText("Clique no + para adicionar uma lista");
        } else {
            imgSemGrupos.setVisibility(View.GONE);
            txtSemGrupos.setVisibility(View.GONE);
        }

    }


    private void setupRecyclerView() {

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                checkAdapterIsEmpty(itemCount);
            }
        });



    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void initUI() {

        //mProgress = ProgressDialog.show(this, null, "Carregando...");

        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);

        mRecyclerLista = (RecyclerView) findViewById(R.id.listaGrupos);
        //mRecyclerLista.setHasFixedSize(true);
        mRecyclerLista.setLayoutManager(mManager);

        mAdapter = new ListaAdapter(gruposRef, new ListaAdapter.GrupoClickListener() {
            @Override
            public void onGrupoClicked(Grupo grupo) {
                Intent intent = new Intent(MainActivity.this, ListItemsActivity.class);
                intent.putExtra(ListItemsActivity.EXTRA_GRUPO, grupo);
                startActivity(intent);
            }
        }, this);


        mRecyclerLista.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fabAddGrupo.hide();
                else if (dy < 0)
                    fabAddGrupo.show();
            }
        });

        mRecyclerLista.setAdapter(mAdapter);
        setupRecyclerView();
        configuraSwipe();
    }

    //[START firebase database connection]
    private void initFirebase() {
        database = FirebaseDatabase.getInstance();
        // gruposRef = database.getReference("grupos").child(mAuth.getCurrentUser().getUid());
        gruposRef = database.getReference("grupos");
        usersRef = database.getReference("usuarios");

        gruposRef.keepSynced(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out) {

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Logout")
                    .setMessage("Deseja realmente sair da aplicação?")
                    .setIcon(R.drawable.ic_input_black_24dp)
                    .setPositiveButton("sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseAuth.getInstance().signOut();
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

    private void configuraSwipe() {
        ItemTouchHelper.SimpleCallback swipe =
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(
                            RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(
                            RecyclerView.ViewHolder viewHolder,
                            int swipeDir) {

                        final int position =
                                viewHolder.getAdapterPosition();
                        final String grupoId = mAdapter.getRef(position).getKey();
                        Grupo grupo = mAdapter.getItem(position);

                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Excluir lista")
                                .setMessage("Deseja realmente deletar: " + grupo.getNomeGrupo())
                                .setIcon(R.drawable.ic_add_black_24dp)
                                .setPositiveButton("sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        gruposRef.child(grupoId).removeValue();
                                    }
                                }).setNegativeButton("não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                mAdapter.notifyDataSetChanged();
                            }
                        }).show();

                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipe);
        itemTouchHelper.attachToRecyclerView(mRecyclerLista);
    }

    public void addGroup(View view) {
        //Toast.makeText(this, "Adicionar Grupo", Toast.LENGTH_LONG).show();
        CriarGrupoDialog criarGrupo = new CriarGrupoDialog(this);
        criarGrupo.criarGrupoDialog();
    }
}
