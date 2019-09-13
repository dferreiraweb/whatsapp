package com.dfsistemas.whatsapp1.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.HashMap;

public class Preferencias {

    private Context contexto;
    public SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String NOME_ARQUIVO = "whatsapp.preferencias";
    private int MODO_LEITURA = 0; //privado

    private String CHAVE_NOME = "nome";
    private String CHAVE_TELEFONE = "telefone";
    private String CHAVE_TOKEN = "token";
    private String CHAVE_TERMOS = "termosUso";



    public Preferencias (Context contextoParametro) {

        contexto = contextoParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODO_LEITURA);
        editor = preferences.edit();
    }

    public void salvarUsuarioPreferencias(String telefone, String token) {
        editor.putString(CHAVE_TELEFONE, telefone );
        editor.putString(CHAVE_TOKEN, token );
        editor.commit();
    }

    public HashMap<String, String> getDadosUsuario() {
        HashMap<String, String> dadosUsuario = new HashMap<>();

        dadosUsuario.put(CHAVE_NOME, preferences.getString(CHAVE_NOME, null));
        dadosUsuario.put(CHAVE_TELEFONE, preferences.getString(CHAVE_TELEFONE, null));
        dadosUsuario.put(CHAVE_TOKEN, preferences.getString(CHAVE_TOKEN, null));

        return dadosUsuario;
    }

    public boolean salvarTemosAceitos() {
        boolean termosAceitos = preferences.getBoolean(CHAVE_TERMOS, false);
        if(!termosAceitos){
            editor.putBoolean(CHAVE_TERMOS, true);
            editor.commit();
            return true;
        }else {
            Toast.makeText(contexto, "Termos já aceitos!", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

}
