
package Tienda;


public class ItemCarrito {
     private int idProducto;
    private String nombre;
    private double precio;
    private int cantidad;

    public ItemCarrito(int idProducto, String nombre, double precio, int cantidad) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return precio * cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
