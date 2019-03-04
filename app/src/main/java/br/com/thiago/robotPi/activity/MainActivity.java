package br.com.thiago.robotPi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import br.com.thiago.robotPi.R;
import br.com.thiago.robotPi.config.ConfiguracaoFirebase;
import br.com.thiago.robotPi.dao.UserDAO;
import br.com.thiago.robotPi.fragment.HistoricoFragment;
import br.com.thiago.robotPi.fragment.RaspberryFragment;
import br.com.thiago.robotPi.model.Raspberry;
import br.com.thiago.robotPi.model.User;
import br.com.thiago.robotPi.tasks.RemoveUserDispositivoTask;
import br.com.thiago.robotPi.tasks.SalvaUserDispositivoTask;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private MaterialSearchView searchView;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        user = new UserDAO(MainActivity.this).buscaUser();

        new SalvaUserDispositivoTask(MainActivity.this, user).execute();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Robot Pi");
        setSupportActionBar( toolbar );

        //Configurar abas
        final FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                .add("Drones", RaspberryFragment.class)
                .add("Histórico", HistoricoFragment.class)
                .create()
        );

        final ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter( adapter );

        SmartTabLayout viewPagerTab = findViewById(R.id.viewPagerTab);
        viewPagerTab.setViewPager( viewPager );


        //Configuração do search view
        searchView = findViewById(R.id.materialSearchPrincipal);
        //Listener para o search view
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                RaspberryFragment fragment = (RaspberryFragment) adapter.getPage(0);
                fragment.recarregarRaspberry();

            }
        });

        //Listener para caixa de texto
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Log.d("evento", "onQueryTextSubmit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.d("evento", "onQueryTextChange");

                // Verifica se esta pesquisando Conversas ou Contatos
                // a partir da tab que esta ativa
                switch ( viewPager.getCurrentItem() ){
                    case 0:
                        RaspberryFragment raspberryFragment = (RaspberryFragment) adapter.getPage(0);
                        if( newText != null && !newText.isEmpty() ){
                            raspberryFragment.pesquisarRaspberries( newText.toLowerCase() ); //Vai atualizando a view ao mesmo tempo
                        }else {
                            raspberryFragment.recarregarRaspberry();
                        }
                        break;
                    case 1 :
                        HistoricoFragment historicoFragment = (HistoricoFragment) adapter.getPage(1);
                        if( newText != null && !newText.isEmpty() ){
                            historicoFragment.pesquisarHistoricos( newText.toLowerCase() );
                        }else {
                            historicoFragment.recarregarHistoricos();
                        }
                        break;
                }



                return true;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        //Configurar botao de pesquisa
        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.menuSair :
                deslogarUsuario();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.menuConfiguracoes :
                abrirConfiguracoes();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void deslogarUsuario(){

        try {
            new UserDAO(MainActivity.this).deleta();
            new RemoveUserDispositivoTask(MainActivity.this).removeDispositivo();
            autenticacao.signOut();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void abrirConfiguracoes(){
        Intent intent = new Intent(MainActivity.this, ConfiguracoesActivity.class);
        startActivity( intent );
    }

}
