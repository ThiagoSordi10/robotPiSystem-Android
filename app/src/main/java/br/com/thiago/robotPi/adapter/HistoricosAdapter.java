package br.com.thiago.robotPi.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import br.com.thiago.robotPi.R;
import br.com.thiago.robotPi.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class HistoricosAdapter extends RecyclerView.Adapter<HistoricosAdapter.MyViewHolder> {

    private List<User> contatos;
    private Context context;

    public HistoricosAdapter(List<User> listaContatos, Context c) {
        this.contatos = listaContatos;
        this.context = c;
    }

    public List<User> getContatos(){
        return this.contatos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from( parent.getContext() ).inflate(R.layout.adapter_historico, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        User user = contatos.get( position );
        boolean cabecalho = user.getEmail().isEmpty();

        holder.nome.setText( user.getNome() );
        holder.user.setText( user.getEmail() );

        if( user != null ){
            Uri uri = null;
            Glide.with( context ).load( uri ).into( holder.icone );
        }else {
            if( cabecalho ){
                holder.icone.setImageResource( R.drawable.icone_grupo );
                holder.user.setVisibility( View.GONE );
            }else {
                holder.icone.setImageResource( R.drawable.padrao );
            }

        }

    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView icone;
        TextView nome, user, horario;

        public MyViewHolder(View itemView) {
            super(itemView);

            icone = itemView.findViewById(R.id.imageViewIcone);
            nome = itemView.findViewById(R.id.textNomeRaspberry);
            user = itemView.findViewById(R.id.textNomeUser);
            horario = itemView.findViewById(R.id.textHorario);


        }
    }

}
