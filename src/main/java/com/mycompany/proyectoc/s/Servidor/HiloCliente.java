/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectoc.s.Servidor;

import com.mycompany.proyectoc.s.Modelo.Empleado;
import com.mycompany.proyectoc.s.Modelo.Tecnico;
import com.mycompany.proyectoc.s.Modelo.Ticket;

import com.mycompany.proyectoc.s.Modelo.Ticket;
import java.io.*;
import java.net.Socket;

/**
 * Esta clase representa el hilo que maneja a cada cliente que se conecta al servidor.
 * Puede ser un empleado o un técnico, y se encarga de recibir objetos por el socket
 * y de iniciar la comunicación entre ellos. 
 * 
 * Cuando se conecta un empleado, guarda su socket, lo conecta y lo agrega a la lista
 * de empleados conectados. Si envía un ticket, lo añade a la cola de tickets y luego
 * se crea un hilo que escucha sus mensajes para enviarlos al técnico correspondiente.
 * 
 * Si se conecta un técnico, hace algo parecido: guarda su socket, lo conecta y lo agrega
 * a la lista de técnicos conectados, y luego se crea un hilo para escuchar sus mensajes
 * y reenviarlos al empleado correspondiente. 
 * 
 * Todo esto permite que el chat funcione en tiempo real entre empleados y técnicos, 
 * manejando la comunicación de forma ordenada y evitando que se bloquee el servidor.
 */

public class HiloCliente implements Runnable {

    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private String nombreUsuario;
    private String rol;

    public HiloCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);

            // Identificación inicial: EMPLEADO:Juan o TECNICO:Carlos
            String identificacion = entrada.readLine();
            if (identificacion.startsWith("EMPLEADO:")) {
                rol = "EMPLEADO";
                nombreUsuario = identificacion.substring(9);
                ServidorChat.empleadosConectados.put(nombreUsuario, socket);
                salida.println("Bienvenido EMPLEADO " + nombreUsuario);

            } else if (identificacion.startsWith("TECNICO:")) {
                rol = "TECNICO";
                nombreUsuario = identificacion.substring(8);
                ServidorChat.tecnicosConectados.put(nombreUsuario, socket);
                salida.println("Bienvenido TECNICO " + nombreUsuario);

                // 🔑 Enviar lista de tickets existentes al técnico al conectarse
                for (Ticket t : ServidorChat.colaTickets) {
                    salida.println("TICKET:" + t.getId() + " - " + t.getDescripcion());
                }
            }

            // Escuchar mensajes
            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                System.out.println("[" + nombreUsuario + "] " + mensaje);

                if (rol.equals("EMPLEADO") && mensaje.startsWith("NUEVO_TICKET:")) {
                    // Cuando un empleado crea un ticket
                    String desc = mensaje.substring(13);
                    Ticket nuevo = new Ticket(nombreUsuario, desc, 1); // urgencia = 1 por defecto
                    ServidorChat.colaTickets.add(nuevo);

                    // Avisar a todos los técnicos conectados
                    for (Socket s : ServidorChat.tecnicosConectados.values()) {
                        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                        out.println("TICKET:" + nuevo.getId() + " - " + nuevo.getDescripcion());
                    }
                } 
                else if (rol.equals("TECNICO") && mensaje.startsWith("SOLICITAR_DETALLE:")) {
                    String idTicket = mensaje.substring(18);
                    for (Ticket t : ServidorChat.colaTickets) {
                        if (String.valueOf(t.getId()).equals(idTicket)) {
                            salida.println("DETALLE:" + t.getDescripcion() + 
                                           " (Urgencia: " + t.getUrgencia() + ")");
                            break;
                        }
                    }
                } 
                else {
                    // Mensaje de chat general (empleado ↔ técnico)
                    // Aquí puedes meter la lógica de reenvío según empToTechName y techToEmpName
                    for (Socket s : ServidorChat.tecnicosConectados.values()) {
                        if (s != socket) {
                            new PrintWriter(s.getOutputStream(), true).println(nombreUsuario + ": " + mensaje);
                        }
                    }
                    for (Socket s : ServidorChat.empleadosConectados.values()) {
                        if (s != socket) {
                            new PrintWriter(s.getOutputStream(), true).println(nombreUsuario + ": " + mensaje);
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error en HiloCliente: " + e.getMessage());
        } finally {
            try {
                if (rol != null && nombreUsuario != null) {
                    if (rol.equals("EMPLEADO")) ServidorChat.empleadosConectados.remove(nombreUsuario);
                    else if (rol.equals("TECNICO")) ServidorChat.tecnicosConectados.remove(nombreUsuario);
                }
                socket.close();
            } catch (IOException e) { e.printStackTrace(); }
        }
    }
}