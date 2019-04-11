package com.example.befit;

public class VOSesion {
    private int Identificador;
    private String Nombre;
    private String Musculo_1;
    private String Musculo_2;
    private String Musculo_3;
    private String Musculo_4;
    private String Actualizacion;

    //Getters
    public int getIdentificador() {
        return Identificador;
    }
    public String getNombre() {
        return Nombre;
    }
    public String getMusculo_1() {
        return  Musculo_1;
    }
    public String getMusculo_2() {
        return Musculo_2;
    }
    public String getMusculo_3() {
        return Musculo_3;
    }
    public String getMusculo_4() {
        return Musculo_4;
    }
    public String getActualizacion() {
        return Actualizacion;
    }

    //Setters
    public void setIdentificador(int identificador) {
        Identificador = identificador;
    }
    public void setNombre(String nombre) {
        Nombre = nombre;
    }
    public void setMusculo_1(String musculo_1) {
        Musculo_1 = musculo_1;
    }
    public void setMusculo_2(String musculo_2) {
        Musculo_2 = musculo_2;
    }
    public void setMusculo_3(String musculo_3) {
        Musculo_3 = musculo_3;
    }
    public void setMusculo_4(String musculo_4) {
        Musculo_4 = musculo_4;
    }
    public void setActualizacion(String actualizacion) {
        Actualizacion = actualizacion;
    }
}