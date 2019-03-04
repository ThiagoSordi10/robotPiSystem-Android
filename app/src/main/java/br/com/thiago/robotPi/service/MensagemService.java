package br.com.thiago.robotPi.service;

import br.com.thiago.robotPi.dto.MensagemSync;
import br.com.thiago.robotPi.model.Mensagem;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface MensagemService {

    @POST("mensagem")
    Call<MensagemSync> enviarMensagem(@Body Mensagem mensagem);

    @GET("mensagem/chat")
    Call<MensagemSync> getMensagens(@Header("idUser") String idUser,
                                    @Header("idRaspberry") String idRaspberry);
}
