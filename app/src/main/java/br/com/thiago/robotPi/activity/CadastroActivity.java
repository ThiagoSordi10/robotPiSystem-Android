package br.com.thiago.robotPi.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.ArrayList;
import java.util.List;

import br.com.thiago.robotPi.R;
import br.com.thiago.robotPi.adapter.EmpresaAdapter;
import br.com.thiago.robotPi.config.ConfiguracaoFirebase;
import br.com.thiago.robotPi.dao.EmpresaDAO;
import br.com.thiago.robotPi.dao.UserDAO;
import br.com.thiago.robotPi.dto.EmpresaSync;
import br.com.thiago.robotPi.helper.MaskEditUtil;
import br.com.thiago.robotPi.helper.UsuarioHelper;
import br.com.thiago.robotPi.model.Empresa;
import br.com.thiago.robotPi.model.User;
import br.com.thiago.robotPi.retrofit.RetrofitInicializador;
import br.com.thiago.robotPi.sinc.EmpresaSincronizador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText campoNome, campoSenha,
            campoTelefone, campoEmail, campoCEP;
    private Spinner empresa;
    private FirebaseAuth autenticacao;
    private List<Empresa> empresas = new ArrayList<>(), empresasAux = new ArrayList<>();
    private Empresa textoInicial;
    private EmpresaAdapter empresaAdapter;
    private int posicaoEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome  = findViewById(R.id.editNome);
        campoTelefone = findViewById(R.id.editTelefone);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        campoCEP = findViewById(R.id.editCEP);
        empresa = (Spinner)findViewById(R.id.spinnerEmpresa);


        Call<EmpresaSync> call = new RetrofitInicializador().getEmpresaService().lista();
        call.enqueue(new Callback<EmpresaSync>() {
            @Override
            public void onResponse(Call<EmpresaSync> call, Response<EmpresaSync> response) {
                Log.i("onResponse", "requisicao de GET de empresa sucedida");
                EmpresaSync empresaSync = response.body();
                empresas = empresaSync.getEmpresas();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("onFailure", "requisicao de GET de empresa falhou");
            }
        });

        textoInicial = new Empresa();
        textoInicial.setId("DEFAULT");
        textoInicial.setNome("Selecione sua empresa: ");
        empresasAux.add(textoInicial);
        empresaAdapter = new EmpresaAdapter(this, empresasAux);
        empresa.setAdapter(empresaAdapter);
    }

    @Override
    protected void onStart(){
        super.onStart();

        campoCEP.addTextChangedListener(MaskEditUtil.mask(campoCEP, MaskEditUtil.FORMAT_CEP));

        campoCEP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                empresasAux.clear();
                empresa.setAdapter(null);
                empresasAux.add(textoInicial);
                String texto = charSequence.toString();
                for(Empresa empresa : empresas){
                    if(empresa.getCep().equals(texto)){
                        empresasAux.add(empresa);
                    }
                }
                empresaAdapter.setEmpresas(empresasAux);
                empresa.setAdapter(empresaAdapter);


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        empresa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                posicaoEmpresa = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void cadastrarUsuario(final User user){

        Call call = new RetrofitInicializador().getUserService().insereOuAltera(user);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.i("onResponse", "requisicao de inserção no servidor com sucesso");
                insereAuth(user);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("onFailure", "requisicao de inserção no servidor falhou");
            }
        });

           }

    private void insereAuth(final User user) {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                user.getEmail(), user.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if ( task.isSuccessful() ){

                    Toast.makeText(CadastroActivity.this,
                            "Conta criada com sucesso "+user.getNome(),
                            Toast.LENGTH_SHORT).show();
                    UsuarioHelper.atualizarNomeUsuario( user.getNome() );
                    new EmpresaDAO(CadastroActivity.this).insere(user.getEmpresa());
                    new UserDAO(CadastroActivity.this).insere(user);
                    finish();

                }else {

                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }catch ( FirebaseAuthInvalidCredentialsException e){
                        excecao= "Por favor, digite um e-mail válido";
                    }catch ( FirebaseAuthUserCollisionException e){
                        excecao = "Este conta já foi cadastrada";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: "  + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    public void validarCadastroUsuario(View view){

        Log.i("Item", ""+empresaAdapter.getItem(posicaoEmpresa));

        //Recuperar textos dos campos
        String textoNome  = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();
        String textoTelefone = campoTelefone.getText().toString();
        String textoCEP = campoCEP.getText().toString();
        Empresa empresaUser = (Empresa) empresaAdapter.getItem(posicaoEmpresa);

        if( !textoNome.isEmpty() ){//verifica nome
            if( !textoEmail.isEmpty() ){//verifica e-mail
                if ( !textoSenha.isEmpty() && textoSenha.length() >= 6 ){
                    if( !textoTelefone.isEmpty()){
                        if( !textoCEP.isEmpty()){
                            if( empresaUser.getId() != "DEFAULT"){

                                User user = new User();
                                user.setNome( textoNome );
                                user.setEmail( textoEmail );
                                user.setSenha( user.criptografaSenha(textoSenha) );
                                user.setEmpresa(empresaUser);
                                user.setTelefone(textoTelefone);
                                user.setDesativado(1);

                                cadastrarUsuario( user );

                            } else{
                                Toast.makeText(CadastroActivity.this,
                                        "Escolha sua empresa!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else{
                            Toast.makeText(CadastroActivity.this,
                                    "Preencha o CEP de sua empresa!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else{
                        Toast.makeText(CadastroActivity.this,
                                "Preencha seu telefone!",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(CadastroActivity.this,
                            "Preencha a senha ou aumente o tamanho dela!",
                            Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(CadastroActivity.this,
                        "Preencha o email!",
                        Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(CadastroActivity.this,
                    "Preencha o nome!",
                    Toast.LENGTH_SHORT).show();
        }

    }


}
