package com.example.befit.Entidades;

import java.util.Date;

public class VOConfiGraficas {
    private Date FechaAnterior;
    private Date FechaProxima;
    private String EstadoSesion;

    //Getters
    public Date getFechaAnterior() {
        return FechaAnterior;
    }
    public Date getFechaProxima() {
        return FechaProxima;
    }
    public String getEstadoSesion() {
        return EstadoSesion;
    }

    //Setters
    public void setFechaAnterior(Date fechaAnterior) {
        FechaAnterior = fechaAnterior;
    }
    public void setFechaProxima(Date fechaProxima) {
        FechaProxima = fechaProxima;
    }
    public void setEstadoSesion(String estadoSesion) {
        EstadoSesion = estadoSesion;
    }
}