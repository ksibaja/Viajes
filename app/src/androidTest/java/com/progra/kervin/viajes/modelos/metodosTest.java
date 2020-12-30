package com.progra.kervin.viajes.modelos;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class metodosTest {

    private Metodos met = new Metodos(); // se crea referencia a la clase metodos

    @Test
    public void agregarClienteTest() {
        String tipo = met.agregarCliente("es32tfs", "Luis", "luis@gmail.com", "luis12", 1);
        assertEquals("Cliente", tipo); // si el tipo usuario es 1, se espera que sea un cliente
    } // se realiza esta prueba

    @Test
    public void agregarClienteTest2() {
        String tipo = met.agregarCliente("asffds", "Juan", "ja@gmail.com", "contra", 332);
        assertEquals("", tipo); // si el tipo de usuario es != de 1 y 2, no es ningun tipo de usuario
    } // se realiza esta prueba

    @Test
    public void agregarClienteTest3() {
        String tipo = met.agregarCliente("2df4fds", "Repuestos ", "rep@gmail.com", "repuesto233", 2);
        assertEquals("Organizador", tipo);  // si el tipo de usuario es 2, se espera que sea un organizador
    } // se realiza esta prueba

}