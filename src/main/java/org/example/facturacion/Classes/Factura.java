package org.example.facturacion.Classes;

import java.util.ArrayList;

public class Factura {
    private String numero;
    private String fecha;
    private Client cliente;
    private ArrayList<LineaFactura> lineas = new ArrayList<>();

    public Client getCliente() {
        return cliente;
    }

    public Factura(String numero, String fecha, Client cliente) {
        this.numero = numero;
        this.fecha = fecha;
        this.cliente = cliente;
    }

    public void agregarLinea(LineaFactura linea) {
        lineas.add(linea);
    }

    public ArrayList<LineaFactura> getLineas() {
        return lineas;
    }

    public double calcularTotal() {
        return lineas.stream().mapToDouble(linea -> {
            double subtotal = linea.getSubtotal(); // precio * cantidad
            double impuesto = linea.calcularImpuesto(); // Usar método de LineaFactura
            return subtotal + impuesto;
        }).sum();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Factura: ").append(numero).append("\n");
        sb.append("Cliente: ").append(cliente.getNombre()).append("\n");
        sb.append("Fecha: ").append(fecha).append("\n");
        sb.append("Productos:\n");

        double totalSinImpuestos = 0;
        double totalImpuestos = 0;

        for (LineaFactura l : lineas) {
            double subtotal = l.getSubtotal();
            double impuesto = l.calcularImpuesto(); // Usar el método correcto

            sb.append("- ").append(l.getProducto().getDescription())
                    .append(" x").append(l.getCantidad())
                    .append(" = $").append(String.format("%.2f", subtotal))
                    .append(" + $").append(String.format("%.2f", impuesto))
                    .append(" (").append(l.getProducto().getTipodeImpuesto().getTipo()).append(")")
                    .append("\n");

            totalSinImpuestos += subtotal;
            totalImpuestos += impuesto;
        }

        sb.append("\nSUBTOTAL: $").append(String.format("%.2f", totalSinImpuestos)).append("\n");
        sb.append("IMPUESTOS: $").append(String.format("%.2f", totalImpuestos)).append("\n");
        sb.append("TOTAL: $").append(String.format("%.2f", calcularTotal()));
        return sb.toString();
    }
}
