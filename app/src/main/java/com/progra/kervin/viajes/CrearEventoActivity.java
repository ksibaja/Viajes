package com.progra.kervin.viajes;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CrearEventoActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG ="CrearEventoActivity";
    TextView textFecha;
    TextInputLayout inputFecha;
    String fecha, correoOrg, nombreOrg, telefonoOrg, id;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    EditText textLugar, textCosto, textDetalle; //textCategoria,
    Spinner mSpinner;
    Button botonAgregar, botonImagen;
    Map<String, Object> evento;

    // -------------------------------------
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DocumentReference documentReference;
    DocumentSnapshot document;
    FirebaseStorage firebaseStorage;
    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;
    StorageReference storageReference, filePath;
    private static final int Selected = 1;
    UploadTask uploadTask;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);

        // firebase
        iniciarFirebase();

        textFecha = findViewById(R.id.txtFecha);
        inputFecha = findViewById(R.id.inputFecha);
        fecha();
        getDatos();

        // variable para boton botonAgregar
        botonAgregar = findViewById(R.id.btnAgregar);
        botonAgregar.setOnClickListener(this);
        botonImagen = findViewById(R.id.btnImagen);
        botonImagen.setOnClickListener(this);

        // variables para paseo/evento
        textLugar = findViewById(R.id.txtLugar);
        textFecha = findViewById(R.id.txtFecha);
        textCosto = findViewById(R.id.txtCosto);
        textDetalle = findViewById(R.id.txtDetalle);
        mSpinner = findViewById(R.id.spinner);

        String[] opciones = {"Paseo","Excursión","Caminata","Campamento"};
        ArrayAdapter <String> adapter = new  ArrayAdapter <String>(this, android.R.layout.simple_spinner_item, opciones);
        mSpinner.setAdapter(adapter);
    }

    private void iniciarFirebase() {
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    public void getDatos() {
        correoOrg = mUser.getEmail();

        documentReference = firebaseFirestore.collection("Datos").document(correoOrg);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    document = task.getResult();
                    nombreOrg = document.getData().get("nombre").toString();
                    telefonoOrg = document.getData().get("telefono").toString();
                } else {
                    Toast.makeText(CrearEventoActivity.this, "Fallido... ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == botonAgregar.getId()) {
            agregarPaseo();
        } else if (view.getId() == botonImagen.getId()){
            insertarImagen();
        }
    }

    public void fecha(){
        textFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int dia = calendar.get(Calendar.DAY_OF_MONTH);
                int mes = calendar.get(Calendar.MONTH);
                int año = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CrearEventoActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        año,mes,dia);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int año, int mes, int dia) {
                mes++;
                Log.d(TAG, ""+dia+"/"+mes+"/"+año);

                fecha = mes+"/"+dia+"/"+año;
                textFecha.setText(fecha);
            }
        };
    }

    // botonImagen
    public void insertarImagen(){
        id = UUID.randomUUID().toString();
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Selected);
    }

    @Override   // botonImagen seleccionada
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Selected && resultCode == RESULT_OK){
            uri = data.getData();
            filePath = storageReference.child("Paseo/"+id).child(uri.getLastPathSegment());
            uploadTask = filePath.putFile(uri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Fallido: "+e,Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(CrearEventoActivity.this, "Imagen agregada", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //botonAgregar viaje/evento/paseo
    public void agregarPaseo(){
        String categoria = mSpinner.getSelectedItem().toString();
        String lugar = textLugar.getText().toString();
        String costo = textCosto.getText().toString();
        String detalle = textDetalle.getText().toString();

        if (categoria.equals("")||lugar.equals("")||fecha.equals("")
                ||costo.equals("")||detalle.equals("")) {
            validarTxt(categoria,lugar,fecha,costo,detalle);
        }
        else {
            if(uri == null ){
                Toast.makeText(this, "Debe agregar una imagen", Toast.LENGTH_SHORT).show();
            } else {
                collectionReference = firebaseFirestore.collection("Datos");
                evento = new HashMap<>();
                evento.put("idEvento", id);
                evento.put("tipoUser", 3);
                evento.put("organizador", nombreOrg);
                evento.put("correo", correoOrg);
                evento.put("categoria", categoria);
                evento.put("telefono", telefonoOrg);
                evento.put("lugar", lugar);
                evento.put("fecha", fecha);
                evento.put("costo", costo);
                evento.put("detalle", detalle);

                collectionReference.document(id).set(evento);
                Toast.makeText(CrearEventoActivity.this, "Paseo agregado", Toast.LENGTH_SHORT).show();
                limpiarTxt();
            }
        }
    }

    public void validarTxt(String categoria, String lugar, String fecha, String costo, String detalle) {
        if (lugar.equals("")) {
            textLugar.setError("Espacio requerido");
        } else if (fecha.equals("")) {
            textFecha.setError("Espacio requerido");
        } else if (costo.equals("")) {
            textCosto.setError("Espacio requerido");
        } else if (detalle.equals("")) {
            textDetalle.setError("Espacio requerido");
        }
    }

    public void limpiarTxt() {
        //textCategoria.setText("");
        textLugar.setText("");
        textFecha.setText("");
        textCosto.setText("");
        textDetalle.setText("");
    }

}
