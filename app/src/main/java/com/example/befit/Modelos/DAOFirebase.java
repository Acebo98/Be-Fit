package com.example.befit.Modelos;

import com.example.befit.Entidades.VOFeedback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DAOFirebase {

    public final String REPORTE = "Reporte";

    //Base de datos...
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public DAOFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    //Inseramos un reporte
    public void insertarReporte(VOFeedback feedback) throws Exception {
        try {
            feedback.setIdentificador(databaseReference.push().getKey());           //Identificador...
            feedback.setFecha(DAOSesiones.sacarFechaHoy());                         //Fecha de hoy...
            databaseReference.child(REPORTE).child(String.valueOf(feedback.getIdentificador())).setValue(feedback);
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }
    }
}