package org.example.facturacion.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


class Producto {
    private String id;
    private String description;
    private float price;
    private int stock;
    private Impuesto TipodeImpuesto;

    public Producto(String id, String description, float price, int stock, Impuesto tipodeImpuesto) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.stock = stock;
        TipodeImpuesto = tipodeImpuesto;
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
        if (price > 0) {
            return price;
        } else {
            return 0;
        }
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStock() {
        if (stock > 0) {
            return stock;
        } else {
            return 0;
        }
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

    public double calcularImpuesto() {
        double subtotal = price * stock;
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
        private RadioButton rbtnWithoutTax;
        @FXML
        private RadioButton rbtnWithTaxIVA;
        @FXML
        private Button btnGuardar;
        @FXML
        private Button btnBack;

        private ArrayList<Producto> productos = new ArrayList<>();


        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            ToggleGroup taxGroup = new ToggleGroup();
            rbtnWithTaxIVA.setToggleGroup(taxGroup);
            rbtnWithoutTax.setToggleGroup(taxGroup);
            rbtnWithTaxIVA.setSelected(true);
            btnGuardar.setOnAction(e -> guardar());
            btnBack.setOnAction(e -> Salir(e));
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


                if (existeProducto(id)) {
                    mostrarAlerta("Error de Duplicación", "Ya existe un producto con el ID: " + id);
                    txtID.requestFocus();
                    return;
                }

                Impuesto tipoImpuesto = rbtnWithTaxIVA.isSelected() ? new IVA() : new Excento();
                Producto producto = new Producto(id, description, price, stock, tipoImpuesto);
                productos.add(producto);

                System.out.println("=== Lista de Productos ===");
                for (int i = 0; i < productos.size(); i++) {
                    System.out.println((i + 1) + ". " + productos.get(i));
                }
                System.out.println("==========================");
                FacturaController.agregarProducto(producto);
                limpiarCampos();


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
        public void Salir (ActionEvent event){
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/facturacion/Menu.view.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                System.out.print(("Error"+ "No se pudo cargar la interfaz anterior"));
                e.printStackTrace();
            }
        }

        private void limpiarCampos() {
            if (txtID != null) txtID.clear();
            if (txtDescription != null) txtDescription.clear();
            if (txtPrice != null) txtPrice.clear();
            if (txtStock != null) txtStock.clear();
            if (txtID != null) txtID.requestFocus();
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

