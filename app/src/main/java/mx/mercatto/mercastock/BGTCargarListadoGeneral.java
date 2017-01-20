package mx.mercatto.mercastock;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Iterator;
import java.util.List;


public class BGTCargarListadoGeneral extends AsyncTask<String, String, JSONObject> {
    public static Integer CodeResponse=400;
    public static JSONObject _JsonGenerico = null;
    public static List<Categoria>_Listado;
    static JSONObject jObj = null;
    static String json = "";
    String sURL = null;
    JSONObject postparams;
    Activity activity;
    ProgressDialog asyncDialog;
    boolean bandera=true;
    public static int BANDERA_LISTA;
    public static final int BANDERA_CATEGORIA=0;
    public static final int BANDERA_ARTICULO=1;
    public static final int BANDERA_BUSQUEDA=2;
    public BGTCargarListadoGeneral(String url, Activity activity, JSONObject postparams) {
        this.sURL = url;
        this.activity = activity;
        this.postparams = postparams;
        if (activity!= null){
            asyncDialog = new ProgressDialog(activity,R.style.AppCompatAlertDialogStyle);
        }
    }

    protected void onPreExecute() {
        super.onPreExecute();
        if(activity!=null) {
            asyncDialog.setIndeterminate(false);
            asyncDialog.setCancelable(false);
            asyncDialog.setProgress(0);
            asyncDialog.setMessage("Cargando Lista de Categorias");
            asyncDialog.show();
        }
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        _Listado = new ArrayList<>();
        _JsonGenerico = null;
        try {
            URL url=new URL(sURL);
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setUseCaches(false);
            httpCon.setRequestProperty("Content-Type", "application/json");
            httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestProperty("Authorization","4eb34f33b936af2dc8d024da0c20fd6f");
            httpCon.setRequestMethod("POST");
            httpCon.connect(); // Note the connect() here

            OutputStream os = httpCon.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");

            osw.write(postparams.toString());
            osw.flush();
            osw.close();

            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader( httpCon.getInputStream(),"utf-8"));
            CodeResponse =httpCon.getResponseCode();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            json = sb.toString();
            Log.d("jsonresponse",json);
            jObj = new JSONObject(json);

        } catch (Exception e){
            bandera=false;
        }
        return jObj;
    }

    @Override
    protected void onPostExecute(JSONObject file_url) {
        Log.d("respuesta",CodeResponse+"");
        try {
            super.onPostExecute(file_url);
            if(bandera) {
                ListViewCategorias(file_url);
            }
            else{
                if(CodeResponse>400){
                    showToast("Error al cargar la lista, favor de intentar nuevamente "
                            +CodeResponse);
                }else{
                    showToast("Parece que algo surgió mal...:"
                            +CodeResponse);
                }

            }

            jObj=null;
        }
        finally{
            if(activity!=null){
                asyncDialog.dismiss();
            }
        }
    }

    public void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }

    public void ListViewCategorias(JSONObject file_url){
        try {

            for(Iterator iterator = file_url.keys(); iterator.hasNext();) {
                int contador=0;
                String key = (String) iterator.next();
                JSONArray jsonArray= file_url.getJSONArray(key);
                ArrayList<Articulo> articulo = new ArrayList<>();
                String catId="";
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject articulojson = jsonArray.getJSONObject(i);
                    articulo.add(new Articulo(
                                              articulojson.get("idInventario").toString(),
                                              articulojson.get("art_id").toString(),
                                              articulojson.get("clave").toString(),
                                              articulojson.get("descripcion").toString(),
                                              articulojson.get("cat_id").toString(),
                                              articulojson.get("granel").toString(),
                                              articulojson.get("Unidad").toString(),
                                              articulojson.get("claveAlterna").toString(),
                                              articulojson.get("existencia").toString()
                                                ));
                    catId = articulojson.get("cat_id").toString();
                    contador++;
                }
                _Listado.add(new Categoria(catId,contador+"",key,"",articulo));
            }

            int transaccion=PantallaPrincipal.isChecked?ListaAdapter.TRANSACCION_GENERAL:ListaAdapter.TRANSACCION_CATEGORIA;
            ListaGeneral.adapter = new ListaAdapter(_Listado,activity,transaccion);

            ListaGeneral.listView= (ListView) activity.findViewById(R.id.ListView1);
            ListaGeneral.listView.setAdapter(ListaGeneral.adapter);
            ListaGeneral.contador = ListaGeneral.listView.getCount();
            BANDERA_LISTA=BANDERA_CATEGORIA;
            ListaGeneral.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    if(BANDERA_LISTA==BANDERA_CATEGORIA) {
                        ListaGeneral.adapter = new ListaAdapter(_Listado.get(position).obtenerListaArticulo(), activity, true);
                        ListaGeneral.listView.setAdapter(ListaGeneral.adapter);
                        ListaGeneral.CAT_ID=_Listado.get(position).obtenerCatId();
                        activity.setTitle("ARTICULO");

                        BANDERA_LISTA=BANDERA_ARTICULO;
                        Posicion=position;
                        ListaGeneral.contador = ListaGeneral.listView.getCount();
                    }else if(BANDERA_LISTA==BANDERA_ARTICULO){
                        FormularioArticulo fragment = new FormularioArticulo();
                        Bundle args = new Bundle();
                        args.putString("NOMBREARTICULO",_Listado.get(Posicion).obtenerListaArticulo().get(position).obtenerDescripcion());
                        args.putString("ARTID",_Listado.get(Posicion).obtenerListaArticulo().get(position).obtenerArtId());
                        args.putString("CLAVE",_Listado.get(Posicion).obtenerListaArticulo().get(position).obtenerClave());
                        args.putString("IDINVENTARIO",_Listado.get(Posicion).obtenerListaArticulo().get(position).obtenerIdInventario());
                        args.putString("UNIDAD",_Listado.get(Posicion).obtenerListaArticulo().get(position).obtenerUnidad());
                        args.putString("GRANEL",_Listado.get(Posicion).obtenerListaArticulo().get(position).obtenerGranel());
                        args.putString("EXISTENCIA",_Listado.get(Posicion).obtenerListaArticulo().get(position).obtenerExistencia());
                        fragment.setArguments(args);
                        //fragment.establecerCategoria(_Listado.get(position));
                        FragmentManager fragmentManager = activity.getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_pantalla_principal, fragment).addToBackStack(null).commit();
                        BANDERA_LISTA=BANDERA_CATEGORIA;
                    }else{
                        FormularioArticulo fragment = new FormularioArticulo();
                        Bundle args = new Bundle();
                        args.putString("NOMBREARTICULO",_Listado.get(position).obtenerListaArticulo().get(0).obtenerDescripcion());
                        args.putString("ARTID",_Listado.get(position).obtenerListaArticulo().get(0).obtenerArtId());
                        args.putString("CLAVE",_Listado.get(position).obtenerListaArticulo().get(0).obtenerClave());
                        args.putString("IDINVENTARIO",_Listado.get(position).obtenerListaArticulo().get(0).obtenerIdInventario());
                        args.putString("UNIDAD",_Listado.get(position).obtenerListaArticulo().get(0).obtenerUnidad());
                        args.putString("GRANEL",_Listado.get(position).obtenerListaArticulo().get(0).obtenerGranel());
                        args.putString("EXISTENCIA",_Listado.get(position).obtenerListaArticulo().get(0).obtenerExistencia());
                        fragment.setArguments(args);
                        //fragment.establecerCategoria(_Listado.get(position));
                        ListaGeneral.CAT_ID=_Listado.get(position).obtenerListaArticulo().get(0).obtenerCatId();
                        FragmentManager fragmentManager = activity.getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_pantalla_principal, fragment).addToBackStack(null).commit();
                        BANDERA_LISTA=BANDERA_CATEGORIA;
                    }

               }
            });

        }catch(Exception e){
            Log.d("",e.getMessage());
        }

    }
    Integer Posicion=0;


}
