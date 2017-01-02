package com.book.homelist.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.book.homelist.R;
import com.book.homelist.pojo.Grupo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ListaAdapter extends FirebaseRecyclerAdapter<Grupo, GrupoHolder> {

    private GrupoClickListener mListener;
    private Context mContext;
    private FirebaseDatabase database;
    private DatabaseReference gruposRef;
    private DatabaseReference usersRef;
    private FirebaseAuth mAuth;

    public ListaAdapter(Query ref, GrupoClickListener listener, Context context) {
        super(Grupo.class, R.layout.adapter_lista_grupos, GrupoHolder.class, ref);
        mListener = listener;
        mContext = context;

        database = FirebaseDatabase.getInstance();
        // gruposRef = database.getReference("grupos").child(mAuth.getCurrentUser().getUid());
        gruposRef = database.getReference("grupos");
        usersRef = database.getReference("usuarios");

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void populateViewHolder(final GrupoHolder viewHolder, final Grupo model, int position) {
        viewHolder.setGrupo(model, mContext);
    }

    @Override
    public GrupoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final GrupoHolder holder = super.onCreateViewHolder(parent, viewType);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    int position = holder.getAdapterPosition();
                    Grupo grupo = getItem(position);
                    grupo.setId(getRef(position).getKey());
                    mListener.onGrupoClicked(grupo);
                }
            }
        });

        return holder;
    }

    public interface GrupoClickListener {
        void onGrupoClicked(Grupo grupo);
    }
}
