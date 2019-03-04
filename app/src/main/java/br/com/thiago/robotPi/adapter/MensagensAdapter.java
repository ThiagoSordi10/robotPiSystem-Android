package br.com.thiago.robotPi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.thiago.robotPi.R;
import br.com.thiago.robotPi.model.Mensagem;

import java.util.List;

public class MensagensAdapter extends RecyclerView.Adapter<MensagensAdapter.MyViewHolder> {

    private List<Mensagem> mensagens;
    private Context context;
    private static final int TIPO_USER     = 0;
    private static final int TIPO_SERVER  = 1;

    public MensagensAdapter(List<Mensagem> lista, Context c) {
        this.mensagens = lista;
        this.context = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View item = null;
        if ( viewType == TIPO_USER ){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_user, parent, false);
        }else if( viewType == TIPO_SERVER ){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_server, parent, false);
        }

        return new MyViewHolder(item);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Mensagem mensagem = mensagens.get( position );
        String msg = mensagem.getMensagem();

        if(mensagem.getMensagem().contains("Clique aqui") && mensagem.getComando().getExecutado() == 1){
            holder.link.setVisibility(View.VISIBLE);
            holder.mensagem.setVisibility(View.GONE);
            holder.link.setText(msg);
            holder.link.setClickable(true);
        }else{
            holder.mensagem.setVisibility(View.VISIBLE);
            holder.link.setVisibility(View.GONE);
            holder.mensagem.setText(msg);
            holder.mensagem.setClickable(false);
        }

        if(getItemViewType(position) == TIPO_USER){
            holder.nome.setText( mensagem.getUser().getNome() );
        }else{
            holder.nome.setText( mensagem.getRaspberry().getNome() );
        }



        //Esconder a imagem
        //holder.imagem.setVisibility(View.GONE);



    }

    public List<Mensagem> getMensagens(){
        return this.mensagens;
    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    @Override
    public int getItemViewType(int position) {

        Mensagem mensagem = mensagens.get( position );

        if ( mensagem.getEnvio() == 0 ){
            return TIPO_USER;
        }

        return TIPO_SERVER;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mensagem, nome, link;

        public MyViewHolder(View itemView) {
            super(itemView);

            mensagem = itemView.findViewById(R.id.textMensagemServer);
            nome = itemView.findViewById(R.id.textNomeExibicao);
            link = itemView.findViewById(R.id.textLinkServer);

        }
    }

}
