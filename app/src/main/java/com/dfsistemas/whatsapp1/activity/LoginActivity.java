package com.dfsistemas.whatsapp1.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.dfsistemas.whatsapp1.R;
import com.dfsistemas.whatsapp1.helper.EditTextPhoneMask;
import com.dfsistemas.whatsapp1.helper.Efeitos;
import com.dfsistemas.whatsapp1.helper.PermissionsHelper;
import com.dfsistemas.whatsapp1.helper.Preferencias;

import java.util.Random;


public class LoginActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener {

    private Button button_pais;
    private Button button_Proximo;
    private EditText editText_ddi;
    private EditText editText_telefone;
    private TextView textView_meuNumero;
    private TextView textView_tarifas;
    private ConstraintLayout constraintLayout;
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.INTERNET
    };
    private boolean permissaoCondedida = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        permissaoCondedida = PermissionsHelper.validarPermissoes(1,this, permissoesNecessarias);

        button_pais = findViewById(R.id.button_pais);
        button_Proximo = findViewById(R.id.button_proximo);
        editText_ddi = findViewById(R.id.editText_ddi);
        editText_telefone = findViewById(R.id.editText_telefone);
        textView_meuNumero = findViewById(R.id.textView_meuNumero);
        textView_tarifas = findViewById(R.id.textView_tarifas);
        constraintLayout = findViewById(R.id.constraintLayoutLogin);

        //Event Listeners
        textView_meuNumero.setOnClickListener(this);
        editText_telefone.setOnFocusChangeListener(this);
        editText_ddi.setOnFocusChangeListener(this);
        button_pais.setOnClickListener(this);
        textView_tarifas.setOnClickListener(this);
        constraintLayout.setOnClickListener(this);
        button_Proximo.setOnClickListener(this);

//        verificarCampos(editText_telefone);
        editText_telefone.addTextChangedListener(EditTextPhoneMask.insert(editText_telefone));
    }




    public void efeitoFocusEditText(View v) {
        if(v.hasFocus()) {
            v.setBackground(getResources().getDrawable(R.drawable.login_botao_paises_hover));
            button_pais.setPressed(false);
            efeitoFocusButton(button_pais);
        }else {
            v.setBackground(getResources().getDrawable(R.drawable.login_botao_paises));
        }
    }

    public void efeitoFocusButton(View v) {
        if(v.isPressed()) {
            v.setBackground(getResources().getDrawable(R.drawable.login_botao_paises_hover));
            editText_telefone.clearFocus();
            editText_ddi.clearFocus();
        }else {
            v.setBackground(getResources().getDrawable(R.drawable.login_botao_paises));
        }
    }

    public void cadastrar() {
        String ddi = editText_ddi.getText().toString().trim();
        String telefone = ddi + editText_telefone.getText().toString().replace("(","").replace(")","").replace("-","").replace(" ","");

        //Gerar Token
        Random random = new Random();
        int numeroRandomico = random.nextInt( 9999 - 1000) + 1000;
        String token = String.valueOf(numeroRandomico);
        String mensagemEnvio = "Whatsapp Código de Confirmação: " + token;
        Preferencias preferencias = new Preferencias(this);
        preferencias.salvarUsuarioPreferencias(telefone, token);
        //Envia SMS

        telefone = "5554";
        boolean enviadoSMS = enviaSMS(telefone, mensagemEnvio);

        if(enviadoSMS) {
            Intent intent = new Intent(this, ValidacaoActivity.class);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(this, "Problema ao enviar o SMS. Tente novamente!!", Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean enviaSMS(String telefone, String mensagem) {
        try {
            if(permissaoCondedida) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(telefone, null, mensagem, null, null);
                return true;
            }else {
                PermissionsHelper.validarPermissoes(1,this, permissoesNecessarias);
                return false;
            }

        }catch (Exception e) {
            e.printStackTrace();
            PermissionsHelper.validarPermissoes(1,LoginActivity.this, permissoesNecessarias);
            return false;
        }
    }





    public void fecharTeclado() {
        String telefone = editText_telefone.getText().toString().replace("(","").replace(")","").replace("-","").replace(" ","");
        if(telefone.length() >= 11) {
            Efeitos.fecharTeclado(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_pais:
                efeitoFocusButton(view);
                break;
            case R.id.textView_tarifas:
                break;
            case R.id.constraintLayoutLogin:
                break;
            case R.id.button_proximo:
                cadastrar();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.editText_telefone:
                efeitoFocusEditText(view);
                fecharTeclado();
                break;
            case R.id.editText_ddi:
                efeitoFocusEditText(view);
                break;
        }
    }
}
