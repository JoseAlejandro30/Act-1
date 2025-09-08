package org.example.facturacion.Classes;

public class LineaFactura {
    private Producto producto;
    private int cantidad;

    public LineaFactura(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public double getSubtotal() { return producto.getPrice() * cantidad; }
    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public double calcularImpuesto() {
        double subtotal = getSubtotal(); // precio * cantidad
        return producto.getTipodeImpuesto().calcularImpuesto(subtotal);
    }
}
