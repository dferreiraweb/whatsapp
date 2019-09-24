package com.dfsistemas.whatsapp1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dfsistemas.whatsapp1.Model.Mensagem;
import com.dfsistemas.whatsapp1.R;
import com.dfsistemas.whatsapp1.helper.Preferencias;

import java.util.ArrayList;
import java.util.List;

public class MensagemAdapter extends ArrayAdapter<Mensagem> {

    private Context context;
    private ArrayList<Mensagem> mensagens;

    public MensagemAdapter(@NonNull Context c, @NonNull ArrayList<Mensagem> objects) {
        super(c, 0, objects);
        this.context = c;
        this.mensagens = objects;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        //Verifica se a lista está preenchida
        if(mensagens != null) {

            //Recupera dados do usuario remetente
            Preferencias preferencias = new Preferencias(context);
            String idUsuarioRemetente = preferencias.getIdentificador();

            //Inicializa objeto para montagem do layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //Recuperando mensagem
            Mensagem mensagem = mensagens.get(position);

            //Montar view a partir do xml
            if(idUsuarioRemetente.equals(mensagem.getIdUsuario())) {
                view = inflater.inflate(R.layout.item_mensagem_direita, parent, false);
            }else {
                view = inflater.inflate(R.layout.item_mensagem_esquerda, parent, false);
            }


            //Recupewra elemento para exibição
            TextView textoMensagem = view.findViewById(R.id.edit_mensagem);
            textoMensagem.setText(mensagem.getMensagem());
        }

        return view;
    }
}
