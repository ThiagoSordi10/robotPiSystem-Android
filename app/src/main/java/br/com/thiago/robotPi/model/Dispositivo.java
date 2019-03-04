package br.com.thiago.robotPi.model;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import br.com.thiago.robotPi.config.ConfiguracaoFirebase;
import br.com.thiago.robotPi.helper.UsuarioHelper;
import br.com.thiago.robotPi.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@JsonIgnoreProperties(ignoreUnknown = true)//Dados a mais vindo do server não são sincronizados
public class Dispositivo implements Serializable {

    private String token;
    private User user;

    public Dispositivo(String token){
        this.token = token;
    }

    public Dispositivo(String token, User user){
        this.token = token;
        this.user = user;
    }

    public Dispositivo(){}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void enviaTokenEUserParaServidor(final String token, User user){
        Call<Void> call = new RetrofitInicializador().getDispositivoService().enviaTokenUser(new Dispositivo(token, user));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i("token enviado", token);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("token falhou", t.getMessage());
            }
        });

    }

    public void removeUserToken(final String token){
        Call<Void> call = new RetrofitInicializador().getDispositivoService().removeUserToken(token);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i("usuario deslogado", token);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("deslogamento falhou", t.getMessage());
            }
        });

    }
}
