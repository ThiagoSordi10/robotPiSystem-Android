package br.com.thiago.robotPi.dialog;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog {

    public ProgressDialog createProgress(Context context, String mensagem){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMax(100);
        progressDialog.setMessage(mensagem);
        progressDialog.setTitle("Carregando");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return progressDialog;
    }
}
