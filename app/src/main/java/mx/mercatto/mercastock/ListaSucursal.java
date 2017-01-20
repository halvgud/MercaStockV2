package mx.mercatto.mercastock;


public class ListaSucursal {
    private String _nombreAmistoso;
    private String _direccionIp;
    private String _rutaRest;
    private String _IdSucursal;

    public ListaSucursal(String nombreAmistoso, String direccionIp,String rutaRest,String IdSucursal) {
        this._nombreAmistoso=nombreAmistoso;
        this._direccionIp=direccionIp;
        this._rutaRest=rutaRest;
        this._IdSucursal=IdSucursal;
    }

    public String obtenerNombreAmistoso() {
        return _nombreAmistoso;
    }

    public String obtenerDireccionIp() {
        return _direccionIp;
    }

    public String obtenerRutaRest() {
        return _rutaRest;
    }

    public String obtenerRutaCompuesta(){
        return "http://"+_direccionIp+_rutaRest;
    }
    public String obtenerIdSucursal(){
        return _IdSucursal;
    }
    public String toString(){
        return _nombreAmistoso;
    }
}

