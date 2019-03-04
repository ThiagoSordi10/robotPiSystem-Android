package br.com.thiago.robotPi.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


import br.com.thiago.robotPi.activity.MainActivity;
import br.com.thiago.robotPi.dao.DispositivoDAO;
import br.com.thiago.robotPi.model.Dispositivo;
import br.com.thiago.robotPi.model.User;


public class SalvaUserDispositivoTask extends AsyncTask<Void, Void, String> {
    private Context context;
    private ProgressDialog dialog;
    private User user;

    public SalvaUserDispositivoTask(Context context, User user) {

        this.context = context;
        this.user = user;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, "Aguarde", "Buscando User...", true, true);
    }


    @Override
    protected String doInBackground(Void... params) {
        Log.i("ME ACHA AQUI", "DIOS");
        salvaDispositivo();
        return "Bem-vindo "+user.getNome();
    }


    public void salvaDispositivo(){
        String tokenFCM = new DispositivoDAO(this.context).buscaDispositivo();
        new Dispositivo().enviaTokenEUserParaServidor(tokenFCM, user);

    }

    @Override
    protected void onPostExecute(String resposta) {
        dialog.dismiss();
        Toast.makeText(context, resposta, Toast.LENGTH_LONG).show();
    }

}

