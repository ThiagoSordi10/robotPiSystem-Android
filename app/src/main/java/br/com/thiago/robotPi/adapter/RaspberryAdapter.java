package br.com.thiago.robotPi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.thiago.robotPi.R;
import br.com.thiago.robotPi.model.Raspberry;
import br.com.thiago.robotPi.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class RaspberryAdapter extends RecyclerView.Adapter<RaspberryAdapter.MyViewHolder> {

    private List<Raspberry> raspberries;
    private Context context;

    public RaspberryAdapter(List<Raspberry> lista, Context c) {
        this.raspberries = lista;
        this.context = c;
    }

    public List<Raspberry> getRaspberries(){
        return this.raspberries;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_raspberries, parent, false );
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Raspberry raspberry = raspberries.get( position );
        holder.nome.setText( raspberry.getNome() );
        holder.icone.setImageResource(R.drawable.padrao);

        if ( raspberry.getDispositivo() != null ){
            holder.itemView.setClickable(false);
            holder.itemView.setEnabled(false);
            holder.itemView.setAlpha(.5f);
            User user = raspberry.getDispositivo().getUser();
            holder.user.setText( user.getNome() );
        }else{
            holder.user.setText("Sem usuário");
            holder.itemView.setClickable(true);
            holder.itemView.setAlpha(1f);
        }
//        else if( raspberry.getEndereco() == null){
//            holder.itemView.setClickable(false);
//            holder.itemView.setAlpha(.5f);
//            holder.user.setText("Dispositivo desconectado");
//        }





    }

    @Override
    public int getItemCount() {
        return raspberries.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView icone;
        TextView nome, user;

        public MyViewHolder(View itemView) {
            super(itemView);

            icone = itemView.findViewById(R.id.imageViewIcone);
            nome = itemView.findViewById(R.id.textNomeRaspberry);
            user = itemView.findViewById(R.id.textNomeUser);

        }
    }

}
