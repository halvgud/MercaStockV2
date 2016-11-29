package mx.mercatto.mercastock;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BGTCargarListaArticulo extends AsyncTask<String,String,JSONObject> {
    private Activity _activity;
    private String _url;
    private JSONObject _postParams;
    public static List<Articulo> _Listado;
    public  JSONObject _JsonGenerico = null;
    ProgressDialog asyncDialog;
    private Integer CodeResponse;

    public BGTCargarListaArticulo(String url, Activity activity, JSONObject postparams){
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
            conexionHttp.setRequestProperty("Authorization","4eb34f33b936af2dc8d024da0c20fd6f");
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
            if(BANDERA_RESULTADO&&CodeResponse.equals(Constante.Ok)){
                ListViewArticulos(postResult);
                PantallaPrincipal.BANDERA_FRAGMENTO=PantallaPrincipal.LISTA_ARTICULO;
            }else{
                ListaGeneral.cargarListadoCategoria(_activity);
            }
            _JsonGenerico=null;
        }finally{
            if(_activity!=null){
                asyncDialog.dismiss();
            }
        }
    }
    public void showToast(String msg){
        Toast.makeText(_activity,msg,Toast.LENGTH_LONG).show();
    }
    private void ListViewArticulos(JSONObject file_url){
        try{
            JSONArray jsonarray = file_url.getJSONArray("datos");
            for(int i =0;i<jsonarray.length();i++){
                JSONObject articuloJson = jsonarray.getJSONObject(i);

                _Listado.add(new Articulo(
                        articuloJson.get("idInventario").toString(),
                        articuloJson.get("art_id").toString(),
                        articuloJson.get("clave").toString(),
                        articuloJson.get("NombreArticulo").toString(),
                        articuloJson.get("cat_id").toString(),
                        articuloJson.get("granel").toString(),
                        articuloJson.get("Unidad").toString(),
                        articuloJson.get("claveAlterna").toString()
                ));
            }
            ListaGeneral.adapter = new ListaAdapter(_Listado,_activity,true);
            ListaGeneral.listView = (ListView) _activity.findViewById(R.id.ListView1);
            ListaGeneral.listView.setAdapter(ListaGeneral.adapter);
            ListaGeneral.contador = ListaGeneral.listView.getCount();

            ListaGeneral.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    FormularioArticulo fragment = new FormularioArticulo();
                    Bundle args = new Bundle();
                    args.putString("NOMBREARTICULO",_Listado.get(position).obtenerDescripcion());
                    args.putString("ARTID",_Listado.get(position).obtenerArtId());
                    args.putString("CLAVE",_Listado.get(position).obtenerClave());
                    args.putString("IDINVENTARIO",_Listado.get(position).obtenerIdInventario());
                    args.putString("UNIDAD",_Listado.get(position).obtenerUnidad());
                    args.putString("GRANEL",_Listado.get(position).obtenerGranel());
                    fragment.setArguments(args);
                    fragment.establecerCategoria(null);
                    _Listado.remove(_Listado.get(position));
                    FragmentManager fragmentManager = _activity.getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_pantalla_principal, fragment).addToBackStack(null).commit();
                }
            });
        }catch(Exception e ){
            showToast(e.getMessage());
        }
    }


}
