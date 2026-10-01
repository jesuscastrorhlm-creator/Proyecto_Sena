package Tienda;

public class Venta {

    private int id_venta;
    private String fecha;
    private int id_cliente;
    private double total;

    public Venta() {
    }

    public Venta(int id_venta, String fecha, int id_cliente, double total) {
        this.id_venta = id_venta;
        this.fecha = fecha;
        this.id_cliente = id_cliente;
        this.total = total;
    }

    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}