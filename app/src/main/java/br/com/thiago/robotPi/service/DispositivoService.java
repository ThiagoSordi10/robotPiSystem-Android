package br.com.thiago.robotPi.service;

import br.com.thiago.robotPi.model.Dispositivo;
import br.com.thiago.robotPi.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DispositivoService {

    @POST("firebase/dispositivo")
    Call<Void> enviaTokenUser(@Body Dispositivo dispositivo);

    @DELETE("firebase/{token}")
    Call<Void> removeUserToken(@Path("token") String token);
}
