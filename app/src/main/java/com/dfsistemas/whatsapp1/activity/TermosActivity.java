package com.dfsistemas.whatsapp1.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dfsistemas.whatsapp1.R;
import com.dfsistemas.whatsapp1.helper.Preferencias;

public class TermosActivity extends Activity implements View.OnClickListener {

    private Button aceitarTermos;
    private Preferencias pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termos);

        aceitarTermos = findViewById(R.id.button_aceitarTermos);
        aceitarTermos.setOnClickListener(this);

        pref = new Preferencias(this);
        boolean termosAceitos = pref.salvarTemosAceitos();

        if(termosAceitos) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }


    public void onClickAceitarTermos() {
        Preferencias preferencias = new Preferencias(this);
        boolean termosAceitos = preferencias.salvarTemosAceitos();

        if(termosAceitos) {
            startActivity(new Intent(this, MainActivity.class));
            Toast.makeText(this, "Termos: " + preferencias.preferences.getBoolean("termosUso", false), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Por favor aceite os termos de uso", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_aceitarTermos:
                onClickAceitarTermos();
                break;
        }
    }
}
