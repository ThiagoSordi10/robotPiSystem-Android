package br.com.thiago.robotPi.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    private static final String USER_PREFERENCES = "br.com.thiago.robotPi.preferences.UserPreferences";
    private static final String VERSAO_DO_DADO = "versao_do_dado";
    private Context context;

    public UserPreferences(Context context) {
        this.context = context;
    }

    public void salvaVersao(String versao) { //salva versão para só recuperar dados não recebidos antes
        SharedPreferences preferences = context.getSharedPreferences(USER_PREFERENCES, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(VERSAO_DO_DADO, versao);
        editor.commit();
    }

    private SharedPreferences getSharedPreferences(){
        return context.getSharedPreferences(USER_PREFERENCES, context.MODE_PRIVATE);
    }

    public String getVersao() {
        SharedPreferences preferences = getSharedPreferences();
        return preferences.getString(VERSAO_DO_DADO, "");
    }

    public boolean haveVersion() {
        return !getVersao().isEmpty();
    }
}
