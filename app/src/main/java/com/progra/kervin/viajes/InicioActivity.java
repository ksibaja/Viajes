package com.progra.kervin.viajes;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class InicioActivity extends AppCompatActivity implements View.OnClickListener {

    Button login, invitado, registro;
    EditText user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        login = findViewById(R.id.btnLogin);
        login.setOnClickListener(this);
        invitado = findViewById(R.id.btnInvitado);
        invitado.setOnClickListener(this);
        registro = findViewById(R.id.btnRegistro);
        registro.setOnClickListener(this);

        user = findViewById(R.id.txtUser);
        pass = findViewById(R.id.txtPass);
    }

    public void login(){
        String usuario = user.getText().toString();
        String contrasena = pass.getText().toString();

        if (usuario.equals("") || contrasena.equals("")){
            if (usuario.equals("")) {
                user.setError("Espacio requerido");
            } else if (contrasena.equals("")) {
                pass.setError("Espacio requerido");
            }
        } else {
            if (usuario.equals("admi") && contrasena.equals("admi")) {
                Intent intent = new Intent(this, AdminActivity.class);
                startActivity(intent);
            }
            else if (usuario.equals("user")) {
                Intent intent = new Intent(this,  UsuarioActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Bienvenido Cliente", Toast.LENGTH_LONG).show();
            } else if (usuario.equals("org")) {
                Intent intent = new Intent(this,  OrganizadorActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Bienvenido Organizador", Toast.LENGTH_LONG).show();
            }
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
