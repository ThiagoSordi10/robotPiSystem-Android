package br.com.thiago.robotPi.service;



import br.com.thiago.robotPi.dto.EmpresaSync;
import br.com.thiago.robotPi.dto.EstacaoSync;
import br.com.thiago.robotPi.dto.UserSync;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;


public interface EstacaoService {

    @GET("estacao")
    Call<EstacaoSync> lista(@Header("idEmpresa") String idEmpresa);

}
