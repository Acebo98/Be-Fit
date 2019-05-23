package com.example.befit.Entidades;

public class VOConfiGraficas {
    private String FechaAnterior;
    private String FechaProxima;
    private String EstadoSesion;

    //Getters
    public String getFechaAnterior() {
        return FechaAnterior;
    }
    public String getFechaProxima() {
        return FechaProxima;
    }
    public String getEstadoSesion() {
        return EstadoSesion;
    }

    //Setters
    public void setFechaAnterior(String fechaAnterior) {
        FechaAnterior = fechaAnterior;
    }
    public void setFechaProxima(String fechaProxima) {
        FechaProxima = fechaProxima;
    }
    public void setEstadoSesion(String estadoSesion) {
        EstadoSesion = estadoSesion;
    }
}