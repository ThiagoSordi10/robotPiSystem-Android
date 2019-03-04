package br.com.thiago.robotPi.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import br.com.thiago.robotPi.R;
import br.com.thiago.robotPi.adapter.EstacoesAdapter;
import br.com.thiago.robotPi.dao.UserDAO;
import br.com.thiago.robotPi.dialog.AlertsDialog;
import br.com.thiago.robotPi.dialog.LoadingDialog;
import br.com.thiago.robotPi.dto.EstacaoSync;
import br.com.thiago.robotPi.helper.CustomInterface;
import br.com.thiago.robotPi.helper.MaskEditUtil;
import br.com.thiago.robotPi.helper.RecyclerItemClickListener;
import br.com.thiago.robotPi.model.Comando;
import br.com.thiago.robotPi.model.Estacao;
import br.com.thiago.robotPi.model.User;
import br.com.thiago.robotPi.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    private EstacoesAdapter adapter;
    private List<Estacao> listaEstacoes = new ArrayList<>();;
    private RecyclerView recyclerViewComando;
    private TextView tituloBottomSheet;
    private TextInputEditText dataSaida, horarioSaida;
    private ImageButton botaoComandoPronto;
    private User user;
    private Comando comando;
    private CustomInterface customInterface;
    private LinearLayout linearLayoutComandoEnvio;
    private  LinearLayoutManager linearLayoutManager;

    public BottomSheetFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public BottomSheetFragment(CustomInterface customInterface){
        this.customInterface = customInterface;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);


        recyclerViewComando = view.findViewById(R.id.recyclerComando);
        tituloBottomSheet = view.findViewById(R.id.tituloBottomSheet);
        dataSaida = view.findViewById(R.id.dataSaida);
        horarioSaida = view.findViewById(R.id.horarioSaida);
        botaoComandoPronto = view.findViewById(R.id.botaoComandoPronto);
        linearLayoutComandoEnvio = view.findViewById(R.id.linearLayoutComandoEnvio);

        comando = new Comando();

        tituloBottomSheet.setText("Selecionar estação saída");

        adapter = new EstacoesAdapter(listaEstacoes, getActivity());

        user = new UserDAO(getActivity()).buscaUser();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewComando.setLayoutManager( layoutManager );
        recyclerViewComando.setHasFixedSize(true);
        recyclerViewComando.setAdapter( adapter );

        botaoComandoPronto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = dataSaida.getText().toString();
                String hora = horarioSaida.getText().toString();
                if(!data.isEmpty() && !hora.isEmpty()) {
                    String horario = data + " " + hora;
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                    try {
                        Date date = df.parse(horario);
                        comando.setHorarioSaida(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    Date horario = new Date();
                    comando.setHorarioSaida(horario);
                    comando.setExecutado(2); //Executando - Para executar agora
                }
                customInterface.metodoCallback(comando);
                dismiss();
            }
        });

        recyclerViewComando.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewComando,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                List<Estacao> listaEstacoesAtualizada = adapter.getEstacoes();
                                Estacao estacaoSelecionada = listaEstacoesAtualizada.get( position );
                                if(comando.getEstacaoSaida() == null){
                                    comando.setEstacaoSaida(estacaoSelecionada);
                                    listaEstacoes.remove(estacaoSelecionada);
                                    tituloBottomSheet.setText("Selecionar estação chegada");
                                    recarregarOpcoes();
                                } else if(comando.getEstacaoChegada() == null){
                                    comando.setEstacaoChegada(estacaoSelecionada);
                                    listaEstacoes.remove(estacaoSelecionada);
                                    tituloBottomSheet.setText("Selecionar horário de saída");
                                    selecionarHorario();
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


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        dataSaida.addTextChangedListener(MaskEditUtil.mask(dataSaida, MaskEditUtil.FORMAT_DATE));
        horarioSaida.addTextChangedListener(MaskEditUtil.mask(horarioSaida, MaskEditUtil.FORMAT_HOUR));
        recuperarEstacoes();
    }

    public void selecionarHorario(){
        dataSaida.setVisibility(View.VISIBLE);
        horarioSaida.setVisibility(View.VISIBLE);
        linearLayoutComandoEnvio.setVisibility(View.VISIBLE);
        recyclerViewComando.setVisibility(View.GONE);
    }

    public Comando getComando(){
        return this.comando;
    }

    public void recarregarOpcoes(){
        adapter = new EstacoesAdapter(listaEstacoes, getActivity());
        recyclerViewComando.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void recuperarEstacoes() {
        final ProgressDialog progressDialog = new LoadingDialog().createProgress(getContext(),
                "Buscando estações");
        progressDialog.show();
        Call<EstacaoSync> call = new RetrofitInicializador().getEstacaoService().lista(user.getEmpresa().getId());
        call.enqueue(new Callback<EstacaoSync>() {
            @Override
            public void onResponse(Call<EstacaoSync> call, Response<EstacaoSync> response) {
                Log.i("onResponse", "requisicao de busca de estações no servidor com sucesso");
                if (response.body().getEstacoes() == null) {
                    Estacao estacao = new Estacao();
                    listaEstacoes.add(estacao);
                } else {
                    List<Estacao> estacoes = response.body().getEstacoes();
                    Log.i("ESTACOES", ""+estacoes);
                    listaEstacoes = estacoes;
                    recarregarOpcoes();
                }
                progressDialog.dismiss();
                Collections.sort(listaEstacoes);

            }

            @Override
            public void onFailure(Call<EstacaoSync> call, Throwable t) {
                Log.e("onFailure", "" + t.getLocalizedMessage());
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertsDialog().alertaBuilder(getContext());
                builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });

                builder.create().show();
            }
        });
    }

}
