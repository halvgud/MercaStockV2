package mx.mercatto.mercastock;

import java.util.ArrayList;
import java.util.List;

public class Categoria {
    private String nombre;
    private String auxiliar;
    public String obtenerAuxiliar(){
        return auxiliar;
    }
    public String obtenerNombre(){
        return nombre;
    }
    private String catId;
    public String obtenerCatId(){
        return catId;
    }
    private String cantidad;
    public String obtenerCantidad(){
        return cantidad;
    }
    private ArrayList<Articulo> listaArticulo;
    public ArrayList<Articulo> obtenerListaArticulo(){
        return listaArticulo;
    }
    public Categoria(String catId,String auxiliar, String nombre,String cantidad,List<Articulo> listaArticulo) {
        this.catId = catId;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.auxiliar=auxiliar;
        this.listaArticulo= new ArrayList<>();
        this.listaArticulo.addAll(listaArticulo);
    }
}
