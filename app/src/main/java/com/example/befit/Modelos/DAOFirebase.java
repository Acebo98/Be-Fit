package com.example.befit.Modelos;

import android.content.Context;

import com.example.befit.Entidades.VOFeedback;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DAOFirebase {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    final String REPORTE = "Reporte";

    public DAOFirebase(Context context) {
        FirebaseApp.initializeApp(context);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    //Insertamos un reporte
    public void insertarReporte(VOFeedback feedback) throws Exception {
        try {
            databaseReference.child(REPORTE).child(String.valueOf(feedback.getIdentificador())).setValue(feedback);
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }
    }
}