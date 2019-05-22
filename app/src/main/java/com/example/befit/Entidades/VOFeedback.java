package com.example.befit.Entidades;

import java.util.Date;

public class VOFeedback {
    private String identificador;
    private String titulo;
    private String cuerpo;
    private String tipo;
    private String fecha;

    //Setters
    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    //Getters
    public String getIdentificador() {
        return identificador;
    }
    public String getTitulo() {
        return titulo;
    }
    public String getCuerpo() {
        return cuerpo;
    }
    public String getTipo() {
        return tipo;
    }
    public String getFecha() {
        return fecha;
    }
}