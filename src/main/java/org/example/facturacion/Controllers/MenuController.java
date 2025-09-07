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
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
    private Label welcomeText;
    @FXML
    private Button btnEnterUser;
    @FXML
    private Button btnEnterProducts;
    @FXML
    private Button btnBill;
    @FXML
    private Button btnExit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configurar texto de bienvenida
        welcomeText.setText("Sistema de Facturación Básico");

        // Configurar eventos de botones
        btnEnterUser.setOnAction(this::abrirClientes);
        btnEnterProducts.setOnAction(this::abrirProductos);
        btnBill.setOnAction(this::abrirFacturacion);
        btnExit.setOnAction(this::salirAplicacion);
    }

    private void abrirClientes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/facturacion/Clients-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 584, 424);
            stage.setTitle("Gestión de Clientes");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la ventana de clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void abrirProductos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/facturacion/Productos-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 600, 400);
            stage.setTitle("Gestión de Productos");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la ventana de productos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void abrirFacturacion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/facturacion/Factura-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 700, 500);
            stage.setTitle("Facturación");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la ventana de facturación: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void salirAplicacion(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}