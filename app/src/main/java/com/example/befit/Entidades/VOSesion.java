package com.example.befit.Entidades;

public class VOSesion {
    private String Activo;
    private int Identificador;
    private String Nombre;
    private String Ejercicio_1;
    private String Ejercicio_2;
    private String Ejercicio_3;
    private String Ejercicio_4;
    private String Tag;
    private String FechaCreacion;
    private String Actualizacion;
    private int IdTag;

    //Constructores (una sesión por defecto está activa)
    public VOSesion() {
        this.setActivo("s");
    }
    public VOSesion(String nombre, String actu) {
        super();
        this.setNombre(nombre);
        this.setActualizacion(actu);
    }

    //Getters
    public String getActivo() { return Activo; }
    public int getIdentificador() {
        return Identificador;
    }
    public String getNombre() {
        return Nombre;
    }
    public String getEjercicio_1() {
        return Ejercicio_1;
    }
    public String getEjercicio_2() {
        return Ejercicio_2;
    }
    public String getEjercicio_3() {
        return Ejercicio_3;
    }
    public String getEjercicio_4() {
        return Ejercicio_4;
    }
    public String getFechaCreacion() { return this.FechaCreacion; }
    public String getActualizacion() { return Actualizacion; }
    public int getIdTag() { return IdTag; }

    //Setters
    public void setActivo(String activo) {
        Activo = activo;
    }
    public void setIdentificador(int identificador) {
        Identificador = identificador;
    }
    public void setNombre(String nombre) {
        Nombre = nombre;
    }
    public void setEjercicio_1(String musculo_1) {
        Ejercicio_1 = musculo_1;
    }
    public void setEjercicio_2(String musculo_2) {
        Ejercicio_2 = musculo_2;
    }
    public void setEjercicio_3(String musculo_3) {
        Ejercicio_3 = musculo_3;
    }
    public void setEjercicio_4(String musculo_4) {
        Ejercicio_4 = musculo_4;
    }
    public void setFechaCreacion(String fechaCreacion) {
        FechaCreacion = fechaCreacion;
    }
    public void setActualizacion(String actualizacion) {
        Actualizacion = actualizacion;
    }
    public void setIdTag(int idTag) { IdTag = idTag; }
}