package com.progra.kervin.viajes;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.progra.kervin.viajes.modelos.Paseo;

import java.util.UUID;

public class CrearEventoActivity extends AppCompatActivity implements View.OnClickListener{

    EditText nombreE, lugarE, fechaE, costoE, descripcionE;
    Button agregar, imagen;
    Paseo paseo;

    Uri uri;
    StorageReference filePath;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    static final int GALLERY_INTENT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);

        // firebase
        iniciarFirebase();

        // crea evento y define id
        paseo = new Paseo();
        paseo.setId(UUID.randomUUID().toString());

        // variable para boton agregar
        agregar = findViewById(R.id.btnAgregar);
        agregar.setOnClickListener(this);
        imagen = findViewById(R.id.btnImagen);
        imagen.setOnClickListener(this);


        // variables para paseo/evento
        nombreE = findViewById(R.id.txtNombre);
        lugarE = findViewById(R.id.txtLugar);
        fechaE = findViewById(R.id.txtFecha);
        costoE = findViewById(R.id.txtCosto);
        descripcionE = findViewById(R.id.txtDescripcion);

    }

    private void iniciarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == agregar.getId()) {
            agregarPaseo();
        } else if (view.getId() == imagen.getId()){
            insertarImagen();
        }
    }

    // agregar imagen
    public void insertarImagen(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_INTENT);

    }

    @Override   // imagen seleccionada
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            uri = data.getData();
            filePath = storageReference.child("Paseo/"+paseo.getId()).child(uri.getLastPathSegment());
            Toast.makeText(this, "Imagen agregada", Toast.LENGTH_LONG).show();
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
            if(uri==null || filePath==null){
                Toast.makeText(this, "Debe agregar una imagen", Toast.LENGTH_LONG).show();
            } else {
                paseo.setNombre(nombre);
                paseo.setLugar(lugar);
                paseo.setFecha(fecha);
                paseo.setCosto(costo);
                paseo.setDescripcion(descripcion);
                databaseReference.child("Paseo").child(paseo.getId()).setValue(paseo);
                filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });
                Toast.makeText(this, "Paseo agregado", Toast.LENGTH_LONG).show();
                limpiarTxt();
            }
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
