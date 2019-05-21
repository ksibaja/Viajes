package com.progra.kervin.viajes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.progra.kervin.viajes.modelos.Paseo;

import java.util.UUID;

public class CrearEventoActivity extends AppCompatActivity implements View.OnClickListener{

    EditText nombreE, lugarE, fechaE, costoE, descripcionE;
    Button agregar;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);

        // variable para boton agregar
        agregar = findViewById(R.id.btnAgregar);
        agregar.setOnClickListener(this);

        // variables para paseo/evento
        nombreE = findViewById(R.id.txtNombre);
        lugarE = findViewById(R.id.txtLugar);
        fechaE = findViewById(R.id.txtFecha);
        costoE = findViewById(R.id.txtCosto);
        descripcionE = findViewById(R.id.txtDescripcion);

        // firebase
        iniciarFirebase();

    }

    private void iniciarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==agregar.getId()) {
            agregarPaseo();
        }
    }
    //agregar viaje/evento/paseo
    public void agregarPaseo(){
        String nombre = nombreE.getText().toString();
        String lugar = lugarE.getText().toString();
        String fecha = fechaE.getText().toString();
        String costo = costoE.getText().toString();
        String descripcion = descripcionE.getText().toString();

        if (nombre.equals("")||lugar.equals("")||fecha.equals("")
                ||costo.equals("")||descripcion.equals("")) {
            validarTxt(nombre,lugar,fecha,costo,descripcion);
        }
        else {
            Paseo paseo = new Paseo();
            paseo.setId(UUID.randomUUID().toString());
            paseo.setNombre(nombre);
            paseo.setLugar(lugar);
            paseo.setFecha(fecha);
            paseo.setCosto(costo);
            paseo.setDescripcion(descripcion);
            databaseReference.child("Paseo").child(paseo.getId()).setValue(paseo);

            Toast.makeText(this, "Paseo agregado", Toast.LENGTH_LONG).show();
            limpiarTxt();
        }
    }

    private void validarTxt(String nombre, String lugar, String fecha, String costo, String descripcion) {
        if (nombre.equals("")) {
            nombreE.setError("Espacio requerido");
        } else if (lugar.equals("")) {
            lugarE.setError("Requerido");
        } else if (fecha.equals("")) {
            fechaE.setError("Requerido");
        } else if (costo.equals("")) {
            costoE.setError("Requerido");
        } else if (descripcion.equals("")) {
            descripcionE.setError("Requerido");
        }
    }

    private void limpiarTxt() {
        nombreE.setText("");
        lugarE.setText("");
        fechaE.setText("");
        costoE.setText("");
        descripcionE.setText("");
    }

}
