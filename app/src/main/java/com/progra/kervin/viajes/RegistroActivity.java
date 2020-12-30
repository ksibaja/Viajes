package com.progra.kervin.viajes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.progra.kervin.viajes.modelos.Cliente;
import com.progra.kervin.viajes.modelos.Organizador;

import java.util.UUID;
import java.lang.Object;
import java.util.Map;
import java.util.HashMap;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputLayout  nombreUser, apellidoUser, telefonoUser, correoUser, contraUser, contraUser2;
    TextInputLayout nombreOrg, cedulaOrg, telefonoOrg, dirOrg, correoOrg, contraOrg, contraOrg2;
    Button btnUser, btnOrg, btnListo, btnListo2;
    Map<String, Object> userCliente;
    Map<String, Object> userOrg;

    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Initialize Firebase
        iniciarFirebase();

        nombreUser = findViewById(R.id.txtRegNombre);
        apellidoUser = findViewById(R.id.txtRegApellido);
        telefonoUser = findViewById(R.id.txtRegTelefono);
        correoUser = findViewById(R.id.txtRegCorreo);
        contraUser = findViewById(R.id.txtRegContra);
        contraUser2 = findViewById(R.id.txtRegContra2);

        nombreOrg = findViewById(R.id.txtRegNombreOrg);
        cedulaOrg = findViewById(R.id.txtRegCedulaOrg);
        telefonoOrg = findViewById(R.id.txtRegTelefonoOrg);
        dirOrg = findViewById(R.id.txtRegDirOrg);
        correoOrg = findViewById(R.id.txtRegCorreoOrg);
        contraOrg = findViewById(R.id.txtRegContraOrg);
        contraOrg2 = findViewById(R.id.txtRegContra2Org);

        btnUser = findViewById(R.id.btnUser);
        btnUser.setOnClickListener(this);
        btnOrg = findViewById(R.id.btnOrg);
        btnOrg.setOnClickListener(this);
        btnListo = findViewById(R.id.btnListo);
        btnListo.setOnClickListener(this);
        btnListo2 = findViewById(R.id.btnListo2);
        btnListo2.setOnClickListener(this);

    }

    private void iniciarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnUser.getId()) {
            if (nombreUser.getVisibility() == View.GONE) {
                nombreUser.setVisibility(View.VISIBLE);
                apellidoUser.setVisibility(View.VISIBLE);
                telefonoUser.setVisibility(View.VISIBLE);
                correoUser.setVisibility(View.VISIBLE);
                contraUser.setVisibility(View.VISIBLE);
                contraUser2.setVisibility(View.VISIBLE);
                btnListo.setVisibility(View.VISIBLE);

                nombreOrg.setVisibility(View.GONE);
                cedulaOrg.setVisibility(View.GONE);
                telefonoOrg.setVisibility(View.GONE);
                dirOrg.setVisibility(View.GONE);
                correoOrg.setVisibility(View.GONE);
                contraOrg.setVisibility(View.GONE);
                contraOrg2.setVisibility(View.GONE);
                btnListo2.setVisibility(View.GONE);

            } else {
                nombreUser.setVisibility(View.GONE);
                apellidoUser.setVisibility(View.GONE);
                telefonoUser.setVisibility(View.GONE);
                correoUser.setVisibility(View.GONE);
                contraUser.setVisibility(View.GONE);
                contraUser2.setVisibility(View.GONE);
                btnListo.setVisibility(View.GONE);
            }
        } else if (view.getId() == btnOrg.getId()) {
            if (nombreOrg.getVisibility() == View.GONE) {
                nombreOrg.setVisibility(View.VISIBLE);
                cedulaOrg.setVisibility(View.VISIBLE);
                telefonoOrg.setVisibility(View.VISIBLE);
                dirOrg.setVisibility(View.VISIBLE);
                correoOrg.setVisibility(View.VISIBLE);
                contraOrg.setVisibility(View.VISIBLE);
                contraOrg2.setVisibility(View.VISIBLE);
                btnListo2.setVisibility(View.VISIBLE);

                nombreUser.setVisibility(View.GONE);
                apellidoUser.setVisibility(View.GONE);
                telefonoUser.setVisibility(View.GONE);
                correoUser.setVisibility(View.GONE);
                contraUser.setVisibility(View.GONE);
                contraUser2.setVisibility(View.GONE);
                btnListo.setVisibility(View.GONE);

            } else {
                nombreOrg.setVisibility(View.GONE);
                cedulaOrg.setVisibility(View.GONE);
                telefonoOrg.setVisibility(View.GONE);
                dirOrg.setVisibility(View.GONE);
                correoOrg.setVisibility(View.GONE);
                contraOrg.setVisibility(View.GONE);
                contraOrg2.setVisibility(View.GONE);
                btnListo2.setVisibility(View.GONE);

            }
        } else if (view.getId() == btnListo.getId()) {
            agregarCliente();

        } else if (view.getId() == btnListo2.getId()) {
            agregarOrg();

        }
    }

    public void agregarCliente(){
        final String id = UUID.randomUUID().toString();
        String nombre = nombreUser.getEditText().getText().toString();
        String apellido = apellidoUser.getEditText().getText().toString();
        String telefono = telefonoUser.getEditText().getText().toString();
        final String correo = correoUser.getEditText().getText().toString();
        String contrasena = contraUser.getEditText().getText().toString();
        String contrasena2 = contraUser2.getEditText().getText().toString();

        if (nombre.equals("")||apellido.equals("")||telefono.equals("")
                ||correo.equals("")||contrasena.equals("")||contrasena2.equals("")) {
            validarTxt(nombre,apellido,telefono,correo,contrasena,contrasena2);
        }
        else {
            collectionReference = firebaseFirestore.collection("Datos");
            userCliente = new HashMap<>();
            userCliente.put("idCliente", id);
            userCliente.put("tipoUser", 1);
            userCliente.put("nombre", nombre);
            userCliente.put("apellido", apellido);
            userCliente.put("telefono", telefono);
            userCliente.put("txtCorreo", correo);

            if (contrasena.equals(contrasena2)) {
                userCliente.put("txtContrasena", contrasena);

                mAuth.createUserWithEmailAndPassword(correo,contrasena)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    collectionReference.document(correo).set(userCliente);

                                    Toast.makeText(RegistroActivity.this, "Usuario registrado ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegistroActivity.this, InicioActivity.class);
                                    startActivity(intent);
                                    limpiarTxt();
                                } else {
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        Toast.makeText(RegistroActivity.this, "Usuario ya existe", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RegistroActivity.this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        });
            } else {
                Toast.makeText(this, "Contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void agregarOrg(){
        final String id = UUID.randomUUID().toString();
        String nombre = nombreOrg.getEditText().getText().toString();
        String cedula = cedulaOrg.getEditText().getText().toString();
        String telefono = telefonoOrg.getEditText().getText().toString();
        String direccion = dirOrg.getEditText().getText().toString();
        final String correo = correoOrg.getEditText().getText().toString();
        String contrasena = contraOrg.getEditText().getText().toString();
        String contrasena2 = contraOrg2.getEditText().getText().toString();

        if (nombre.equals("")||cedula.equals("")||telefono.equals("")
                ||direccion.equals("")||correo.equals("")
                ||contrasena.equals("")||contrasena2.equals("")) {
            validarTxtOrg(nombre,cedula,telefono,direccion,correo,contrasena,contrasena2);
        }
        else {
            collectionReference = firebaseFirestore.collection("Datos");
            userOrg = new HashMap<>();
            userOrg.put("idOrg", id);
            userOrg.put("tipoUser", 2);
            userOrg.put("nombre", nombre);
            userOrg.put("cedula", cedula);
            userOrg.put("telefono", telefono);
            userOrg.put("direccion", direccion);
            userOrg.put("txtCorreo", correo);

            if (contrasena.equals(contrasena2)) {
                userOrg.put("txtContrasena", contrasena);

                mAuth.createUserWithEmailAndPassword(correo,contrasena)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    collectionReference.document(correo).set(userOrg);

                                    Toast.makeText(RegistroActivity.this, "Usuario registrado ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegistroActivity.this, InicioActivity.class);
                                    startActivity(intent);
                                    limpiarTxt();
                                } else {
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        Toast.makeText(RegistroActivity.this, "Usuario ya existe", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RegistroActivity.this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            } else {
                Toast.makeText(this, "Contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void validarTxt(String nombre, String apellido, String telefono,
                            String correo, String contrasena, String contrasena2) {
        if (nombre.equals("")) {
            nombreUser.getEditText().setError("Espacio requerido");
        } else if (apellido.equals("")) {
            apellidoUser.getEditText().setError("Espacio requerido");
        } else if (telefono.equals("")) {
            telefonoUser.getEditText().setError("Espacio requerido");
        } else if (correo.equals("")) {
            correoUser.getEditText().setError("Espacio requerido");
        } else if (contrasena.equals("")) {
            contraUser.getEditText().setError("Espacio requerido");
        } else if (contrasena2.equals("")) {
            contraUser2.getEditText().setError("Espacio requerido");
        }
    }

    private void validarTxtOrg(String nombre, String cedula, String telefono,
                               String direccion, String correo,
                               String contrasena, String contrasena2){
        if (nombre.equals("")) {
            nombreUser.getEditText().setError("Espacio requerido");
        } else if (cedula.equals("")) {
            apellidoUser.getEditText().setError("Espacio requerido");
        } else if (telefono.equals("")) {
            telefonoUser.getEditText().setError("Espacio requerido");
        } else if (direccion.equals("")) {
            telefonoUser.getEditText().setError("Espacio requerido");
        } else if (correo.equals("")) {
            correoUser.getEditText().setError("Espacio requerido");
        } else if (contrasena.equals("")) {
            contraUser.getEditText().setError("Espacio requerido");
        } else if (contrasena2.equals("")) {
            contraUser2.getEditText().setError("Espacio requerido");
        }
    }

    private void limpiarTxt() {
        nombreUser.getEditText().setText("");
        apellidoUser.getEditText().setText("");
        telefonoUser.getEditText().setText("");
        correoUser.getEditText().setText("");
        contraUser.getEditText().setText("");
        contraUser2.getEditText().setText("");

        nombreOrg.getEditText().setText("");
        cedulaOrg.getEditText().setText("");
        telefonoOrg.getEditText().setText("");
        dirOrg.getEditText().setText("");
        correoOrg.getEditText().setText("");
        contraOrg.getEditText().setText("");
        contraOrg2.getEditText().setText("");
    }
}
