package mx.mercatto.mercastock;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class BGTPostFormularioArticulo extends AsyncTask<String, String, JSONObject> {
    String _url = null;
    static JSONObject jObj = null;
    JSONObject _postParams = null;
    public  JSONObject _JsonGenerico = null;
    Activity activity;
    ProgressDialog asyncDialog;
    boolean BANDERA_RESULTADO;

    static Integer CodeResponse;
    public BGTPostFormularioArticulo(String url, Activity activity, JSONObject postparams) {
        this._url = url;
        this.activity = activity;
        this._postParams = postparams;
        if (activity!= null)
            asyncDialog = new ProgressDialog(activity,R.style.AppCompatAlertDialogStyle);
    }
    public void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }
    protected void onPreExecute() {
        super.onPreExecute();
        if(activity!=null) {
            asyncDialog.setIndeterminate(false);
            asyncDialog.setCancelable(false);
            asyncDialog.setProgress(0);
            asyncDialog.setMessage("Guardando registro, favor de esperar");
            asyncDialog.show();
        }
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            URL url = new URL(_url);
            HttpURLConnection conexionHttp=(HttpURLConnection) url.openConnection();
            conexionHttp.setDoOutput(true);
            conexionHttp.setUseCaches(false);
            conexionHttp.setRequestProperty("Content-Type","application/json");
            conexionHttp.setRequestProperty("Accept","application/json");
            conexionHttp.setRequestProperty("Auth","4eb34f33b936af2dc8d024da0c20fd6f");
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
        } catch (Exception e) {

              showToast(e.getMessage());
        }
        return _JsonGenerico;

    }
    Boolean bandera=true;
    @Override
    protected void onPostExecute(JSONObject file_url) {
        try {
            super.onPostExecute(file_url);
            if(BANDERA_RESULTADO) {

                switch (CodeResponse) {
                    case 200: {
                        showToast(file_url.getString("mensaje"));
                        if(ListaGeneral.devolverConteo()>1){
                            //getActivity().getFragmentManager().popBackStack();
                            Bundle args = new Bundle();
                            args.putInt("TRANSACCION_GENERAL",Constante.ARTICULO);
                            ListaGeneral fragment = new ListaGeneral();
                            fragment.setArguments(args);
                            FragmentManager fragmentManager = activity.getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.content_pantalla_principal, fragment).addToBackStack(null).commit();
                        }
                        else{
                            Bundle args = new Bundle();
                            args.putInt("TRANSACCION_GENERAL",Constante.CATEGORIA);
                            ListaGeneral fragment2 = new ListaGeneral();
                            fragment2.setArguments(args);
                            FragmentManager fragmentManager = activity.getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_pantalla_principal, fragment2);
                            fragmentTransaction.commit();
                        }
                    }
                    break;
                    default:
                        showToast(Integer.toString(CodeResponse));
                        break;
                }
            }
            jObj=null;
        }catch(JSONException ignored){
            Log.d("kek",ignored.getMessage());
        }
        finally{
            if(activity!=null){
                asyncDialog.dismiss();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     asyncDialog.dismiss();
            }
        }
    }

}



