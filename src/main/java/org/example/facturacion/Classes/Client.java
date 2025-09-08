package org.example.facturacion.Classes;

import java.util.ArrayList;

public class Client extends Persona {
    private ArrayList<Producto> productos = new ArrayList<>();

    public Client() {
    }

    public Client(String id, String nombre, String email, String identificacion) {
        super(id, nombre, email, identificacion);
    }

    public void agregarProducto(Producto producto) {
        this.productos.add(producto);
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id='" + getId() + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", identificacion='" + getIdentificacion() + '\'' +
                '}';
    }
}
