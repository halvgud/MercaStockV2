package mx.mercatto.mercastock;


public class ListaSucursal {
    private String _nombreAmistoso;
    private String _direccionIp;
    private String _rutaRest;

    public ListaSucursal(String nombreAmistoso, String direccionIp,String rutaRest) {
        this._nombreAmistoso=nombreAmistoso;
        this._direccionIp=direccionIp;
        this._rutaRest=rutaRest;
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
    public String toString(){
        return _nombreAmistoso;
    }
}

