package br.com.thiago.robotPi.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import br.com.thiago.robotPi.R;
import br.com.thiago.robotPi.adapter.MensagensAdapter;
import br.com.thiago.robotPi.dao.UserDAO;
import br.com.thiago.robotPi.dialog.AlertsDialog;
import br.com.thiago.robotPi.dialog.LoadingDialog;
import br.com.thiago.robotPi.dto.MensagemSync;
import br.com.thiago.robotPi.event.AtualizaChatEvent;
import br.com.thiago.robotPi.fragment.BottomSheetFragment;
import br.com.thiago.robotPi.fragment.RaspberryFragment;
import br.com.thiago.robotPi.helper.CustomInterface;
import br.com.thiago.robotPi.helper.RecyclerItemClickListener;
import br.com.thiago.robotPi.model.Comando;
import br.com.thiago.robotPi.model.Mensagem;
import br.com.thiago.robotPi.model.Raspberry;
import br.com.thiago.robotPi.model.User;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import br.com.thiago.robotPi.retrofit.RetrofitInicializador;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComandoActivity extends AppCompatActivity implements CustomInterface {

    private TextView textViewNomeDrone;
    private CircleImageView circleIconeDrone;
    private RecyclerView recyclerMensagens;
    private EditText editMensagem;
    private Raspberry raspberry;
    private User user;
    private CustomInterface customInterface;
    private FloatingActionButton botaoEnviar;
    private MensagensAdapter adapter;
    private List<Mensagem> mensagens = new ArrayList<>();
    private Comando comando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comando);

        //Configurar toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        customInterface = this;

        //Configuracoes iniciais
        textViewNomeDrone = findViewById(R.id.textViewNomeDrone);
        circleIconeDrone = findViewById(R.id.circleIconeDrone);
        editMensagem = findViewById(R.id.editMensagem);
        recyclerMensagens = findViewById(R.id.recyclerMensagens);
        botaoEnviar = findViewById(R.id.fabEnviar);

        editMensagem.setClickable(true);
        editMensagem.setFocusable(false);

        //recupera dados do usuario remetente
        user = new UserDAO(ComandoActivity.this).buscaUser();

        //Recuperar dados do usuário destinatario
        Bundle bundle = getIntent().getExtras();
        raspberry = (Raspberry) bundle.getSerializable("raspberry");
        textViewNomeDrone.setText(raspberry.getNome());

        EventBus eventBus = EventBus.getDefault();
        eventBus.register(this);


        //Configuração adapter
        adapter = new MensagensAdapter(mensagens, getApplicationContext() );

        //Configuração recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMensagens.setLayoutManager( layoutManager );
        recyclerMensagens.setHasFixedSize( true );
        recyclerMensagens.setAdapter( adapter );

        recyclerMensagens.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        ComandoActivity.this,
                        recyclerMensagens,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                List<Mensagem> listaMensagensAtualizada = adapter.getMensagens();
                                Mensagem mensagemClicada = listaMensagensAtualizada.get( position );

                                if(mensagemClicada.getMensagem().contains("Clique aqui") &&
                                        mensagemClicada.getComando().getExecutado() == 1){
                                    mensagemClicada.getComando().setExecutado(2);
                                    mensagemClicada.getComando().setHorarioSaida(new Date());
                                    comando = mensagemClicada.getComando();
                                    editMensagem.setText("Autorizo a ação do drone");
                                }
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );

        botaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textoMensagem = editMensagem.getText().toString();

                if ( !textoMensagem.isEmpty() ){

                    preparaEnvioComandoEMensagem(textoMensagem);

                }else {
                    Toast.makeText(ComandoActivity.this,
                            "Digite uma mensagem para enviar!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void preparaEnvioComandoEMensagem(String msg){
        comando.setUser(user);
        comando.setRaspberry(raspberry);

        Mensagem mensagem = new Mensagem();
        mensagem.setUser(user);
        mensagem.setRaspberry(raspberry);
        mensagem.setComando(comando);
        mensagem.setEnvio(0);
        mensagem.setMensagem(msg);

        enviarMensagemEComando(mensagem);
        editMensagem.setText("Clique para montar seu comando");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//So funciona na thread principal
    public void atualizaChatEvent(AtualizaChatEvent event){//Esse metodo espera pelo evento
       // if(swipe.isRefreshing()) swipe.setRefreshing(false);
        mensagens.add(event.getMensagem());
        recarregarMensagens();
    }

    public void abrirFragment(View view){
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(customInterface);
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void enviarMensagem(View view){



    }

    private void enviarMensagemEComando(Mensagem mensagem){
        final ProgressDialog progressDialog = new LoadingDialog().createProgress(ComandoActivity.this,
                "Enviando comando");
        progressDialog.show();

        Call<MensagemSync> call = new RetrofitInicializador().getMensagemService().enviarMensagem(mensagem);
        call.enqueue(new Callback<MensagemSync>() {
            @Override
            public void onResponse(Call<MensagemSync> call, Response<MensagemSync> response) {
                Log.i("onResponse", "requisicao de envio de comandos no servidor com sucesso");
                mensagens.addAll(response.body().getMensagens());
                recarregarMensagens();
                progressDialog.dismiss();

            }
            @Override
            public void onFailure(Call<MensagemSync> call, Throwable t) {
                Log.e("onFailure", ""+t.getLocalizedMessage());
                progressDialog.dismiss();
            }
        });

        editMensagem.setText("");

    }


    @Override
    protected void onStart() {
        super.onStart();
        recuperarMensagens();
    }

    public void recarregarMensagens(){
        adapter = new MensagensAdapter(mensagens, getApplicationContext());
        recyclerMensagens.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if(mensagens.size() > 0) {
            recyclerMensagens.smoothScrollToPosition(mensagens.size() - 1);
        }
    }

        public void desconectar(){
            /*final ProgressDialog progressDialog = new LoadingDialog().createProgress(ComandoActivity.this,
                    "Configurando drones....");
            progressDialog.show();*/

            Call<Void> call = new RetrofitInicializador().getRaspberryService().desconectar(raspberry.getDispositivo());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.i("onResponse", "Desconexão feita");
                    raspberry.setDispositivo(null);
                    //progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("onFailure", "Erro ao desconectar");
                    //progressDialog.dismiss();
                }
            });
        }

    @Override
    protected void onResume() {
        super.onResume();
        if(raspberry.getDispositivo() == null){
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        desconectar();
    }

    private void recuperarMensagens(){
        final ProgressDialog progressDialog = new LoadingDialog().createProgress(ComandoActivity.this,
                "Recuperando mensagens");
        progressDialog.show();
        Call<MensagemSync> call = new RetrofitInicializador().getMensagemService().getMensagens(user.getId(), raspberry.getId());
        call.enqueue(new Callback<MensagemSync>() {
            @Override
            public void onResponse(Call<MensagemSync> call, Response<MensagemSync> response) {
                Log.i("onResponse", "requisicao de busca de mensagens no servidor com sucesso");
                if(response.body().getMensagens() == null){
                    mensagens.add(null);
                }else{
                    mensagens = response.body().getMensagens();
                    recarregarMensagens();
                }
                progressDialog.dismiss();
                //mensagens.add( mensagem );

            }
            @Override
            public void onFailure(Call<MensagemSync> call, Throwable t) {
                Log.e("onFailure", ""+t.getLocalizedMessage());
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertsDialog().alertaBuilder(ComandoActivity.this);
                builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(ComandoActivity.this, MainActivity.class);
                        startActivity( intent );
                    }
                });

                builder.create().show();
            }
        });

    }

    @Override
    public void metodoCallback(Comando comando) {
        this.comando = comando;
        if(comando.getExecutado() == 2){
            editMensagem.setText(comando.toString()+" .........\nExecutar agora");
        }else{
            editMensagem.setText(comando.toString()+" .........\n"+
                    comando.dataString());
        }

    }
}
