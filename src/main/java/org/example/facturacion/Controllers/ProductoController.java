package org.example.facturacion.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

class Producto extends Impuestos{
    private String id;
    private String description;
    private float price;
    private int stock;
    private float tax;
    private boolean exempt;


    public Producto(String id, String description, float price, int stock, float tax) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.tax = tax;
    }
    @Override
    public float CalcularImpuestoIVA(float Precio) {
        return super.CalcularImpuestoIVA(Precio);
    }

    @Override
    public float CalcularSinImpuesto(float Precio) {
        return super.CalcularSinImpuesto(Precio);
    }
    public Producto() {
    }

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
        if (price > 0){
            return price;
        }else  {
            return 0;
        }

    }
    public void setPrice(float price) {
        this.price = price;
    }

    public int getStock() {
        if (stock > 0){
            return stock;
        }else  {
            return 0;
        }
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public boolean isExempt() {
        return exempt;
    }

    public void setExempt(boolean exempt) {
        this.exempt = exempt;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", tax=" + tax +
                ", exempt=" + exempt +
                '}';
    }
}

public class ProductoController implements Initializable {

    @FXML
    private TextField txtID;
    @FXML
    private TextField txtDescription;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtStock;
    @FXML
    private TextField txtTax;
    @FXML
    private RadioButton rbtnWithoutTax;
    @FXML
    private RadioButton rbtnWithTaxIVA;
    @FXML
    private Button btnGuardar;

    private ArrayList<Producto> productos = new ArrayList<>();
    private ToggleGroup taxGroup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        taxGroup = new ToggleGroup();
        rbtnWithTaxIVA.setToggleGroup(taxGroup);
        rbtnWithoutTax.setToggleGroup(taxGroup);
        rbtnWithTaxIVA.setSelected(true);
        btnGuardar.setOnAction(event -> guardar());
    }

    public void guardar() {
        try {
            if (!validarCamposVacios()) {
                return;
            }

            String id = txtID.getText().trim();
            String description = txtDescription.getText().trim();

            float price = validarYConvertirFloat(txtPrice.getText().trim(), "Precio");
            int stock = validarYConvertirInt(txtStock.getText().trim(), "Stock");
            float tax = 0;

            if (rbtnWithTaxIVA.isSelected()) {
                tax = validarYConvertirFloat(txtTax.getText().trim(), "Impuesto");
            }

            if (price < 0) {
                mostrarAlerta("Error de Validación", "El Precio no puede ser menor que 0.");
                txtPrice.requestFocus();
                return;
            }

            if (stock < 0) {
                mostrarAlerta("Error de Validación", "El Stock no puede ser menor que 0.");
                txtStock.requestFocus();
                return;
            }

            if (rbtnWithTaxIVA.isSelected() && tax < 0) {
                mostrarAlerta("Error de Validación", "El impuesto no puede ser negativo.");
                txtTax.requestFocus();
                return;
            }

            if (existeProducto(id)) {
                mostrarAlerta("Error de Duplicación", "Ya existe un producto con el ID: " + id);
                txtID.requestFocus();
                return;
            }

            Producto producto = new Producto(id, description, price, stock, tax);
            producto.setExempt(rbtnWithoutTax.isSelected());
            productos.add(producto);

            mostrarAlerta("Éxito", "Producto guardado correctamente.\nTotal de productos: " + productos.size());


            limpiarCampos();

            System.out.println("=== Lista de Productos ===");
            for (int i = 0; i < productos.size(); i++) {
                System.out.println((i + 1) + ". " + productos.get(i));
            }
            System.out.println("==========================");

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", "Por favor, ingrese valores numéricos válidos.");
        } catch (Exception e) {
            mostrarAlerta("Error Inesperado", "Ocurrió un error inesperado: " + e.getMessage());
        }
    }

    private boolean validarCamposVacios() {
        if (txtID.getText().trim().isEmpty()) {
            mostrarAlerta("Campo Requerido", "El ID del producto es obligatorio.");
            txtID.requestFocus();
            return false;
        }

        if (txtDescription.getText().trim().isEmpty()) {
            mostrarAlerta("Campo Requerido", "La descripción del producto es obligatoria.");
            txtDescription.requestFocus();
            return false;
        }

        if (txtPrice.getText().trim().isEmpty()) {
            mostrarAlerta("Campo Requerido", "El precio del producto es obligatorio.");
            txtPrice.requestFocus();
            return false;
        }

        if (txtStock.getText().trim().isEmpty()) {
            mostrarAlerta("Campo Requerido", "El stock del producto es obligatorio.");
            txtStock.requestFocus();
            return false;
        }

        if (rbtnWithTaxIVA.isSelected() && txtTax.getText().trim().isEmpty()) {
            mostrarAlerta("Campo Requerido", "El impuesto es obligatorio cuando está seleccionado 'Con Impuesto'.");
            txtTax.requestFocus();
            return false;
        }

        return true;
    }

    private float validarYConvertirFloat(String valor, String campo) throws NumberFormatException {
        try {
            return Float.parseFloat(valor);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("El campo '" + campo + "' debe ser un número decimal válido.");
        }
    }

    private int validarYConvertirInt(String valor, String campo) throws NumberFormatException {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("El campo '" + campo + "' debe ser un número entero válido.");
        }
    }

    private boolean existeProducto(String id) {
        return productos.stream().anyMatch(p -> p.getId().equals(id));
    }

    private void limpiarCampos() {
        txtID.clear();
        txtDescription.clear();
        txtPrice.clear();
        txtStock.clear();
        txtTax.clear();
        rbtnWithTaxIVA.setSelected(true);
        txtID.requestFocus();
    }

    public void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
