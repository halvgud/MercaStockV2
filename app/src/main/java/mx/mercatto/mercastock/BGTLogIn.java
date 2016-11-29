package mx.mercatto.mercastock;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BGTLogIn extends AsyncTask<String,String,JSONObject> {
    private Activity _activity;
    private String _url;
    private JSONObject _postParams;
    public static List<Articulo> _Listado;
    Integer _codeResponse;
    public  JSONObject _JsonGenerico = null;
    ProgressDialog asyncDialog;

    public BGTLogIn(String url, Activity activity, JSONObject postparams){
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
            asyncDialog.setMessage("Autenticando");
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
            _codeResponse= conexionHttp.getResponseCode();
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
            if(BANDERA_RESULTADO){
                LogIn( postResult);
            }else{
                showToast("Error en la respuesta de la transacción");
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

    public void LogIn(JSONObject postResult){
        try{
            switch(_codeResponse){
                case Constante.Ok: {
                    JSONObject datos = postResult.getJSONObject(Constante.jsonDatos);
                    SharedPreferences.Editor editor = Constante._settings.edit();
                    editor.putString(Constante.argumentoIdUsuario(), datos.getString(Constante.jsonIdUsuario));
                    editor.apply();
                    Vibrator v = (Vibrator) _activity.getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(350);
                    ListaGeneral fragment = new ListaGeneral();
                    Bundle args = new Bundle();
                    args.putBoolean(Constante.argumentoArticulo(),false);
                    fragment.setArguments(args);
                    FragmentManager fragmentManager = _activity.getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_pantalla_principal,fragment).addToBackStack(null).commit();
                }break;
                case Constante.Denied:
                    showToast("usuario y/o contraseña incorrectos");
                    break;
                case Constante.NotFound:
                    showToast("algo a salido mal, favor de contactar a sistemas");
                    break;
                default:
                    showToast("algo a ocurrido mal, favor de contactar a sistemas");
            }
        }catch (JSONException excepcion){
            showToast(excepcion.getMessage());
        }

    }
}
