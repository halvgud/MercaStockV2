package mx.mercatto.mercastock;

import android.app.Activity;
import android.app.SearchManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import org.json.JSONObject;



public class ListaGeneral extends Fragment implements View.OnClickListener {
    public static String CAT_ID="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //getActivity().InvalidateOptionsMenu();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.pantalla_principal_lista, menu);
        super.onCreateOptionsMenu(menu,inflater);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(getActivity().SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        MenuItem searchItem = menu.findItem(R.id.search);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {


            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
              // adapter =new listView.getAdapter();
                if(searchQuery!=null&&!searchQuery.isEmpty()){
                    adapter.filter(searchQuery.trim());
                    listView.invalidate();
                    return true;
                }
                return false;

            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });
    }
    public static ListView listView;
    public static ListaAdapter adapter;
    public static int contador=0;
    public static int devolverConteo(){
        return contador;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lista_general, container, false);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Bundle args = getArguments();
        Boolean LISTA_ARTICULO = args.getBoolean("ARTICULO");
        if(LISTA_ARTICULO){
            getActivity().setTitle("ARTICULO");
            DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            cargarListadoArticulo(getActivity());
            PantallaPrincipal.BANDERA_FRAGMENTO=PantallaPrincipal.ARTICULO;
            return rootView;
        }else{
            getActivity().setTitle("CATEGORIA");
            DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            cargarListadoCategoria(getActivity());
            PantallaPrincipal.BANDERA_FRAGMENTO=PantallaPrincipal.LISTA_CATEGORIA;
            return rootView;
        }

    }
    public static void cargarListadoCategoria(Activity act) {
        try {
            BGTCargarListadoGeneral bgt;
            JSONObject jsonObj1 = new JSONObject();
            bgt = new BGTCargarListadoGeneral(Constante.urlListaCategoria(),act,jsonObj1);
            bgt.execute();

        } catch (Exception e) {
            // throw e;
        }
    }

    public static void cargarListadoArticulo(Activity act){
        try{
            BGTCargarListaArticulo bgt;
            JSONObject jsonObj1 = new JSONObject();
            jsonObj1.put("cat_id", CAT_ID);
            //jsonObj1.put("claveApi","4b6b41242e48f8f69f263687e15f8f24");
            bgt=new BGTCargarListaArticulo(Constante.restArticuloObtener(),act,jsonObj1);
            bgt.execute();
        }catch(Exception e){
            Log.d("catch",e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {

    }
    public static void backButtonWasPressed(Activity act) {
        act.setTitle("CATEGORIA");
        cargarListadoCategoria(act);
    }



}
