package mx.mercatto.mercastock;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.FragmentManager;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class PantallaPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String PROJECT_NUMBER="917548048883";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Constante constante = new Constante(this);
        GCMClientManager pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {

            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {
                System.out.println("Registration id"+ registrationId);
                Constante.claveGCM=registrationId;
            }

            @Override
            public void onFailure(String ex) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = settings.edit();

                editor.apply();
                super.onFailure(ex);
            }
        });
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(!Constante.obtenerUrlApi().equals("")){
            Login fragment2 = new Login();
            fragmentTransaction.replace(R.id.content_pantalla_principal, fragment2);
        }else{
            Configuracion fragment2 = new Configuracion();
            fragmentTransaction.replace(R.id.content_pantalla_principal, fragment2);
        }
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            switch(BANDERA_FRAGMENTO){
                case LISTA_CATEGORIA:
                    ListaGeneral.backButtonWasPressed(this);
                    ListaGeneral.CAT_ID="";
                    break;
                case ARTICULO:
                    Bundle args = new Bundle();
                    args.putBoolean(Constante.argumentoArticulo(),true);
                    ListaGeneral fragment = new ListaGeneral();
                    fragment.setArguments(args);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_pantalla_principal, fragment).addToBackStack(null).commit();
                    break;
                case LISTA_ARTICULO:
                    Bundle args2 = new Bundle();
                    args2.putBoolean(Constante.argumentoArticulo(),false);
                    ListaGeneral fragment2 = new ListaGeneral();
                    fragment2.setArguments(args2);
                    FragmentManager fragmentManager2 = getFragmentManager();
                    fragmentManager2.beginTransaction().replace(R.id.content_pantalla_principal, fragment2).addToBackStack(null).commit();
                    break;
                case CONFIGURACION:
                    break;
            }
           // super.onBackPressed();
        }
    }
    static int BANDERA_FRAGMENTO=0;
    static final int LISTA_CATEGORIA=1;
    static final int ARTICULO=2;
    static final int LISTA_ARTICULO=3;
    static final int CONFIGURACION=4;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pantalla_principal, menu);
        // Retrieve the SearchView and plug it into SearchManager
        /*final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id==R.id.cerrarsesion){
            Login login = new Login();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_pantalla_principal,login).addToBackStack(null).commit();
        }else if (id==R.id.ConfigurarServidor){
            Configuracion config = new Configuracion();
            FragmentManager fragmentManager = this.getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_pantalla_principal,config).addToBackStack(null).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
