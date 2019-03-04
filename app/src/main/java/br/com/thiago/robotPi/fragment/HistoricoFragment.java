package br.com.thiago.robotPi.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import br.com.thiago.robotPi.R;
import br.com.thiago.robotPi.activity.ComandoActivity;
import br.com.thiago.robotPi.adapter.HistoricosAdapter;
import br.com.thiago.robotPi.config.ConfiguracaoFirebase;
import br.com.thiago.robotPi.helper.RecyclerItemClickListener;
import br.com.thiago.robotPi.helper.UsuarioHelper;
import br.com.thiago.robotPi.model.User;

import java.util.ArrayList;
import java.util.List;


public class HistoricoFragment extends Fragment {

    private RecyclerView recyclerViewListaHistoricos;
    private HistoricosAdapter adapter;
    private ArrayList<User> listaHistoricos = new ArrayList<>();
    private DatabaseReference usuariosRef;
    private ValueEventListener valueEventListenerContatos;
    private FirebaseUser usuarioAtual;

    public HistoricoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_historico, container, false);

        //Configurações iniciais
        recyclerViewListaHistoricos = view.findViewById(R.id.recyclerViewListaHistorico);
        usuariosRef = ConfiguracaoFirebase.getFirebaseDatabase().child("usuarios");
        usuarioAtual = UsuarioHelper.getUsuarioAtual();

        //configurar adapter
        adapter = new HistoricosAdapter(listaHistoricos, getActivity() );

        //configurar recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getActivity() );
        recyclerViewListaHistoricos.setLayoutManager( layoutManager );
        recyclerViewListaHistoricos.setHasFixedSize( true );
        recyclerViewListaHistoricos.setAdapter( adapter );

        //Configurar evento de clique no recyclerview
        recyclerViewListaHistoricos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewListaHistoricos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                List<User> listaUsuariosAtualizada  = adapter.getContatos();

                                User usuarioSelecionado = listaUsuariosAtualizada.get( position );
                                boolean cabecalho = usuarioSelecionado.getEmail().isEmpty();


                                Intent i = new Intent(getActivity(), ComandoActivity.class);
                                i.putExtra("chatContato", usuarioSelecionado );
                                startActivity( i );



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

        /*Define usuário com e-mail vazio
        * em caso de e-mail vazio o usuário será utilizado como
        * cabecalho, exibindo novo grupo */
        User itemGrupo = new User();
        itemGrupo.setNome("Novo grupo");
        itemGrupo.setEmail("");

        listaHistoricos.add( itemGrupo );


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarContatos();
    }

    @Override
    public void onStop() {
        super.onStop();
        usuariosRef.removeEventListener( valueEventListenerContatos );
    }

    public void recuperarContatos(){

        valueEventListenerContatos = usuariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for ( DataSnapshot dados: dataSnapshot.getChildren() ){

                    User user = dados.getValue( User.class );

                    String emailUsuarioAtual = usuarioAtual.getEmail();
                    if ( !emailUsuarioAtual.equals( user.getEmail() ) ){
                        listaHistoricos.add( user );
                    }


                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void pesquisarHistoricos(String texto){
        //Log.d("pesquisa",  texto );

        List<User> listaContatosBusca = new ArrayList<>();

        for ( User usuario : listaHistoricos ) {

            String nome = usuario.getNome().toLowerCase();
            if(nome.contains( texto )) {
                listaContatosBusca.add(usuario);
            }

        }

        adapter = new HistoricosAdapter(listaContatosBusca, getActivity());
        recyclerViewListaHistoricos.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void recarregarHistoricos(){
        adapter = new HistoricosAdapter(listaHistoricos, getActivity());
        recyclerViewListaHistoricos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
