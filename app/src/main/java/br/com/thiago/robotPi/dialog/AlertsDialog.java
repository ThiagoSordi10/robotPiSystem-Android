package br.com.thiago.robotPi.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;

import br.com.thiago.robotPi.activity.LoadActivity;
import br.com.thiago.robotPi.activity.LoginActivity;
import br.com.thiago.robotPi.dao.UserDAO;
import br.com.thiago.robotPi.tasks.RemoveUserDispositivoTask;

public class AlertsDialog {

    public AlertDialog.Builder alertaBuilder(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Erro de conexão");
        builder.setMessage("Não foi possivel a conexão com o servidor");
        return builder;
    }

    public void alertaDesativadoBuilder(final Context context, final FirebaseAuth autenticacao){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Permissão negada");
        builder.setMessage("Essa conta não está autorizada para uso");

        builder.setNeutralButton("Voltar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                UserDAO userDAO = new UserDAO(context);
                try {
                    if(userDAO.buscaUser() != null) {
                        userDAO.deleta();
                        new RemoveUserDispositivoTask(context).removeDispositivo();
                    }
                    autenticacao.signOut();
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        builder.create().show();
    }
}
