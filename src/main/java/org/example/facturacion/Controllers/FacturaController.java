package org.example.facturacion.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.stage.Stage;
import org.example.facturacion.Classes.Client;
import org.example.facturacion.Classes.Factura;
import org.example.facturacion.Classes.LineaFactura;
import org.example.facturacion.Classes.Producto;

import java.util.*;


public class FacturaController implements Initializable {
    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnNuevaFactura;
    @FXML
    private Button btnGuardarFactura;
    @FXML
    private Button btnImprimir;
    @FXML
    private Button btnVolverMenu;
    @FXML
    private TableView<Client> clientesTable;
    @FXML
    private TableColumn<Client, String> colClienteId;
    @FXML
    private TableColumn<Client, String> colClienteNombre;
    @FXML
    private TableColumn<Client, String> colClienteEmail;

    @FXML
    private TableView<Producto> productosTable;
    @FXML
    private TableColumn<Producto, String> colProdId;
    @FXML
    private TableColumn<Producto, String> colProdDesc;
    @FXML
    private TableColumn<Producto, Float> colProdPrecio;
    @FXML
    private TableColumn<Producto, Integer> colProdStock;
    @FXML
    private TableColumn<Producto, String> colProdImpuesto;

    @FXML
    private Label lblClienteSeleccionado;
    @FXML
    private Label lblTotal;

    private ObservableList<Client> clientesTabla = FXCollections.observableArrayList();
    private ObservableList<Producto> productosTabla = FXCollections.observableArrayList();
    private FilteredList<Client> clientesFiltrados;

