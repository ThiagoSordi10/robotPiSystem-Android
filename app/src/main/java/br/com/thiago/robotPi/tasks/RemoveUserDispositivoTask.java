package br.com.thiago.robotPi.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import br.com.thiago.robotPi.dao.DispositivoDAO;
import br.com.thiago.robotPi.model.Dispositivo;
import br.com.thiago.robotPi.model.User;


public class RemoveUserDispositivoTask extends AsyncTask<Void, Void, String> {
    private Context context;
    private ProgressDialog dialog;

    public RemoveUserDispositivoTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, "Aguarde", "Removendo User do dispositivo...", true, true);
    }


    @Override
    protected String doInBackground(Void... params) {
        removeDispositivo();
        return null;
    }


    public void removeDispositivo(){
        String tokenFCM = new DispositivoDAO(this.context).buscaDispositivo();
        new Dispositivo().removeUserToken(tokenFCM);

    }

    @Override
    protected void onPostExecute(String resposta) {
        dialog.dismiss();
        Toast.makeText(context, resposta, Toast.LENGTH_LONG).show();
    }

}

