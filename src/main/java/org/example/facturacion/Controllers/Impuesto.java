package org.example.facturacion.Controllers;

import java.util.ArrayList;

public interface Impuesto {
    double calcularImpuesto(double Subtotal);
    String getTipo();
}
class IVA implements Impuesto {
    @Override
    public double calcularImpuesto(double Subtotal) {
        return Subtotal * 0.19;
    }

    @Override
    public String getTipo() {
        return "IVA 19%";
    }
}
class Excento implements Impuesto {
    @Override
    public double calcularImpuesto(double Subtotal) {
        return 0;
    }

    @Override
    public String getTipo() {
        return "EXCENTO";
    }
}