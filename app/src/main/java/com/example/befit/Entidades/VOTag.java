package com.example.befit.Entidades;

import com.example.befit.Estructura_BD.BeFitDB;

public class VOTag {
    private int Identificador;
    private String Tag;

    //Constructores
    public VOTag(int identificador, String tag) {
        Identificador = identificador;
        Tag = tag;
    }
    public VOTag() {
    }

    //Getters
    public int getIdentificador() {
        return Identificador;
    }
    public String getTag() {
        return Tag;
    }

    //Setters
    public void setIdentificador(int identificador) {
        Identificador = identificador;
    }
    public void setTag(String tag) {
        Tag = tag;
    }
}