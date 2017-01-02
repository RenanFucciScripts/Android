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

import com.book.homelist.ActivityAddItem;
import com.book.homelist.R;
import com.book.homelist.adapter.ItensListaAdapter;
import com.book.homelist.adapter.ItensPorListaHolder;
import com.book.homelist.pojo.Grupo;
import com.book.homelist.pojo.ItensPorGrupo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.book.homelist.ListItemsActivity.EXTRA_GRUPO;

public class ListItensFragmentList extends Fragment {

    private Grupo grupo;
    private RecyclerView mRecyclerItens;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseDatabase database;
    private DatabaseReference gruposRef;
    private DatabaseReference itensListaRef;
    private FirebaseRecyclerAdapter<ItensPorGrupo, ItensPorListaHolder> mAdapter;

    private LinearLayoutManager mManager;
    private FirebaseAuth mAuth;


    public static ListItensFragmentList newInstance() {
        return new ListItensFragmentList();
    }

    public ListItensFragmentList() {
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        gruposRef = database.getReference("grupos");
        itensListaRef = gruposRef.child(grupo.getId()).child("itens");

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://homelist-b4559.appspot.com").child("imagens");
        itensListaRef.keepSynced(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_itens, container, false);

        mRecyclerItens = (RecyclerView) view.findViewById(R.id.listaItens);

        grupo = (Grupo) getActivity().getIntent().getSerializableExtra(EXTRA_GRUPO);

        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityAddItem.class);
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

        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);

        mRecyclerItens.setLayoutManager(mManager);

        mAdapter = new ItensListaAdapter(itensListaRef, new ItensListaAdapter.ItensPorListaClickListener() {
            @Override
            public void onItemClicked(ItensPorGrupo itensPorGrupo) {

                if (itensPorGrupo.isCompradoStatus()) {
                    itensListaRef.child(itensPorGrupo.getId()).child("compradoStatus").setValue(false);
                    Toast.makeText(getActivity(), "Item desmarcado.", Toast.LENGTH_LONG).show();
                } else {
                    itensListaRef.child(itensPorGrupo.getId()).child("compradoStatus").setValue(true);
                    Toast.makeText(getActivity(), "Item comprado com sucesso.", Toast.LENGTH_LONG).show();
                }



            }
        }, getActivity());


        mRecyclerItens.setAdapter(mAdapter);
        //configuraSwipe();

        return view;
    }
}
