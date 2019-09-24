package com.dfsistemas.whatsapp1.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dfsistemas.whatsapp1.Model.Usuario;
import com.dfsistemas.whatsapp1.R;
import com.dfsistemas.whatsapp1.config.ConfiguracaoFirebase;
import com.dfsistemas.whatsapp1.helper.Base64Custom;
import com.dfsistemas.whatsapp1.helper.Preferencias;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends Activity {

    private EditText editText_email;
    private EditText editText_senha;
    private Button button_entrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    String identificadorUsuarioLogado;

    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verificarUsuarioLogado();

        editText_email = findViewById(R.id.editText_email);
        editText_senha = findViewById(R.id.editText_senha);
        button_entrar = findViewById(R.id.button_entrar);


        button_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario = new Usuario();
                usuario.setEmail(editText_email.getText().toString());
                usuario.setSenha(editText_senha.getText().toString());
                if(!usuario.getEmail().isEmpty() && !usuario.getSenha().isEmpty()) {
                    validarLogin();
                }else {
                    Toast.makeText(MainActivity.this, "Preencha corretamente!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verificarUsuarioLogado() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if(autenticacao.getCurrentUser() != null) {
            abrirTelaPrincipal();
        }
    }


    private void validarLogin() {
            autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
            autenticacao.signInWithEmailAndPassword(
                    usuario.getEmail(),
                    usuario.getSenha()
            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {


                        identificadorUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail());

                        firebase = ConfiguracaoFirebase.getFirebase()
                                .child("usuarios")
                                .child(identificadorUsuarioLogado);

                        valueEventListenerUsuario = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Usuario usuarioRecuperado = dataSnapshot.getValue(Usuario.class);

                                Preferencias preferencias = new Preferencias(MainActivity.this);
                                preferencias.salvarDados(identificadorUsuarioLogado, usuarioRecuperado.getNome());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };

                        firebase.addListenerForSingleValueEvent(valueEventListenerUsuario);




                        abrirTelaPrincipal();
                        Toast.makeText(MainActivity.this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainActivity.this, "Erro ao fazer login!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(this, InicioActivity.class);
        startActivity(intent);
    }




    public void abrirCadastroUsuario(View v) {
        startActivity(new Intent(this, CadastroUsuarioActivity.class));
    }
}
