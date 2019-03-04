package br.com.thiago.robotPi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.thiago.robotPi.R;
import br.com.thiago.robotPi.model.Estacao;

public class EstacoesAdapter extends RecyclerView.Adapter<EstacoesAdapter.MyViewHolder> {
    private List<Estacao> estacoes;
    private Context context;

    public EstacoesAdapter(List<Estacao> estacoes, Context c) {
        this.estacoes = estacoes;
        this.context = c;
    }

    public List<Estacao> getEstacoes(){
        return this.estacoes;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_estacoes, parent, false );
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Estacao estacao = estacoes.get(position);
        holder.nomeEstacao.setText(estacao.getNome());
        holder.iconeEstacao.setImageResource(R.drawable.estacao);
    }

    @Override
    public int getItemCount() {
        return estacoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iconeEstacao;
        TextView nomeEstacao;

        public MyViewHolder(View itemView) {
            super(itemView);

            iconeEstacao = itemView.findViewById(R.id.imageViewIconeEstacao);
            nomeEstacao = itemView.findViewById(R.id.textNomeEstacao);

        }
    }
}
