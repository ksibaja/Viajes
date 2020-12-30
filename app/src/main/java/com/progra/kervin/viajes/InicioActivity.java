package com.progra.kervin.viajes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class InicioActivity extends AppCompatActivity implements View.OnClickListener {

    Button login, invitado, registro;
    EditText txtCorreo, txtContrasena;

    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    DocumentReference documentReference;
    DocumentSnapshot document;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        FirebaseApp.initializeApp(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        login = findViewById(R.id.btnLogin);
        login.setOnClickListener(this);
        invitado = findViewById(R.id.btnInvitado);
        invitado.setOnClickListener(this);
        registro = findViewById(R.id.btnRegistro);
        registro.setOnClickListener(this);

        txtCorreo = findViewById(R.id.txtUser);
        txtContrasena = findViewById(R.id.txtPass);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void login(){
        final String correo = this.txtCorreo.getText().toString().trim();
        String contrasena = this.txtContrasena.getText().toString().trim();

        if (correo.equals("") || contrasena.equals("")){
            if (correo.equals("")) {
                this.txtCorreo.setError("Espacio requerido");
            } else {
                this.txtContrasena.setError("Espacio requerido");
            }
        } else {
            mAuth.signInWithEmailAndPassword(correo, contrasena)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(InicioActivity.this, "Bienvenido ", Toast.LENGTH_SHORT).show();

                                documentReference = firebaseFirestore.collection("Datos").document(correo);
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            document = task.getResult();
                                            if (document.getData().get("tipoUser").toString().equals("1")) {

                                                Intent intent = new Intent(InicioActivity.this, UsuarioActivity.class);
                                                startActivity(intent);
                                            } else if (document.getData().get("tipoUser").toString().equals("2")) {
                                                Intent intent = new Intent(InicioActivity.this, OrganizadorActivity.class);
                                                startActivity(intent);
                                            }
                                        } else {
                                            Toast.makeText(InicioActivity.this, "Fallido... ", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                txtCorreo.setText("");
                                txtContrasena.setText("");
                            } else {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(InicioActivity.this, "Usuario ya existe", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(InicioActivity.this, "Usuario no existe", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == login.getId()) {
            login();
        } else if (view.getId() == invitado.getId()) {
            Intent invitado = new Intent(this, InvitadoActivity.class);
            startActivity(invitado);
        } else if (view.getId() == registro.getId()) {
            Intent registro = new Intent(this,RegistroActivity.class);
            startActivity(registro);
        }
    }
}
