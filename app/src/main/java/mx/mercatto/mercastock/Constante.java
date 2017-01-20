package mx.mercatto.mercastock;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Constante {
    public static final int Ok = 200;
    public static final int Denied = 401;
    public static final int NotFound = 404;
    public static final int ARTICULO=1;
    public static final int CATEGORIA=2;
    public static final int GENERAL=3;
    public static final String jsonDatos="datos";
    public static final String jsonUsuario="usuario";
    public static final String jsonContrasena="contrasena";
    public static final String jsonClaveApi="claveApi";
    public static final String jsonIdUsuario="idUsuario";
    public static final String jsonClaveGCM = "claveGCM";
    private static final String restArticuloActualizar="/articulo/actualizar";
    private static final String restArticuloObtener ="/articulo/obtener";
    private static final String restUsuarioLogin = "/usuario/login";
    private static final String restListaCategoria ="/categoria/seleccionar";
    private static final String restWebArticuloCrearGeneral="/inventario/nuevo";
    private static final String restWebArticuloInsertar="/articulo/insertar";
    private static final String restListaConexion="/sucursal/lista/conexion";
    public static SharedPreferences _settings;
    public static SharedPreferences.Editor _editor;
    public static String claveGCM="";

    public Constante(Activity activiy){
        _settings=PreferenceManager.getDefaultSharedPreferences(activiy.getApplicationContext());
        _editor= _settings.edit();
    }

    public static String obtenerUrlApi(){
        return _settings.getString("URL_REST","");
    }
    public static String obtenerIdSucursal(){
        return _settings.getString("ID_SUCURSAL","");
    }

    public static String urlArticuloActualizar(){
        return obtenerUrlApi()+restArticuloActualizar;
    }

    public static String urlInventarioCrearGeneral(){
        return ObtenerUrlApiWeb()+restWebArticuloCrearGeneral;
    }
    public static String urlArticuloInsertar(){
        return obtenerUrlApi()+restWebArticuloInsertar;
    }

    public static String urlListaSucursalWeb(){
        return ObtenerUrlApiWeb()+restListaConexion;
    }

    public static String ObtenerUrlApiWeb(){
        return "http://mercastock.mercatto.mx/API/public";
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

    public static void ConfigurarUsuario(String idUsuario){
        _editor.putString(argumentoIdUsuario(),idUsuario);
        _editor.apply();
    }

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
    public static String argumentoExistencia(){
        return "EXISTENCIA";
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
}

