package mx.mercatto.mercastock;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONObject;

import java.lang.reflect.Field;



public class Configuracion extends Fragment implements View.OnClickListener  {
    public static  String _servidorLAN="";
    EditText txtIp;
    InputMethodManager imm;
    View rootView;
    Button guardar;
    public Configuracion() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_configuracion, container, false);
        getActivity().setTitle("Configurar Servidor Sucursal");

        txtIp= (EditText)rootView.findViewById(R.id.editText13);
        txtIp.setText(Constante._settings.getString("SERVIDOR_MERCATTO",""));
        Button upButton = (Button) rootView.findViewById(R.id.ProbarConexion);
        upButton.setOnClickListener(this);
        Button upButton2 = (Button) rootView.findViewById(R.id.GuardarCambios);
        upButton2.setOnClickListener(this);
        PantallaPrincipal.BANDERA_FRAGMENTO = PantallaPrincipal.CONFIGURACION;
        _servidorLAN = txtIp.getText().toString();




        txtIp.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {
        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);

        switch(v.getId())
        {
            case R.id.ProbarConexion: {
                //peticion();
                cargaHost();
                guardar = (Button) getActivity().findViewById(R.id.GuardarCambios);
                guardar.setEnabled(false);
                /*probar = (Button) getActivity().findViewById(R.id.GuardarCambios);
                probar.setEnabled(false);*/
            }
            break;

            case R.id.GuardarCambios: {
                SharedPreferences.Editor editor = Constante._settings.edit();
                editor.putString("DIRECCION_IP", BGTCargarListaServidor.DIRECCION_IP);
                editor.putString("NOMBRE_AMISTOSO", BGTCargarListaServidor.RUTA_REST);
                editor.putString("RUTA_LOGIN", BGTCargarListaServidor.NOMBRE_AMISTOSO);
                editor.putString("URL_REST",BGTCargarListaServidor.URL_REST);
                editor.putString("SERVIDOR_MERCATTO",txtIp.getText().toString());
                editor.apply();

                Login fragment2 = new Login();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_pantalla_principal, fragment2);
                fragmentTransaction.commit();

            }
            break;
            // similarly for other buttons
        }
    }

    public  void cargaHost() {
        _servidorLAN = txtIp.getText().toString();
        BGTCargarListaServidor bgt;
        try {
            JSONObject jsobj = new JSONObject();
            jsobj.put("nombreServidor",_servidorLAN);
            bgt = new BGTCargarListaServidor("http://mercastock.mercatto.mx/API/public/sucursal/lista/conexion", getActivity(),jsobj);
            bgt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}