package org.example.facturacion.Controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FacturaApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(org.example.facturacion.Controllers.MenuApplication.class.getResource("/org/example/facturacion/Factura.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle(" Facturacion ");
        stage.setScene(scene);
        stage.show();
    }
}
