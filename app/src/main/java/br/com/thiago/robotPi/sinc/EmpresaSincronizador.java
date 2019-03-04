package br.com.thiago.robotPi.sinc;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.thiago.robotPi.dto.EmpresaSync;
import br.com.thiago.robotPi.model.Empresa;
import br.com.thiago.robotPi.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmpresaSincronizador {
    private final Context context;


    public EmpresaSincronizador(Context context) {
        this.context = context;
    }

}
