package com.book.homelist;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.widget.Toast;

import com.book.homelist.adapter.UsuariosAdapter;
import com.book.homelist.adapter.UsuariosHolder;
import com.book.homelist.pojo.Grupo;
import com.book.homelist.pojo.Usuario;
import com.book.homelist.pojo.UsuariosPorGrupo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.book.homelist.ListItemsActivity.EXTRA_GRUPO;

public class SearchAddUserActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private DatabaseReference gruposRef;

    private FirebaseRecyclerAdapter<Usuario, UsuariosHolder> mAdapter;

    private LinearLayoutManager mManager;
    private FirebaseAuth mAuth;
    private RecyclerView mRecyclerUsers;
    private Grupo grupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_add_user);

        initFirebase();

        mRecyclerUsers = (RecyclerView) findViewById(R.id.all_users_list);

        grupo = (Grupo) getIntent().getSerializableExtra(EXTRA_GRUPO);

        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);

        mRecyclerUsers.setLayoutManager(mManager);

        mAdapter = new UsuariosAdapter(usersRef, new UsuariosAdapter.UsuariosListener() {
            @Override
            public void onUsuarioClicked(final Usuario usuario) {
                final UsuariosPorGrupo usuariosPorGrupo = new UsuariosPorGrupo();
                usuariosPorGrupo.setChaveUsuario(usuario.getId());
                usuariosPorGrupo.setAdmin(false);

                gruposRef.child(grupo.getId()).child("usuarios-grupo").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(usuario.getId()).exists()) {
                            gruposRef.child(grupo.getId()).child("usuarios-grupo").child(usuario.getId()).removeValue();
                            Toast.makeText(SearchAddUserActivity.this, "Usuário removido.", Toast.LENGTH_LONG).show();
                            SearchAddUserActivity.this.finish();
                        } else {
                            gruposRef.child(grupo.getId()).child("usuarios-grupo").child(usuario.getId()).setValue(usuariosPorGrupo);
                            Toast.makeText(SearchAddUserActivity.this, "Usuário adicionado.", Toast.LENGTH_LONG).show();
                            SearchAddUserActivity.this.finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

        }, this, grupo.getId());


        mRecyclerUsers.setAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.txt_buscar));
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("usuarios");
        gruposRef = database.getReference("grupos");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //final ArrayList<Evento> resultList = filter(eventos, newText);
        //adapter.setEventosList(resultList);
        //updateList();
        return true;
    }

   /* private ArrayList<Evento> filter(ArrayList<Evento> eventos, String query) {
        query = query.toLowerCase();

        final ArrayList<Evento> filteredEventos = new ArrayList<>();
        for (Evento evento : eventos) {
            final String text = evento.getTitulo().toLowerCase();
            if (text.contains(query)) {
                filteredEventos.add(evento);
            }
        }
        return filteredEventos;
    }*/
}
