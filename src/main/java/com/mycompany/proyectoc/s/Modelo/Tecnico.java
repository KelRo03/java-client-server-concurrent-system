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
 * Esta clase representa a un técnico dentro del sistema y hereda de la clase Usuario.
 * Le agregué funcionalidad para conectarse a un socket y poder enviar y recibir mensajes.
 * Uso BufferedReader para leer los mensajes que llegan por el socket y PrintWriter para enviar mensajes.
 * Los métodos conectar(), enviarMensaje() y recibirMensaje() me permiten que el técnico se comunique
 * con el servidor de manera ordenada y segura, y también manejo errores si intenta enviar o recibir
 * mensajes sin estar conectado. Así, cada técnico puede interactuar con el sistema fácilmente.
 */

public class Tecnico extends Usuario {

    private transient BufferedReader entrada;
    private transient PrintWriter salida;

    public Tecnico(String nombre) {
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
