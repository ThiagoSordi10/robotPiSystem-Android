package br.com.thiago.robotPi.helper;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.List;

import br.com.thiago.robotPi.activity.LoginActivity;
import br.com.thiago.robotPi.config.ConfiguracaoFirebase;
//import br.com.thiago.robotPi.dao.UserDAO;
import br.com.thiago.robotPi.dao.EmpresaDAO;
import br.com.thiago.robotPi.dao.UserDAO;
import br.com.thiago.robotPi.dto.UserSync;
import br.com.thiago.robotPi.model.User;
import br.com.thiago.robotPi.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioHelper {
    private User user = new User();
    private Context context;

    public UsuarioHelper(Context context){
        this.context = context;
    }

    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();
    }

    public static boolean atualizarNomeUsuario(String nome){

        try {

            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName( nome )
                    .build();

            user.updateProfile( profile ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if( !task.isSuccessful() ){
                        Log.d("Perfil", "Erro ao atualizar nome de perfil.");
                    }
                }
            });
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }


    }
}
