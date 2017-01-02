package com.book.homelist.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.book.homelist.R;
import com.book.homelist.pojo.ItensPorGrupo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class ItensPorListaHolder extends RecyclerView.ViewHolder {

    private TextView nomeItem;
    private TextView quantidadeItem;
    private TextView marcaItem;
    private ImageView imgItem;
    private ImageView statusItem;

    public ItensPorListaHolder(View view) {
        super(view);
        nomeItem = (TextView) view.findViewById(R.id.txt_item_nome);
        marcaItem = (TextView) view.findViewById(R.id.txt_item_marca);
        quantidadeItem = (TextView) view.findViewById(R.id.txt_quantidade);
        imgItem = (ImageView) view.findViewById(R.id.img_lista_item);
        statusItem = (ImageView) view.findViewById(R.id.txt_icon_status);


        statusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("STATUS", "STATUS_CLICKED!!!");
            }
        });
    }

    public void setItensPorGrupo(ItensPorGrupo itensPorGrupo, Context context) {
        nomeItem.setText(itensPorGrupo.getNomeItem());
        if (itensPorGrupo.getMarcaItem().isEmpty()) {
            marcaItem.setText(R.string.sem_marca);
        } else {
            marcaItem.setText(itensPorGrupo.getMarcaItem());
        }
        quantidadeItem.setText(String.valueOf(itensPorGrupo.getQuantidadeItem()).concat(" unidades"));


        Glide.with(context).
                load(itensPorGrupo.getImagemItem()).
                placeholder(R.drawable.default_placeholder).
                diskCacheStrategy(DiskCacheStrategy.ALL).
                into(imgItem);

        if (itensPorGrupo.isCompradoStatus()) {
            statusItem.setImageResource(R.drawable.ic_check_circle_black_24dp);
            nomeItem.setPaintFlags(nomeItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            quantidadeItem.setPaintFlags(nomeItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            marcaItem.setPaintFlags(nomeItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            statusItem.setImageResource(R.drawable.ic_local_grocery_store_black_24dp);
            //nomeItem.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
        }
    }
}