/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectoc.s.Modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Esta clase representa a un empleado dentro del sistema y hereda de la clase Usuario.
 * Le agregué funcionalidad para conectarse a un socket y poder enviar y recibir mensajes.
 * Uso BufferedReader para leer lo que llega por el socket y PrintWriter para enviar mensajes.
 * Los métodos conectar(), enviarMensaje() y recibirMensaje() me ayudan a manejar la comunicación
 * de manera sencilla, y manejo errores si el empleado intenta enviar o recibir mensajes sin estar conectado.
 * Esto hace que cada empleado pueda comunicarse con el servidor de manera segura y organizada.
 */

public class Empleado extends Usuario {

    private transient BufferedReader entrada;
    private transient PrintWriter salida;

    public Empleado(String nombre) {
        super(nombre);
    }

    public void conectar() throws IOException {
        if (socket == null) throw new IOException("Socket no asignado");
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        salida = new PrintWriter(socket.getOutputStream(), true);
    }

    public void enviarMensaje(String mensaje) throws IOException {
        if (salida == null) throw new IOException("No conectado");
        salida.println(mensaje);
    }

    public String recibirMensaje() throws IOException {
        if (entrada == null) throw new IOException("No conectado");
        return entrada.readLine();
    }
}