package br.com.thiago.robotPi.service;



import br.com.thiago.robotPi.dto.EmpresaSync;
import br.com.thiago.robotPi.dto.RaspberrySync;
import br.com.thiago.robotPi.dto.UserSync;
import br.com.thiago.robotPi.model.Dispositivo;
import br.com.thiago.robotPi.model.Empresa;
import br.com.thiago.robotPi.model.Raspberry;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public interface RaspberryService {

    /*@POST("user")
    Call<Void> insereOuAltera(@Body User user);*/

    @GET("raspberry")
    Call<RaspberrySync> lista(@Header("idEmpresa") String idEmpresa);

    @POST("raspberry/conexao")
    Call<Void> conectarRaspberry(@Body Raspberry raspberry);

    @POST("raspberry/desconexao")
    Call<Void> desconectar(@Body Dispositivo dispositivo);

    /*@DELETE("user/{id}")
    Call<Void> deleta(@Path("id") String id);*/

    /*@GET("user/diff")
    Call<UserSync> novos(@Header("datahora") String versao);*/

    /*@PUT("user/lista")
    Call<UserSync> atualiza(@Body List<User> users);*/

    /*@GET(".")
    Call<UserSync> findID(@Header("email") String email);*/
}
