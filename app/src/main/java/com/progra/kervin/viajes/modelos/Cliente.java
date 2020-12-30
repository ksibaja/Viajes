package com.progra.kervin.viajes.modelos;

public class Cliente {
    private String idCliente;
    private String nombre;
    private String correo;
    private String contrasena;
    private int tipoUsuario;

    public Cliente () {
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setTipoUsuario(int tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    @Override
    public String toString() {
        return ""+tipoUsuario;
    }
}
