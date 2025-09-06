package org.example.facturacion.Controllers;

public class Impuestos {
    private float Tax;
    private float Exempt;
    private float Price;

    public Impuestos() {
    }

    public Impuestos(float tax, float exempt, float price) {
        Tax = tax;
        Exempt = exempt;
        Price = price;
    }

    public float getTax() {
        return Tax;
    }

    public void setTax(float tax) {
        Tax = tax;
    }

    public float getExempt() {
        return Exempt;
    }

    public void setExempt(float exempt) {
        Exempt = exempt;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public float CalcularImpuestoIVA(float Precio){
        //Calcular impuesto con Iva
        return (float) (Precio -Precio * 0.19);
    }
    public float CalcularSinImpuesto(float Precio){
        return Precio;
    }
}
