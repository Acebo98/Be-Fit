package com.example.befit;

public class VOPeso {
    private int Identificador;
    private String Peso_1;
    private String Peso_2;
    private String Peso_3;
    private String Peso_4;
    private String Notas;
    private int IdSesion;

    //Setters
    public void setIdentificador(int identificador) {
        Identificador = identificador;
    }
    public void setPeso_1(String peso_1) {
        Peso_1 = peso_1;
    }
    public void setPeso_2(String peso_2) {
        Peso_2 = peso_2;
    }
    public void setPeso_3(String peso_3) {
        Peso_3 = peso_3;
    }
    public void setPeso_4(String peso_4) {
        Peso_4 = peso_4;
    }
    public void setIdSesion(int idSesion) {
        IdSesion = idSesion;
    }
    public void setNotas(String notas) {
        Notas = notas;
    }

    //Getters
    public int getIdentificador() {
        return Identificador;
    }
    public String getPeso_1() {
        return Peso_1;
    }
    public String getPeso_2() {
        return Peso_2;
    }
    public String getPeso_3() {
        return Peso_3;
    }
    public String getPeso_4() {
        return Peso_4;
    }
    public String getNotas() {
        return Notas;
    }
    public int getIdSesion() {
        return IdSesion;
    }
}