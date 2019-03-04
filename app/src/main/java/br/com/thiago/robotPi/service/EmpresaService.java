package br.com.thiago.robotPi.service;



import br.com.thiago.robotPi.dto.EmpresaSync;
import br.com.thiago.robotPi.dto.UserSync;
import br.com.thiago.robotPi.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface EmpresaService {

    /*@POST("user")
    Call<Void> insereOuAltera(@Body User user);*/

    @GET("empresa")
    Call<EmpresaSync> lista();

    /*@DELETE("user/{id}")
    Call<Void> deleta(@Path("id") String id);*/

    /*@GET("user/diff")
    Call<UserSync> novos(@Header("datahora") String versao);*/

    /*@PUT("user/lista")
    Call<UserSync> atualiza(@Body List<User> users);*/

    /*@GET(".")
    Call<UserSync> findID(@Header("email") String email);*/
}
