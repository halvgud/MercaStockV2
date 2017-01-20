package mx.mercatto.mercastock;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;


public class FormularioArticulo extends Fragment  implements View.OnClickListener {

    private Categoria _Categoria;
    public  void establecerCategoria(Categoria categoria){
        this._Categoria=categoria;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    String NombreArticulo="";
    String Clave="";
    String ArtId="";
    String Existencia="";

    InputMethodManager imm;
    String IdInventario="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       final View rootView = inflater.inflate(R.layout.fragment_formulario_articulo, container, false);
        Bundle args = getArguments();
        NombreArticulo = args.getString(Constante.argumentoNombreArticulo(),"");
        Clave = args.getString(Constante.argumentoClave(),"");
        IdInventario = args.getString(Constante.argumentoIdInventario(),"");
        ArtId= args.getString(Constante.argumentoArtId(),"");
        String esGranel = args.getString(Constante.argumentoGranel(),"");
        Existencia = args.getString(Constante.argumentoExistencia(),"");

        EditText txt1 = (EditText) rootView.findViewById(R.id.editText3);
        TextView txtTituloInferior = (TextView) rootView.findViewById(R.id.FormularioArticulotxtTituloInferior);
        TextView txtCodigoDeBarras = (TextView) rootView.findViewById(R.id.FormularioArticulotxtCodigoDeBarras);
        txtTituloInferior.setText(NombreArticulo);
        txtCodigoDeBarras.setText(Clave);
        TextView txtCantidad = (TextView) rootView.findViewById(R.id.textView4);
        txtCantidad.setText(String.format("Cantidad por %s:", args.getString(Constante.argumentoUnidad())));
        getActivity().setTitle("ARTICULO :"+ArtId);
        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        PantallaPrincipal.BANDERA_FRAGMENTO=PantallaPrincipal.ARTICULO;
        Button upButton = (Button) rootView.findViewById(R.id.button3);
        upButton.setOnClickListener(this);

        txt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                EditText text = (EditText) rootView.findViewById(R.id.editText3);
                String value = text.getText().toString();
                String gg = "";
                if (value.equals(gg)) {
                    rootView.findViewById(R.id.button3).setEnabled(false);
                } else {
                    rootView.findViewById(R.id.button3).setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                EditText text = (EditText) rootView.findViewById(R.id.editText3);
                String value = text.getText().toString();
                String gg = "";
                if (value.equals(gg)) {
                    rootView.findViewById(R.id.button3).setEnabled(false);
                } else {
                    rootView.findViewById(R.id.button3).setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                EditText text = (EditText) rootView.findViewById(R.id.editText3);
                String value = text.getText().toString();
                String gg = "";
                if (value.equals(gg)) {
                    rootView.findViewById(R.id.button3).setEnabled(false);
                } else {
                    rootView.findViewById(R.id.button3).setEnabled(true);
                }
                //Toast.makeText(getApplicationContext(), "Your3 toast message.",
                //      Toast.LENGTH_SHORT).show();

            }
        });
        assert esGranel != null;
        if(esGranel.equals("1")){
            txt1.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        else
        {
            txt1.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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

                return true;
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
    @Override
    public void onClick(View v) {
        final EditText valor;

        valor = (EditText) getActivity().findViewById(R.id.editText3);
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
        dialogo1.setTitle(Html.fromHtml("<font color='#000'>Aviso</font>"));
        dialogo1.setMessage("Se va a registrar la cantidad de \n" + valor.getText().toString() + "\n ¿Desea continuar?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                aceptar(valor.getText().toString());
                imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getView() != null && getView().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                }
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
            }
        });
        AlertDialog dialogo=dialogo1.show();
        TextView messageView = (TextView)dialogo.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);

    }

    public void aceptar(String valor) {
        try{
            JSONObject jsobj = new JSONObject();
            if(!IdInventario.equals("0")){
                jsobj.put("idInventario",IdInventario);
                jsobj.put("existenciaRespuesta",valor);
                jsobj.put("idUsuario",Constante.obtenerIdUsuario());
                jsobj.put("art_id",ArtId);
                jsobj.put("claveApi2","");
                BGTPostFormularioArticulo bgt = new BGTPostFormularioArticulo(Constante.urlArticuloActualizar(), getActivity(), jsobj);
                bgt.execute();
            }else{
                jsobj.put("idInventario",0);
                jsobj.put("existenciaRespuesta",valor);
                jsobj.put("idSucursal",Constante.obtenerIdSucursal());
                jsobj.put("existenciaSolicitud",Existencia);
                jsobj.put("existenciaEjecucion",Existencia);
                jsobj.put("idUsuario",Constante.obtenerIdUsuario());
                jsobj.put("art_id",ArtId);
                BGTPostFormularioArticulo bgt = new BGTPostFormularioArticulo(/*Constante.urlInventarioCrearGeneral()*/Constante.urlArticuloInsertar(), getActivity(), jsobj);
                bgt.execute();
            }

        }catch(Exception e){
            showToast(e.getMessage());
        }
    }
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}
