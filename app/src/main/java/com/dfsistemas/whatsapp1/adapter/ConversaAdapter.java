package com.dfsistemas.whatsapp1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dfsistemas.whatsapp1.Model.Conversa;
import com.dfsistemas.whatsapp1.R;
import com.dfsistemas.whatsapp1.helper.Preferencias;

import java.util.ArrayList;
import java.util.List;

public class ConversaAdapter extends ArrayAdapter<Conversa> {

    private ArrayList<Conversa> conversas;
    private Context context;


    public ConversaAdapter(@NonNull Context c, @NonNull ArrayList<Conversa> objects) {
        super(c, 0, objects);
        this.context = c;
        this.conversas = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view  = null;

        if(conversas != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_conversa, parent, false);

            TextView nome = view.findViewById(R.id.tv_titulo);
            TextView ultimaMensagem = view.findViewById(R.id.tv_subtitulo);

            Conversa conversa = conversas.get(position);


            nome.setText(conversa.getNome());
            ultimaMensagem.setText(conversa.getMensagem().substring(0, 20) + "...");

        }

        return view;

    }
}
