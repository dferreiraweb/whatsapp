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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class CadastroUsuarioActivity extends Activity {

    private EditText editText_nome;
    private EditText editText_email;
    private EditText editText_senha;
    private Button button_cadastrar;
    private Usuario usuario;


    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);


        editText_nome = findViewById(R.id.editText_nome);
        editText_email = findViewById(R.id.editText_email);
        editText_senha = findViewById(R.id.editText_senha);
        button_cadastrar = findViewById(R.id.button_cadastrar);


        button_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = editText_nome.getText().toString();
                String email = editText_email.getText().toString();
                String senha = editText_senha.getText().toString();
                usuario = new Usuario();
                usuario.setNome(nome);
                usuario.setEmail(email);
                usuario.setSenha(senha);
                cadastrarUsuario();
            }
        });
    }

    public void cadastrarUsuario() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if(usuario.getEmail() == null || usuario.getSenha() == null || usuario.getNome() == null) {
            Toast.makeText(this, "Preencha os campos corretamente", Toast.LENGTH_SHORT).show();
        }else {
            autenticacao.createUserWithEmailAndPassword(
                    usuario.getEmail(), usuario.getSenha()
            ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()) {
                        Toast.makeText(CadastroUsuarioActivity.this, "Sucesso ao cadastrar", Toast.LENGTH_SHORT).show();

                        FirebaseUser usuarioFirebase = task.getResult().getUser();
//                        usuario.setId(usuarioFirebase.getUid());
                        String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                        usuario.setId(identificadorUsuario);
                        usuario.salvar();

                        Preferencias preferencias = new Preferencias(CadastroUsuarioActivity.this);
                        preferencias.salvarDados(identificadorUsuario, usuario.getNome());

//                        autenticacao.signOut();
//                        finish();
                        abrirLoginUsuario();
                    }else {
                        String erroExcecao = "";
                        try {
                            throw task.getException();
                        }catch (FirebaseAuthWeakPasswordException e) {
                            erroExcecao = "Digite uma senha mais forte, contendo letras e numeros.";
                        }catch (FirebaseAuthInvalidCredentialsException e) {
                            erroExcecao = "O e-mail digitado é invalido, digite um novo e-mail";
                        }catch (FirebaseAuthUserCollisionException e) {
                            erroExcecao = "Esse e-mail já está em uso no App!";
                        }catch (Exception e) {
                            erroExcecao = "Erro ao efetuar o cadastro!";
                            e.printStackTrace();
                        }

                        Toast.makeText(CadastroUsuarioActivity.this, erroExcecao, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    public void abrirLoginUsuario() {
        Intent intent = new Intent(CadastroUsuarioActivity.this, InicioActivity.class);
        startActivity(intent);
        finish();
    }
}
