package org.example.facturacion.Classes;

import org.example.facturacion.Controllers.Impuesto;

public class Producto {
    private String id;
    private String description;
    private float price;
    private int stock;
    private Impuesto TipodeImpuesto;
    private String clienteId; // ID del cliente propietario

    public Producto(String id, String description, float price, int stock, Impuesto tipodeImpuesto, String clienteId) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.TipodeImpuesto = tipodeImpuesto;
        this.clienteId = clienteId;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price > 0 ? price : 0;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStock() {
        return stock > 0 ? stock : 0;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Impuesto getTipodeImpuesto() {
        return TipodeImpuesto;
    }

    public void setTipodeImpuesto(Impuesto tipodeImpuesto) {
        TipodeImpuesto = tipodeImpuesto;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public double calcularImpuesto(double subtotal) {
        // Ahora recibe el subtotal como parámetro para calcular correctamente
        return TipodeImpuesto.calcularImpuesto(subtotal);
    }
    @Override
    public String toString() {
        return "Producto{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", TipodeImpuesto=" + TipodeImpuesto.getTipo() +
                ", clienteId='" + clienteId + '\'' +
                '}';
    }
}
