package br.com.thiago.robotPi.service;



import java.util.List;

import br.com.thiago.robotPi.dto.UserSync;
import br.com.thiago.robotPi.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface UserService {

    @POST("user")
    Call<Void> insereOuAltera(@Body User user);

    @GET("user/find")
    Call<UserSync> findUser(@Header("email") String email);
}
