package br.com.thiago.robotPi.retrofit;

import br.com.thiago.robotPi.service.MensagemService;
import br.com.thiago.robotPi.service.DispositivoService;
import br.com.thiago.robotPi.service.EmpresaService;
import br.com.thiago.robotPi.service.EstacaoService;
import br.com.thiago.robotPi.service.RaspberryService;
import br.com.thiago.robotPi.service.UserService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;


public class RetrofitInicializador {

    private final Retrofit retrofit;

    public RetrofitInicializador(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(interceptor );

        retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.7:8080/api/")
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public UserService getUserService() {
        return retrofit.create(UserService.class);
    }

    public DispositivoService getDispositivoService() {
        return retrofit.create(DispositivoService.class);
    }

    public EmpresaService getEmpresaService(){ return retrofit.create(EmpresaService.class);}

    public RaspberryService getRaspberryService(){ return retrofit.create(RaspberryService.class);}

    public MensagemService getMensagemService(){ return  retrofit.create(MensagemService.class);}

    public EstacaoService getEstacaoService(){ return retrofit.create(EstacaoService.class);}
}
