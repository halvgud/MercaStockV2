package mx.mercatto.mercastock;

public class Articulo {
    private String descripcion;

    private String artId;
    private String clave;
    private String catId;
    private String idInventario;
    private String granel;
    private String unidad;
    private String claveAlterna;

    public String obtenerDescripcion() {
        return descripcion;
    }

    public String obtenerArtId() {
        return artId;
    }
    public String obtenerGranel(){
        return granel;
    }
    public String obtenerUnidad(){
        return unidad;
    }
    public String obtenerCatId(){
        return catId;
    }
    public String obtenerClave(){
        return clave;
    }
    public String obtenerClaveAlterna(){
        return claveAlterna;
    }
    public String obtenerIdInventario(){
        return idInventario;
    }

    public Articulo(String idInventario,
                    String artId,
                    String clave,
                    String descripcion,
                    String catId,
                    String granel,
                    String unidad,
                    String claveAlterna
                    ) {
        this.idInventario=idInventario;
        this.artId = artId;
        this.clave = clave;
        this.descripcion = descripcion;
        this.catId = catId;
        this.granel = granel;
        this.unidad=unidad;
        this.claveAlterna=claveAlterna;
    }
}
