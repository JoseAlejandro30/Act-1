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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

class Client extends Persona{

    public Client() {
    }

    public Client(String id, String nombre, String email, String identificacion) {
        super(id, nombre, email, identificacion);
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



public class ClientsController implements Initializable {
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtIdentificacion;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnBack;

    private static ArrayList<Client> clients = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSave.setOnAction(event -> Guardar());
        btnBack.setOnAction(event -> Salir(event));
    }

    public void Guardar() {
        String id = txtId.getText();
        String nombre = txtName.getText();
        String email = txtEmail.getText();
        String identificacion = txtIdentificacion.getText();

        if (id.isEmpty() || nombre.isEmpty() || email.isEmpty() || identificacion.isEmpty()) {
            System.out.println("Por favor rellene todos los campos");
            txtId.requestFocus();
            return;
        }


        Client client = new Client(id, nombre, email, identificacion);
        clients.add(client);
        for (Client c : clients) {
            System.out.println(c);
        }
        System.out.println("Cliente guardado correctamente");
        FacturaController.agregarCliente(client);
    }

    private boolean existeCliente(String id) {
        return clients.stream().anyMatch(client -> client.getId().equals(id));
    }


    private void limpiarCampos() {
        txtId.clear();
        txtName.clear();
        txtEmail.clear();
        txtIdentificacion.clear();
        txtId.requestFocus();
    }


    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    public static ArrayList<Client> getClients() {
        return clients;
    }

    private void Salir(ActionEvent event) {
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
}

