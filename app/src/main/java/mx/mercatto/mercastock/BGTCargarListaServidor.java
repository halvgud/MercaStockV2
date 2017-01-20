package mx.mercatto.mercastock;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class BGTCargarListaServidor extends AsyncTask<String,String,JSONObject> {
    private Activity _activity;
    private String _url;
    private JSONObject _postParams;
    public  JSONObject _JsonGenerico = null;
    ProgressDialog asyncDialog;
    private static int CodeResponse;
    public BGTCargarListaServidor(String url, Activity activity, JSONObject postparams){
        _url=url;
        _activity=activity;
        _postParams=postparams;
        if (_activity!= null){
            asyncDialog = new ProgressDialog(_activity,R.style.AppCompatAlertDialogStyle);
        }
    }
    protected void onPreExecute(){
        super.onPreExecute();
        if(_activity!=null) {
            asyncDialog.setIndeterminate(false);
            asyncDialog.setCancelable(false);
            asyncDialog.setProgress(0);
            asyncDialog.setMessage("Cargando lista de Articulos");
            asyncDialog.show();
        }
    }
    @Override
    protected JSONObject doInBackground(String... params){
        _Listado = new ArrayList<>();
        try{
            URL url = new URL(_url);
            HttpURLConnection conexionHttp=(HttpURLConnection) url.openConnection();
            conexionHttp.setDoOutput(true);
            conexionHttp.setUseCaches(false);
            conexionHttp.setRequestProperty("Content-Type","application/json");
            conexionHttp.setRequestProperty("Accept","application/json");
            conexionHttp.setRequestMethod("POST");
            conexionHttp.connect();
            OutputStream os= conexionHttp.getOutputStream();
            OutputStreamWriter osw= new OutputStreamWriter(os,"UTF-8");
            osw.write(_postParams.toString());
            osw.flush();
            osw.close();
            StringBuilder sb = new StringBuilder();
            CodeResponse = conexionHttp.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(conexionHttp.getInputStream(),"utf-8"));
            String line;
            while((line=br.readLine())!=null){
                sb.append(line).append("\n");
            }
            _JsonGenerico=new JSONObject(sb.toString());
            BANDERA_RESULTADO=true;
        }catch(Exception e){
            BANDERA_RESULTADO=false;
        }

        return _JsonGenerico;
    }
    boolean BANDERA_RESULTADO;
    @Override
    protected void onPostExecute(JSONObject postResult){
        try{
            super.onPostExecute(postResult);
            if(BANDERA_RESULTADO&&CodeResponse==Constante.Ok){
                cargarListadoSucursalHost( postResult);
            }else{
                showToast("Error al cargar el listado de sucursales");
                listaSucursal = (Spinner) _activity.findViewById(R.id.spinnerRegistroUsuario2);
                listaSucursal.setAdapter(null);

            }
            _JsonGenerico=null;
        }finally{
            if(_activity!=null){
                asyncDialog.dismiss();
            }
        }
    }
    ArrayList<ListaSucursal> _listaSucursal2 = new ArrayList<>();
    public static ArrayList<HashMap<String, String>>_Listado;
    Spinner listaSucursal;
    public static String DIRECCION_IP ="";
    public static String NOMBRE_AMISTOSO ="";
    public static String RUTA_REST ="";
    public static String URL_REST="";
    public static String ID_SUCURSAL="";
    Button guardar;
    Button probar;

    public void showToast(String msg){
        Toast.makeText(_activity,msg,Toast.LENGTH_LONG).show();
    }
    public void cargarListadoSucursalHost(JSONObject file_url) {
        //SON.stringify(file_url).indexOf('m');
        _Listado = new ArrayList<>();
        if (BANDERA_RESULTADO) {
            try {
                JSONArray countries = file_url.getJSONArray("data");

                if (countries.length() > 0) {
                    for (int i = 0; i < countries.length(); i++) {
                        HashMap<String, String> map = new HashMap<>();
                        JSONObject c = countries.getJSONObject(i);
                        String direccionIp = c.getString("direccionIp");
                        String nombreAmistoso = c.getString("nombreAmistoso");
                        String rutaRest = c.getString("rutaRest");
                        String IdSucursal = c.getString("idSucursal");
                        map.put("nombreAmistoso", nombreAmistoso);
                        map.put("direccionIp",direccionIp);
                        map.put("rutaRest",rutaRest);
                        _listaSucursal2.add(new ListaSucursal(nombreAmistoso,direccionIp,rutaRest,IdSucursal));
                        _Listado.add(map);
                    }

                } else {
                    _listaSucursal2.add(new ListaSucursal("", "","",""));
                }

                listaSucursal = (Spinner) _activity.findViewById(R.id.spinnerRegistroUsuario2);
                SucursalAdapter cAdapter = new SucursalAdapter(_activity, android.R.layout.simple_spinner_item, _listaSucursal2){
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);

                        ((TextView) v).setTextSize(18);
                        ((TextView) v).setTextColor(
                                _activity.getResources().getColorStateList(R.color.white)
                        );

                        return v;
                    }
                };
                listaSucursal.setAdapter(cAdapter);
                showToast("Se han cargado las sucursales");
                listaSucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ListaSucursal selectedCountry = _listaSucursal2.get(position);
                        NOMBRE_AMISTOSO =selectedCountry.obtenerNombreAmistoso();
                        RUTA_REST = selectedCountry.obtenerRutaRest();
                        DIRECCION_IP=selectedCountry.obtenerDireccionIp();
                        URL_REST=selectedCountry.obtenerRutaCompuesta();
                        ID_SUCURSAL=selectedCountry.obtenerIdSucursal();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                guardar = (Button) _activity.findViewById(R.id.GuardarCambios);
                guardar.setEnabled(true);

            }catch(JSONException e){
                Log.d("exc",e.getMessage());
            }

        }
        else {
            guardar = (Button)_activity.findViewById(R.id.GuardarCambios);
            guardar.setEnabled(false);
            probar = (Button) _activity.findViewById(R.id.ProbarConexion);
            probar.setEnabled(false);
            showToast("No se ha podido establecer la conexión");
            probar = (Button) _activity.findViewById(R.id.ProbarConexion);
            probar.setEnabled(true);
            listaSucursal = (Spinner) _activity.findViewById(R.id.spinnerRegistroUsuario2);
            listaSucursal.setAdapter(null);
        }
    }
}
