package br.com.thiago.robotPi.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
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
import br.com.thiago.robotPi.dialog.AlertsDialog;
import br.com.thiago.robotPi.dto.UserSync;
import br.com.thiago.robotPi.model.User;
import br.com.thiago.robotPi.retrofit.RetrofitInicializador;
import br.com.thiago.robotPi.tasks.RemoveUserDispositivoTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Call<UserSync> call = new RetrofitInicializador().getUserService().findUser(autenticacao.getCurrentUser().getEmail());
        call.enqueue(new Callback<UserSync>() {
            @Override
            public void onResponse(Call<UserSync> call, Response<UserSync> response) {
                User user = response.body().getUser();
                if(user != null) {
                    Log.i("onResponse", "requisicao de GET de user sucedida");
                    new EmpresaDAO(LoadActivity.this).insere(user.getEmpresa());
                    new UserDAO(LoadActivity.this).insere(user);
                    Intent intent = new Intent(LoadActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    new AlertsDialog().alertaDesativadoBuilder(LoadActivity.this, autenticacao);
                }
            }

            @Override
            public void onFailure(Call<UserSync> call, Throwable t) {
                Log.e("onFailure", "requisicao de GET de user falhou");
                AlertDialog.Builder builder = new AlertsDialog().alertaBuilder(LoadActivity.this);

                builder.setPositiveButton("Tentar novamente", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(LoadActivity.this, LoadActivity.class);
                        startActivity( intent );
                    }
                });

                builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        UserDAO userDAO = new UserDAO(LoadActivity.this);
                        try {
                            if(userDAO.buscaUser() != null) {
                                userDAO.deleta();
                                new RemoveUserDispositivoTask(LoadActivity.this).removeDispositivo();
                            }
                            autenticacao.signOut();
                            Intent intent = new Intent(LoadActivity.this, LoginActivity.class);
                            startActivity( intent );
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
                builder.create().show();
            }
        });
    }

}
