package com.book.homelist.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.book.homelist.R;
import com.book.homelist.pojo.ItensPorGrupo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ItensListaAdapter extends FirebaseRecyclerAdapter<ItensPorGrupo, ItensPorListaHolder> {

    private ItensPorListaClickListener mListener;
    private Context mContext;

    private FirebaseDatabase database;
    private DatabaseReference gruposRef;
    private DatabaseReference usersRef;
    private DatabaseReference itensListaRef;


    public ItensListaAdapter(Query ref, ItensPorListaClickListener listener, Context context) {
        super(ItensPorGrupo.class, R.layout.lista_itens_adapter, ItensPorListaHolder.class, ref);
        mContext = context;
        mListener = listener;

        database = FirebaseDatabase.getInstance();
        // gruposRef = database.getReference("grupos").child(mAuth.getCurrentUser().getUid());
        /*gruposRef = database.getReference("grupos");
        usersRef = database.getReference("usuarios");
        itensListaRef = database.getReference("grupos").child(grupo.getId()).child("itens");*/
    }

    @Override
    protected void populateViewHolder(ItensPorListaHolder viewHolder, ItensPorGrupo model, int position) {
        viewHolder.setItensPorGrupo(model, mContext);
    }

    @Override
    public ItensPorListaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ItensPorListaHolder holder = super.onCreateViewHolder(parent, viewType);

        holder.itemView.findViewById(R.id.txt_icon_status).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    int position = holder.getAdapterPosition();
                    ItensPorGrupo itensPorGrupo = getItem(position);
                    itensPorGrupo.setId(getRef(position).getKey());
                    mListener.onItemClicked(itensPorGrupo);
                }
            }
        });

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    int position = holder.getAdapterPosition();
                    ItensPorGrupo itensPorGrupo = getItem(position);
                    itensPorGrupo.setId(getRef(position).getKey());
                    mListener.onItemClicked(itensPorGrupo);
                }
            }
        });*/

        return holder;
    }

    public interface ItensPorListaClickListener {
        void onItemClicked(ItensPorGrupo itensPorGrupo);
    }
}
