package com.progra.kervin.viajes;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputLayout  nombreU, apellidoU, telefonoU, correoU, contraU, contraU2;
    TextInputLayout nombreO, telefonoO;
    Button btnUser, btnOrg, btnListo, btnListo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nombreU = findViewById(R.id.txtRegNombre);
        apellidoU = findViewById(R.id.txtRegApellido);
        telefonoU = findViewById(R.id.txtRegTelefono);
        correoU = findViewById(R.id.txtRegCorreo);
        contraU = findViewById(R.id.txtRegContra);
        contraU2 = findViewById(R.id.txtRegContra2);
        btnListo = findViewById(R.id.btnListo);
        btnListo2 = findViewById(R.id.btnListo2);

        nombreO = findViewById(R.id.txtRegNombre2);
        telefonoO = findViewById(R.id.txtRegTelefono2);

        btnUser = findViewById(R.id.btnUser);
        btnUser.setOnClickListener(this);
        btnOrg = findViewById(R.id.btnOrg);
        btnOrg.setOnClickListener(this);
        btnListo = findViewById(R.id.btnListo);
        btnListo.setOnClickListener(this);
        btnListo2 = findViewById(R.id.btnListo2);
        btnListo2.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnUser.getId()) {
            if (nombreU.getVisibility() == View.GONE) {
                nombreU.setVisibility(View.VISIBLE);
                apellidoU.setVisibility(View.VISIBLE);
                telefonoU.setVisibility(View.VISIBLE);
                correoU.setVisibility(View.VISIBLE);
                contraU.setVisibility(View.VISIBLE);
                contraU2.setVisibility(View.VISIBLE);
                btnListo.setVisibility(View.VISIBLE);

                nombreO.setVisibility(View.GONE);
                telefonoO.setVisibility(View.GONE);
                btnListo2.setVisibility(View.GONE);

            } else {
                nombreU.setVisibility(View.GONE);
                apellidoU.setVisibility(View.GONE);
                telefonoU.setVisibility(View.GONE);
                correoU.setVisibility(View.GONE);
                contraU.setVisibility(View.GONE);
                contraU2.setVisibility(View.GONE);
                btnListo.setVisibility(View.GONE);
            }
        } else if (view.getId() == btnOrg.getId()) {
            if (nombreO.getVisibility() == View.GONE) {
                nombreO.setVisibility(View.VISIBLE);
                telefonoO.setVisibility(View.VISIBLE);
                btnListo2.setVisibility(View.VISIBLE);

                nombreU.setVisibility(View.GONE);
                apellidoU.setVisibility(View.GONE);
                telefonoU.setVisibility(View.GONE);
                correoU.setVisibility(View.GONE);
                contraU.setVisibility(View.GONE);
                contraU2.setVisibility(View.GONE);
                btnListo.setVisibility(View.GONE);

            } else {
                nombreO.setVisibility(View.GONE);
                telefonoO.setVisibility(View.GONE);
                btnListo2.setVisibility(View.GONE);

            }
        } else if (view.getId() == btnListo.getId()) {
            Toast.makeText(this, "Cliente registrado", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == btnListo2.getId()) {
            Toast.makeText(this, "Espere mientras verificamos su cuenta", Toast.LENGTH_SHORT).show();
        }
    }
}
