package com.dfsistemas.whatsapp1.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.dfsistemas.whatsapp1.Model.Contato;
import com.dfsistemas.whatsapp1.Model.Usuario;
import com.dfsistemas.whatsapp1.R;
import com.dfsistemas.whatsapp1.adapter.TabAdapter;
import com.dfsistemas.whatsapp1.config.ConfiguracaoFirebase;
import com.dfsistemas.whatsapp1.helper.Base64Custom;
import com.dfsistemas.whatsapp1.helper.Preferencias;
import com.dfsistemas.whatsapp1.helper.SlidingTabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class InicioActivity extends AppCompatActivity {

    private DatabaseReference referenciaDatabase;
    private FirebaseAuth usuarioAutenticacao;
    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private String identificadorContato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        usuarioAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("WhatsApp");
        setSupportActionBar(toolbar);

        slidingTabLayout = findViewById(R.id.stl_tabs);
        viewPager = findViewById(R.id.vp_pagina);

        //Configurando sliding tabs
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorWhite));

        //Configurar Adapter
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_adicionar:
                abrirCadastroContato();
                return true;
//                break;
            case R.id.item_configuracoes:
                return true;
//                break;
            case R.id.item_pesquisa:
                return true;
//                break;
            case R.id.item_sair:
                deslogarUsuario();
                return true;
//                break;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void abrirCadastroContato() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(InicioActivity.this);

        //Configuração do Dialog
        alertDialog.setTitle("Novo contato");
        alertDialog.setMessage("E-mail do usuário");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(this);
        alertDialog.setView(editText);

        //Configurar bototes
        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String emailContato = editText.getText().toString();
                if(emailContato.isEmpty()) {
                    Toast.makeText(InicioActivity.this, "Preencha o e-mail", Toast.LENGTH_SHORT).show();
                }else {
                    //Verificar se o usuario ja esta cadastrado
                    identificadorContato = Base64Custom.codificarBase64(emailContato);

                    //Recuperar instancia do firebase
                    referenciaDatabase = ConfiguracaoFirebase.getFirebase().child("usuarios").child(identificadorContato);

                    referenciaDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() != null) {

                                //Recuperar dados do contato a set adicionado
                                Usuario usuarioContato = dataSnapshot.getValue(Usuario.class);

                                //Recuperar identificador usuario logado (base64)
                                Preferencias preferencias = new Preferencias(InicioActivity.this);
                                String identificadorUsuarioLogado = preferencias.getIdentificador();
//                                usuarioAutenticacao.getCurrentUser();
                                referenciaDatabase = ConfiguracaoFirebase.getFirebase();
                                referenciaDatabase = referenciaDatabase.child("contatos")
                                        .child(identificadorUsuarioLogado)
                                        .child(identificadorContato);


                                Contato contato = new Contato();
                                contato.setIdentificadorUsuario(identificadorContato);
                                contato.setEmail(usuarioContato.getEmail());
                                contato.setNome(usuarioContato.getNome());

                                referenciaDatabase.setValue(contato);


                            }else {
                                Toast.makeText(InicioActivity.this, "Usuário não possui cadastro", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialog.create();
        alertDialog.show();
    }

    private void deslogarUsuario() {
        usuarioAutenticacao.signOut();
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
