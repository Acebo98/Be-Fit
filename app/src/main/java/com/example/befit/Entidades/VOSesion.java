package com.example.befit.Entidades;

public class VOSesion {
    private String Activo;
    private int Identificador;
    private String Nombre;
    private String Musculo_1;
    private String Musculo_2;
    private String Musculo_3;
    private String Musculo_4;
    private String Tag;
    private String FechaCreacion;
    private String Actualizacion;

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
        return  Musculo_1;
    }
    public String getEjercicio_2() {
        return Musculo_2;
    }
    public String getEjercicio_3() {
        return Musculo_3;
    }
    public String getEjercicio_4() {
        return Musculo_4;
    }
    public String getTag() { return this.Tag; }
    public String getFechaCreacion() { return this.FechaCreacion; }
    public String getActualizacion() { return Actualizacion; }

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
        Musculo_1 = musculo_1;
    }
    public void setEjercicio_2(String musculo_2) {
        Musculo_2 = musculo_2;
    }
    public void setEjercicio_3(String musculo_3) {
        Musculo_3 = musculo_3;
    }
    public void setEjercicio_4(String musculo_4) {
        Musculo_4 = musculo_4;
    }
    public void setTag(String tag) {
        Tag = tag;
    }
    public void setFechaCreacion(String fechaCreacion) {
        FechaCreacion = fechaCreacion;
    }
    public void setActualizacion(String actualizacion) {
        Actualizacion = actualizacion;
    }
}