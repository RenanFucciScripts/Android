package com.book.homelist.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.book.homelist.R;
import com.book.homelist.pojo.Usuario;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class UsuariosAdapter extends FirebaseRecyclerAdapter<Usuario, UsuariosHolder> {

    private UsuariosListener mListener;
    private Context mContext;
    private FirebaseDatabase database;
    private DatabaseReference gruposRef;
    private DatabaseReference usersRef;
    private FirebaseAuth mAuth;
    private String grupoId;

    public UsuariosAdapter(Query ref, UsuariosListener listener, Context context, String grupoId) {
        super(Usuario.class, R.layout.adapter_usuarios, UsuariosHolder.class, ref);
        mListener = listener;
        mContext = context;
        this.grupoId = grupoId;

        database = FirebaseDatabase.getInstance();
        gruposRef = database.getReference("grupos");
        usersRef = database.getReference("usuarios");

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void populateViewHolder(final UsuariosHolder viewHolder, final Usuario model, int position) {
        viewHolder.setAddUser(model, mContext, grupoId);
    }

    @Override
    public UsuariosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final UsuariosHolder holder = super.onCreateViewHolder(parent, viewType);

        holder.itemView.findViewById(R.id.btn_add_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    int position = holder.getAdapterPosition();
                    Usuario usuario = getItem(position);
                    usuario.setId(getRef(position).getKey());
                    mListener.onUsuarioClicked(usuario);
                }
            }
        });

        return holder;
    }

    public interface UsuariosListener {
        void onUsuarioClicked(Usuario usuario);
    }
}