    // Nueva factura en proceso
    private Factura facturaActual;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // CORRECCIÓN: inicializar clientesFiltrados antes de asignar al TableView
        clientesFiltrados = new FilteredList<>(clientesTabla, p -> true);
        btnNuevaFactura.setOnAction((e) -> nuevaFactura(e));
        btnGuardarFactura.setOnAction((e) -> guardarFactura(e));
        btnImprimir.setOnAction((e) -> imprimirFactura(e));
        btnVolverMenu.setOnAction((e) -> volverAlMenu(e));
        configurarTablas();
        cargarDatos();
        configurarEventos();
    }

    private void configurarTablas() {
        // Configuración de tabla de clientes
        colClienteId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colClienteNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        if (colClienteEmail != null) {
            colClienteEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        }

        // CORRECCIÓN: Configuración correcta de tabla de productos
        // Los nombres deben coincidir EXACTAMENTE con los métodos getter
        colProdId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProdDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colProdPrecio.setCellValueFactory(new PropertyValueFactory<>("price"));
        colProdStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // Para el impuesto, usamos un enfoque personalizado ya que necesitamos acceder a un método anidado
        colProdImpuesto.setCellValueFactory(cellData -> {
            Producto producto = cellData.getValue();
            if (producto != null && producto.getTipodeImpuesto() != null) {
                return new ReadOnlyStringWrapper(producto.getTipodeImpuesto().getTipo());
            }
            return new ReadOnlyStringWrapper("N/A");
        });

        clientesTable.setItems(clientesFiltrados);
        productosTable.setItems(productosTabla);

        // Configurar el placeholder para cuando no hay datos
        productosTable.setPlaceholder(new Label("Seleccione un cliente para ver sus productos"));
    }

    private void cargarDatos() {
        // Cargar clientes desde la lista estática del ClientsController
        clientesTabla.setAll(ClientsController.getClients());
        productosTabla.clear();
        actualizarTotal();

        // Debug: Verificar que hay clientes cargados
        System.out.println("Clientes cargados: " + clientesTabla.size());
    }

    private void configurarEventos() {
        clientesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldClient, newClient) -> {
            if (newClient != null) {
                System.out.println("Cliente seleccionado: " + newClient.getNombre() + " con " + newClient.getProductos().size() + " productos");
                mostrarProductosDelCliente(newClient);
                crearNuevaFactura(newClient);
                actualizarLabelCliente(newClient);
            } else {
                productosTabla.clear();
                facturaActual = null;
                actualizarLabelCliente(null);
                actualizarTotal();
            }
        });

        productosTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && facturaActual != null) {
                Producto productoSeleccionado = productosTable.getSelectionModel().getSelectedItem();
                if (productoSeleccionado != null) {
                    agregarProductoAFactura(productoSeleccionado);
                }
            }
        });

        btnBuscar.setOnAction(this::buscarCliente);
        if (btnLimpiar != null) {
            btnLimpiar.setOnAction(this::limpiarBusqueda);
        }
    }

    @FXML
    private void buscarCliente(ActionEvent event) {
        String criterio = txtBuscar.getText().trim().toLowerCase();
        if (criterio.isEmpty()) {
            clientesFiltrados.setPredicate(client -> true);
            clientesTable.getSelectionModel().clearSelection();
        } else {
            clientesFiltrados.setPredicate(client ->
                    client.getId().toLowerCase().contains(criterio) ||
                            client.getNombre().toLowerCase().contains(criterio) ||
                            client.getEmail().toLowerCase().contains(criterio)
            );
            if (!clientesFiltrados.isEmpty()) {
                clientesTable.getSelectionModel().selectFirst();
                Client clienteEncontrado = clientesTable.getSelectionModel().getSelectedItem();
                System.out.println("Cliente encontrado - ID: " + clienteEncontrado.getId() + ", Nombre: " + clienteEncontrado.getNombre() + ", Productos: " + clienteEncontrado.getProductos().size());
            } else {
                System.out.println("No se encontraron clientes con el criterio: " + criterio);
                clientesTable.getSelectionModel().clearSelection();
            }
        }
    }

    @FXML
    private void limpiarBusqueda(ActionEvent event) {
        txtBuscar.clear();
        clientesFiltrados.setPredicate(client -> true);
        clientesTable.getSelectionModel().clearSelection();
        productosTabla.clear();
        facturaActual = null;
        actualizarLabelCliente(null);
        actualizarTotal();
    }

    private void mostrarProductosDelCliente(Client cliente) {
        productosTabla.clear();
        if (cliente != null && cliente.getProductos() != null) {
            productosTabla.addAll(cliente.getProductos());
            System.out.println("Productos del cliente " + cliente.getNombre() + ": " + cliente.getProductos().size());
            for (Producto p : cliente.getProductos()) {
                System.out.println("  - ID: " + p.getId() + ", Desc: " + p.getDescription() + ", Precio: $" + p.getPrice() + ", Stock: " + p.getStock() + ", Impuesto: " + p.getTipodeImpuesto().getTipo());
            }

            // Refrescar la tabla para asegurar que se muestren los datos
            productosTable.refresh();
        } else {
            System.out.println("El cliente no tiene productos o es null");
        }
    }

    private void actualizarLabelCliente(Client cliente) {
        if (lblClienteSeleccionado != null) {
            if (cliente != null) {
                lblClienteSeleccionado.setText("Cliente: " + cliente.getId() + " - " + cliente.getNombre());
            } else {
                lblClienteSeleccionado.setText("Ningún cliente seleccionado");
            }
        }
    }

    private void crearNuevaFactura(Client cliente) {
        String numeroFactura = "F-" + System.currentTimeMillis();
        String fechaActual = new Date().toString();
        facturaActual = new Factura(numeroFactura, fechaActual, cliente);
        System.out.println("Nueva factura creada para cliente: " + cliente.getNombre());
        actualizarTotal();
    }

    // MÉTODO CORREGIDO - ESTE ES EL CAMBIO PRINCIPAL
    private void agregarProductoAFactura(Producto producto) {
        if (facturaActual == null) return;
        if (producto.getStock() <= 0) {
            mostrarAlerta("Stock Insuficiente", "El producto no tiene stock disponible.", Alert.AlertType.WARNING);
            return;
        }

        // Permitir al usuario elegir la cantidad
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Cantidad");
        dialog.setHeaderText("Agregar producto: " + producto.getDescription());
        dialog.setContentText("Ingrese la cantidad (máximo " + producto.getStock() + "):");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int cantidadSolicitada = Integer.parseInt(result.get());
                if (cantidadSolicitada > 0 && cantidadSolicitada <= producto.getStock()) {
                    LineaFactura linea = new LineaFactura(producto, cantidadSolicitada);
                    facturaActual.agregarLinea(linea);

                    // Actualizar el stock del producto
                    producto.setStock(producto.getStock() - cantidadSolicitada);

                    System.out.println("Producto agregado a la factura: " + producto.getDescription() + " x" + cantidadSolicitada);
                    System.out.println("Stock restante: " + producto.getStock());

                    // Refrescar la tabla de productos para mostrar el stock actualizado
                    productosTable.refresh();
                    actualizarTotal();
                } else {
                    mostrarAlerta("Cantidad Inválida", "La cantidad debe ser entre 1 y " + producto.getStock(), Alert.AlertType.WARNING);
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "Por favor ingrese un número válido.", Alert.AlertType.ERROR);
            }
        }
    }

    private void actualizarTotal() {
        double total = 0;
        if (facturaActual != null) {
            total = facturaActual.calcularTotal();
            System.out.println("Total calculado: $" + total); // Debug
        }
        if (lblTotal != null) lblTotal.setText(String.format("Total: $%.2f", total));
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // MÉTODOS PARA LOS BOTONES DE LA PARTE INFERIOR
    @FXML
    private void nuevaFactura(ActionEvent event) {
        // Limpiar la factura actual
        facturaActual = null;
        clientesTable.getSelectionModel().clearSelection();
        productosTabla.clear();
        actualizarLabelCliente(null);
        actualizarTotal();
        txtBuscar.clear();
        clientesFiltrados.setPredicate(client -> true);

        mostrarAlerta("Nueva Factura", "Seleccione un cliente para crear una nueva factura.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void guardarFactura(ActionEvent event) {
        if (facturaActual == null || facturaActual.getLineas().isEmpty()) {
            mostrarAlerta("Error", "No hay una factura válida para guardar.", Alert.AlertType.WARNING);
            return;
        }

        // Aquí podrías agregar lógica para guardar en archivo o base de datos
        System.out.println("Factura guardada:");
        System.out.println(facturaActual.toString());

        mostrarAlerta("Factura Guardada",
                "Factura guardada exitosamente.\nTotal: $" + String.format("%.2f", facturaActual.calcularTotal()),
                Alert.AlertType.INFORMATION);
    }

    @FXML
    private void imprimirFactura(ActionEvent event) {
        if (facturaActual == null || facturaActual.getLineas().isEmpty()) {
            mostrarAlerta("Error", "No hay una factura válida para imprimir.", Alert.AlertType.WARNING);
            return;
        }

        // Mostrar la factura en consola (simular impresión)
        System.out.println("=== IMPRIMIENDO FACTURA ===");
        System.out.println(facturaActual.toString());
        System.out.println("=========================");

        mostrarAlerta("Impresión",
                "Factura enviada a imprimir.\n(Ver consola para detalles)",
                Alert.AlertType.INFORMATION);
    }

    @FXML
    private void volverAlMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/facturacion/Menu.view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("MENÚ");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar el menú principal: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
}