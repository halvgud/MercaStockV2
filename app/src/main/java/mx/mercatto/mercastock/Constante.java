package mx.mercatto.mercastock;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Constante {
    public static SharedPreferences _settings;

    public static String obtenerUrlApi(){
        return _settings.getString("URL_REST","");
    }

    private static final String restArticuloActualizar="/articulo/actualizar";
    private static final String restArticuloObtener ="/articulo/obtener";
    private static final String restUsuarioLogin = "/usuario/login";
    private static final String restListaCategoria ="/categoria/seleccionar";
    public static String urlArticuloActualizar(){
        return obtenerUrlApi()+restArticuloActualizar;
    }
    public static String urlUsuarioLogIn(){
        return obtenerUrlApi()+restUsuarioLogin;
    }
    public static String urlListaCategoria(){
        return obtenerUrlApi()+restListaCategoria;
    }
    public static String restArticuloObtener(){
        return obtenerUrlApi()+restArticuloObtener;
    }


    public static String obtenerIdUsuario(){
        return _settings.getString(argumentoIdUsuario(),"");
    }


    public static final int Ok = 200;
    public static final int Denied = 401;
    public static final int NotFound = 404;

    public static String argumentoNombreArticulo(){
        return "NOMBREARTICULO";
    }
    public static String argumentoClave(){
        return "CLAVE";
    }
    public static String argumentoIdInventario(){
        return "IDINVENTARIO";
    }
    public static String argumentoArtId(){
        return "ARTID";
    }
    public static String argumentoGranel(){
        return "GRANEL";
    }
    public static String argumentoUnidad(){
        return "UNIDAD";
    }
    public static String argumentoIdUsuario(){
        return "IDUSUARIO";
    }
    public static String argumentoArticulo(){
        return "ARTICULO";
    }

    public static final String jsonDatos="datos";
    public static final String jsonUsuario="usuario";
    public static final String jsonContrasena="contrasena";
    public static final String jsonClaveApi="claveApi";
    public static final String jsonIdUsuario="idUsuario";
    public static final String jsonClaveGCM = "claveGCM";
    public static String claveGCM="";

    public Constante(Activity activiy){
        _settings=PreferenceManager.getDefaultSharedPreferences(activiy.getApplicationContext());
    }
}

