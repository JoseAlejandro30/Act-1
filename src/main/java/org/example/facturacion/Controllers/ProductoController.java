package org.example.facturacion.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

class Producto  {
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
        if(price > 0){
            return price;
        }
        return 0;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStock() {
        if(stock < 0 || stock == 0 ){
            System.out.println("ERROR DIGITE UN VALOR VALIDO");
        }
        return stock;
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
}
public class ProductoController  implements Initializable{

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
      btnGuardar.setOnAction(event ->{
        Guardar();
        });
    }
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
    private RadioButton rbtnWithTax;
    @FXML
    private Button btnGuardar;
    ArrayList<Producto> productos= new ArrayList<>();
    public void Guardar(){
        String id = txtID.getText();
        String description = txtDescription.getText();
        String Sprice = (txtPrice.getText());
        String Sstock = (txtStock.getText());
        String Stax = (txtTax.getText());

        if(id.isEmpty() || description.isEmpty() || Sprice.isEmpty() || Sstock.isEmpty() || Stax.isEmpty()){
            System.out.println("Los Campos no pueden estar Vacios");
            txtID.requestFocus();
        }
        float price = Float.parseFloat(txtPrice.getText());
        int stock = Integer.parseInt(txtStock.getText());
        float tax = Float.parseFloat(txtTax.getText());

        Producto producto = new Producto(id , description, price, stock, tax);
        productos.add(producto);
        for(Producto producto1 : productos){
            System.out.println(producto1);
            System.out.println();
        }
    }
    public void MostrarAlerta(){

    }

}
