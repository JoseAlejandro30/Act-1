package org.example.facturacion.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import org.example.facturacion.Classes.Client;
import org.example.facturacion.Classes.Producto;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductoController implements Initializable {

    @FXML private TextField txtID;
    @FXML private TextField txtDescription;
    @FXML private TextField txtPrice;
    @FXML private TextField txtStock;
    @FXML private RadioButton rbtnWithoutTax;
    @FXML private RadioButton rbtnWithTaxIVA;
    @FXML private Button btnGuardar;
    @FXML private Button btnBack;

    @FXML private ComboBox<Client> cmbClientes;
    @FXML private Label lblCliente;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup taxGroup = new ToggleGroup();
        rbtnWithTaxIVA.setToggleGroup(taxGroup);
        rbtnWithoutTax.setToggleGroup(taxGroup);
        rbtnWithTaxIVA.setSelected(true);

        btnGuardar.setOnAction(e -> guardar());
        btnBack.setOnAction(e -> Salir(e));

        cargarClientes();

        if (cmbClientes != null) {
            cmbClientes.setOnShowing(e -> cargarClientes());
        }
    }

    private void cargarClientes() {
        if (cmbClientes != null) {
            // Obtenemos los clientes de la lista estática en ClientsController
            cmbClientes.setItems(FXCollections.observableArrayList(ClientsController.getClients()));

            // Mostrar id - nombre
            cmbClientes.setCellFactory(lv -> new ListCell<Client>() {
                @Override
                protected void updateItem(Client client, boolean empty) {
                    super.updateItem(client, empty);
                    setText(empty || client == null ? "" : client.getId() + " - " + client.getNombre());
                }
            });

            cmbClientes.setButtonCell(new ListCell<Client>() {
                @Override
                protected void updateItem(Client client, boolean empty) {
                    super.updateItem(client, empty);
                    setText(empty || client == null ? "Seleccionar Cliente" : client.getId() + " - " + client.getNombre());
                }
            });
        }
    }

    public void guardar() {
        try {
            if (!validarCamposVacios()) {
                return;
            }

            if (cmbClientes != null && cmbClientes.getSelectionModel().getSelectedItem() == null) {
                mostrarAlerta("Campo Requerido", "Debe seleccionar un cliente para el producto.");
                if (cmbClientes != null) cmbClientes.requestFocus();
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

            // CORRECCIÓN: Usar directamente el cliente seleccionado
            Client clienteSeleccionado = cmbClientes.getSelectionModel().getSelectedItem();
            String clienteIdSeleccionado = clienteSeleccionado.getId();

            if (existeProducto(id)) {
                mostrarAlerta("Error de Duplicación", "Ya existe un producto con el ID: " + id);
                txtID.requestFocus();
                return;
            }

            Impuesto tipoImpuesto = rbtnWithTaxIVA.isSelected() ? new IVA() : new Excento();

            Producto producto = new Producto(id, description, price, stock, tipoImpuesto, clienteIdSeleccionado);

            // Agregar el producto directamente al cliente seleccionado
            clienteSeleccionado.agregarProducto(producto);

            System.out.println("Producto '" + producto.getDescription() + "' agregado al cliente: " + clienteSeleccionado.getNombre());
            System.out.println("Ahora el cliente tiene " + clienteSeleccionado.getProductos().size() + " productos.");

            mostrarAlerta("Éxito", "Producto guardado correctamente");
            limpiarCampos();

            System.out.println("Producto guardado: " + producto);

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta("Error Inesperado", "Ocurrió un error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validarCamposVacios() {
        if (txtID.getText().trim().isEmpty() || txtDescription.getText().trim().isEmpty() ||
                txtPrice.getText().trim().isEmpty() || txtStock.getText().trim().isEmpty()) {
            mostrarAlerta("Campo Requerido", "Todos los campos son obligatorios.");
            return false;
        }
        return true;
    }

    private float validarYConvertirFloat(String valor, String campo) throws NumberFormatException {
        return Float.parseFloat(valor);
    }

    private int validarYConvertirInt(String valor, String campo) throws NumberFormatException {
        return Integer.parseInt(valor);
    }

    private boolean existeProducto(String id) {
        return ClientsController.getClients().stream()
                .flatMap(client -> client.getProductos().stream())
                .anyMatch(p -> p.getId().equals(id));
    }

    public void Salir(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/facturacion/Menu.view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error: No se pudo cargar la interfaz anterior");
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        if (txtID != null) txtID.clear();
        if (txtDescription != null) txtDescription.clear();
        if (txtPrice != null) txtPrice.clear();
        if (txtStock != null) txtStock.clear();
        if (cmbClientes != null) cmbClientes.getSelectionModel().clearSelection();
        if (txtID != null) txtID.requestFocus();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}