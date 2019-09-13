package com.dfsistemas.whatsapp1.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dfsistemas.whatsapp1.R;
import com.dfsistemas.whatsapp1.helper.Preferencias;

import java.util.HashMap;

public class ValidacaoActivity extends Activity {

    private EditText editText_codigoValidacao;
    private Button button_validar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validacao);

        editText_codigoValidacao = findViewById(R.id.editText_codigo);
        button_validar = findViewById(R.id.button_validar);


        button_validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Recuperar dados das preferencias do usuario
                Preferencias preferencias = new Preferencias(ValidacaoActivity.this);
                HashMap<String, String> usuario = preferencias.getDadosUsuario();

                String tokenGerado = usuario.get("token");
                String tokenDigitado = editText_codigoValidacao.getText().toString();

                if(tokenDigitado.equals(tokenGerado)) {
                    //Token validado
                    Toast.makeText(ValidacaoActivity.this, "Token validado", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ValidacaoActivity.this, "Token não validado", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
