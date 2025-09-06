module org.example.facturacion {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javafx.graphics;
    requires java.desktop;

    opens org.example.facturacion to javafx.fxml;
    exports org.example.facturacion;
    exports org.example.facturacion.Controllers;
    opens org.example.facturacion.Controllers to javafx.fxml;
}