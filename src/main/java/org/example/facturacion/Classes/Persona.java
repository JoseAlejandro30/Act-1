package org.example.facturacion.Classes;

public class Persona {
    private String id;
    private String nombre;
    private String email;
    private String identificacion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public Persona() {
    }

    public Persona(String id, String nombre, String email, String identificacion) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.identificacion = identificacion;
    }

}
