package com.progra.kervin.viajes.modelos;

public class Metodos {
    Cliente cliente = new Cliente();

    public String agregarCliente(String idCliente, String nombre, String correo, String contrasena, int tipoUsuario){
        cliente.setIdCliente(idCliente);
        cliente.setNombre(nombre);
        cliente.setCorreo(correo);
        cliente.setContrasena(contrasena);
        cliente.setTipoUsuario(tipoUsuario);

        if (cliente.toString().equals("1")){
            return "Cliente";
        } else if(cliente.toString().equals("2")){
            return "Organizador";
        } else {
            return "";
        }
    }

}
