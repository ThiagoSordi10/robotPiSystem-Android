package br.com.thiago.robotPi.fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import br.com.thiago.robotPi.R;
import br.com.thiago.robotPi.activity.ComandoActivity;
import br.com.thiago.robotPi.adapter.RaspberryAdapter;
import br.com.thiago.robotPi.dao.DispositivoDAO;
import br.com.thiago.robotPi.dao.UserDAO;
import br.com.thiago.robotPi.dialog.AlertsDialog;
import br.com.thiago.robotPi.dialog.LoadingDialog;
import br.com.thiago.robotPi.dto.RaspberrySync;
import br.com.thiago.robotPi.event.AtualizaRaspberriesEvent;
import br.com.thiago.robotPi.helper.RecyclerItemClickListener;
import br.com.thiago.robotPi.model.Dispositivo;
import br.com.thiago.robotPi.model.Raspberry;
import br.com.thiago.robotPi.model.User;
import br.com.thiago.robotPi.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RaspberryFragment extends Fragment {

    private RecyclerView recyclerViewRaspberry;
    private List<Raspberry> listaRaspberries = new ArrayList<>();
    private RaspberryAdapter adapter;
    private User user;
    private Dispositivo dispositivo;

    public RaspberryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_raspberry, container, false);

        //autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        user = new UserDAO(getActivity()).buscaUser();
        dispositivo = new DispositivoDAO(getActivity()).getDispositivo();

        recyclerViewRaspberry = view.findViewById(R.id.recyclerListaRaspberries);

        //Configurar adapter
        adapter = new RaspberryAdapter(listaRaspberries, getActivity());

        EventBus eventBus = EventBus.getDefault();
        eventBus.register(this);

        //Configurar recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewRaspberry.setLayoutManager( layoutManager );
        recyclerViewRaspberry.setHasFixedSize(true);
        recyclerViewRaspberry.setAdapter( adapter );

        //Configurar evento de clique
        recyclerViewRaspberry.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewRaspberry,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                List<Raspberry> listaRaspberriesAtualizada = adapter.getRaspberries();
                                Raspberry raspberrySelecionado = listaRaspberriesAtualizada.get( position );
                                if(raspberrySelecionado.getDispositivo() == null) {
                                    raspberrySelecionado.setDispositivo(dispositivo);
                                    conectar(raspberrySelecionado);
                                }


                                /*Intent i = new Intent(getActivity(), ComandoActivity.class);
                                i.putExtra("chatRaspberry", raspberrySelecionado.getId() );
                                startActivity( i );*/

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
       // desconectar();
        return view;
    }

    public void conectar(final Raspberry raspberry){
        final ProgressDialog progressDialog = new LoadingDialog().createProgress(getContext(),
                "Conectando ao "+raspberry.getNome());
        progressDialog.show();
        Call<Void> call = new RetrofitInicializador().getRaspberryService().conectarRaspberry(raspberry);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i("onResponse", "Conexão realizada");
                Intent intent = new Intent(getActivity(), ComandoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("raspberry", raspberry);
                intent.putExtras(bundle);
                startActivity(intent);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("onFailure", "Erro ao conectar");
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarRaspberries();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void atualizaRaspberriesEvent(AtualizaRaspberriesEvent event){
        // if(swipe.isRefreshing()) swipe.setRefreshing(false);
        listaRaspberries = event.getRaspberrySync().getRaspberries();
        recarregarRaspberry();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void pesquisarRaspberries(String texto){
        //Log.d("pesquisa",  texto );

        List<Raspberry> listaRaspberriesBusca = new ArrayList<>();

        for ( Raspberry raspberry : listaRaspberries){

            String nome = raspberry.getNome().toLowerCase();

            if( nome.contains( texto ) ){
                listaRaspberriesBusca.add(raspberry);
            }



        }

        adapter = new RaspberryAdapter(listaRaspberriesBusca, getActivity());
        recyclerViewRaspberry.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void recarregarRaspberry(){
        adapter = new RaspberryAdapter(listaRaspberries, getActivity());
        recyclerViewRaspberry.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void recuperarRaspberries(){
        final ProgressDialog progressDialog = new LoadingDialog().createProgress(getContext(),
                "Carregando drones....");
        progressDialog.show();
        Call<RaspberrySync> call = new RetrofitInicializador().getRaspberryService().lista(user.getEmpresa().getId());
        call.enqueue(new Callback<RaspberrySync>() {
            @Override
            public void onResponse(Call<RaspberrySync> call, Response<RaspberrySync> response) {
                Log.i("onResponse", "requisicao de busca de raspberries no servidor com sucesso");
                if(response.body().getRaspberries() == null){
                    Raspberry rasp = new Raspberry();
                    listaRaspberries.add(rasp);
                }else{
                    List<Raspberry> raspberries = response.body().getRaspberries();
                    listaRaspberries = raspberries;
                    recarregarRaspberry();
                }
                progressDialog.dismiss();
                Collections.sort(listaRaspberries);

            }
            @Override
            public void onFailure(Call<RaspberrySync> call, Throwable t) {
                Log.e("onFailure", ""+t.getLocalizedMessage());
                progressDialog.dismiss();
                alertaErroConexao();
            }
        });
    }

    public void alertaErroConexao(){
        AlertDialog.Builder builder = new AlertsDialog().alertaBuilder(getContext());
        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listaRaspberries.clear();
                recarregarRaspberry();
            }
        });

        builder.create().show();
    }

}
