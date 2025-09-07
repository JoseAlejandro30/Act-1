
package org.example.facturacion.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;

import java.net.URL;
import java.util.*;

class Factura {
    private String numero;
    private String fecha;
    private Client cliente;
    private ArrayList<LineaFactura> lineas = new ArrayList<>();
    public Client getCliente(){
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

    public double calcularTotal() {
        return lineas.stream().mapToDouble(l ->
                l.getSubtotal() + l.getProducto().calcularImpuesto()).sum();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Factura: ").append(numero).append("\n");
        sb.append("Cliente: ").append(cliente).append("\n");
        sb.append("Fecha: ").append(fecha).append("\n");
        sb.append("Productos:\n");
        for(LineaFactura l : lineas) {
            sb.append("- ").append(l.getProducto().getDescription())
                    .append(" x").append(l.getCantidad())
                    .append(" = $").append(l.getSubtotal()).append("\n");
        }
        sb.append("TOTAL: $").append(calcularTotal());
        return sb.toString();
    }

}
   public class FacturaController implements Initializable {
       @FXML private TextField txtBuscar;
       @FXML private Button btnBuscar;

       @FXML private TableView<Client> clientesTable;
       @FXML private TableColumn<Client, String> colClienteId;
       @FXML private TableColumn<Client, String> colClienteNombre;

       @FXML private TableView<Producto> productosTable;
       @FXML private TableColumn<Producto, String> colProdDesc;
       @FXML private TableColumn<Producto, Float> colProdPrecio;
       @FXML private TableColumn<Producto, Integer> colProdStock;
       @FXML private TableColumn<Producto, String> colProdImpuesto;

       @FXML private Label lblTotal;

       private static ArrayList<Client> clientesRegistrados = new ArrayList<>();
       private static ArrayList<Producto> productosRegistrados = new ArrayList<>();

       private ObservableList<Client> clientesTabla = FXCollections.observableArrayList();
       private ObservableList<Producto> productosTabla = FXCollections.observableArrayList();
       private FilteredList<Client> clientesFiltrados;



       @Override
       public void initialize(URL url, ResourceBundle resourceBundle) {
           configurarTablas();
           cargarDatos();
           configurarEventos();
       }
       private void configurarTablas() {
           // Tabla de clientes
           colClienteId.setCellValueFactory(new PropertyValueFactory<>("id"));
           colClienteNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

           // Tabla de productos
           colProdDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
           colProdPrecio.setCellValueFactory(new PropertyValueFactory<>("price"));
           colProdStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
           colProdImpuesto.setCellValueFactory(cell ->
                   new ReadOnlyStringWrapper(cell.getValue().getTipodeImpuesto().getTipo()));

           productosTable.setItems(productosTabla);
       }

       private void cargarDatos() {
           // Cargar clientes registrados
           clientesTabla.setAll(clientesRegistrados);
           clientesFiltrados = new FilteredList<>(clientesTabla, p -> true);
           clientesTable.setItems(clientesFiltrados);

           // Cargar todos los productos inicialmente
           productosTabla.setAll(productosRegistrados);
           actualizarTotal();
       }

       private void configurarEventos() {
           // Selección de cliente
           clientesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldClient, newClient) -> {
               if (newClient != null) {
                   mostrarProductosDelCliente(newClient);
               } else {
                   productosTabla.setAll(productosRegistrados);
                   actualizarTotal();
               }
           });

           btnBuscar.setOnAction(this::buscarCliente);
       }

       @FXML
       private void buscarCliente(ActionEvent event) {
           String criterio = txtBuscar.getText().trim().toLowerCase();

           if (criterio.isEmpty()) {
               clientesFiltrados.setPredicate(client -> true);
           } else {
               clientesFiltrados.setPredicate(client ->
                       client.getId().toLowerCase().contains(criterio) ||
                               client.getNombre().toLowerCase().contains(criterio)
               );
           }

           if (!clientesFiltrados.isEmpty()) {
               clientesTable.getSelectionModel().selectFirst();
           }
       }

       private void mostrarProductosDelCliente(Client cliente) {
           // Por simplicidad, mostrar todos los productos
           // En una aplicación real, aquí filtrarías por cliente
           productosTabla.setAll(productosRegistrados);
           actualizarTotal();
       }

       private void actualizarTotal() {
           double total = 0;
           for (Producto p : productosTabla) {
               double precio = p.getPrice();
               double impuesto = p.getTipodeImpuesto().calcularImpuesto(precio);
               total += precio + impuesto;
           }
           lblTotal.setText(String.format("$%.2f", total));
       }

       // MÉTODOS ESTÁTICOS para que otros controladores agreguen datos
       public static void agregarCliente(Client cliente) {
           clientesRegistrados.add(cliente);
       }

       public static void agregarProducto(Producto producto) {
           productosRegistrados.add(producto);
       }

       public static ArrayList<Client> getClientesRegistrados() {
           return clientesRegistrados;
       }

       public static ArrayList<Producto> getProductosRegistrados() {
           return productosRegistrados;
       }
   }
