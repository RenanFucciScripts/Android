package com.book.homelist.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.book.homelist.R;
import com.book.homelist.SearchAddUserActivity;
import com.book.homelist.adapter.UsuariosEmGrupoAdapter;
import com.book.homelist.adapter.UsuariosEmListaHolder;
import com.book.homelist.pojo.Grupo;
import com.book.homelist.pojo.Usuario;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.book.homelist.ListItemsActivity.EXTRA_GRUPO;


public class ListUsuariosFragmentList extends Fragment {

    private Grupo grupo;
    private RecyclerView mRecyclerItens;


    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private DatabaseReference grupoRef;
    private FirebaseRecyclerAdapter<Usuario, UsuariosEmListaHolder> mAdapter;

    private LinearLayoutManager mManager;
    private FirebaseAuth mAuth;


    public static ListUsuariosFragmentList newInstance() {
        return new ListUsuariosFragmentList();
    }

    public ListUsuariosFragmentList() {
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("usuarios");
        grupoRef = database.getReference("grupos");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_usuarios, container, false);

        mRecyclerItens = (RecyclerView) view.findViewById(R.id.list_usuarios);

        grupo = (Grupo) getActivity().getIntent().getSerializableExtra(EXTRA_GRUPO);

        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.btn_add_user);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchAddUserActivity.class);
                intent.putExtra(EXTRA_GRUPO, grupo);
                startActivity(intent);
            }
        });


        mRecyclerItens.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });

        initFirebase();


        grupoRef.child(grupo.getId()).child("admin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().toString().equals(mAuth.getCurrentUser().getUid())) {
                    fab.show();
                } else {
                    fab.hide();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);

        mRecyclerItens.setLayoutManager(mManager);

        mAdapter = new UsuariosEmGrupoAdapter(usersRef, new UsuariosEmGrupoAdapter.UsuariosEmGrupoListener() {
            @Override
            public void onUsuarioClickedListener(Usuario usuario) {
                Toast.makeText(getActivity(), usuario.getNomeUsuario(), Toast.LENGTH_LONG).show();
            }
        }, getActivity(), grupo.getId());

        mRecyclerItens.setAdapter(mAdapter);
        //configuraSwipe();
        return view;
    }
}
