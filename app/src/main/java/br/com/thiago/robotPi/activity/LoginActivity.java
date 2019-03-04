package br.com.thiago.robotPi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import br.com.thiago.robotPi.R;
import br.com.thiago.robotPi.config.ConfiguracaoFirebase;
import br.com.thiago.robotPi.dao.EmpresaDAO;
import br.com.thiago.robotPi.dao.UserDAO;
import br.com.thiago.robotPi.dto.UserSync;
import br.com.thiago.robotPi.model.User;
import br.com.thiago.robotPi.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText campoEmail, campoSenha;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        campoEmail = findViewById(R.id.editLoginEmail);
        campoSenha = findViewById(R.id.editLoginSenha);


    }

    public void logarUsuario(User user){

        autenticacao.signInWithEmailAndPassword(
                user.getEmail(), user.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if( task.isSuccessful() ){
                    abrirTelaLoad();
                }else {

                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthInvalidUserException e ) {
                        excecao = "Usuário não está cadastrado.";
                    }catch ( FirebaseAuthInvalidCredentialsException e ){
                        excecao = "E-mail e senha não correspondem a um usuário cadastrado";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: "  + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void validarAutenticacaoUsuario(View view){

        //Recuperar textos dos campos
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();

        //Validar se e-mail e senha foram digitados
        if( !textoEmail.isEmpty() ){//verifica username
            if( !textoSenha.isEmpty() ){//verifica senha

                User user = new User();
                user.setEmail( textoEmail );
                user.setSenha( user.criptografaSenha(textoSenha)  );

                logarUsuario( user );

            }else {
                Toast.makeText(LoginActivity.this,
                        "Preencha a senha!",
                        Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(LoginActivity.this,
                    "Preencha o email!",
                    Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser userAtual = autenticacao.getCurrentUser();
        if ( userAtual != null ){
            abrirTelaLoad();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        FirebaseUser userAtual = autenticacao.getCurrentUser();
        if ( userAtual != null ){
            abrirTelaLoad();
        }
    }

    public void abrirTelaCadastro(View view){
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity( intent );
    }

    public void abrirTelaLoad(){
        Intent intent = new Intent(LoginActivity.this, LoadActivity.class);
        startActivity( intent );
    }

}
