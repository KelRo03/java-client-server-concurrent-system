/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectoc.s.Servidor;

import com.mycompany.proyectoc.s.Modelo.Ticket;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;


/**
 * Esta clase representa el servidor del chat y se encarga de manejar la comunicación
 * entre empleados y técnicos en tiempo real. Mantengo una cola de tickets priorizada
 * según su urgencia, y dos mapas concurrentes para saber qué empleados y técnicos
 * están conectados y poder enviarles mensajes directamente. 
 * 
 * En el método main() creo un ServerSocket que escucha en el puerto 5000 y, cada vez
 * que un cliente se conecta, se crea un hilo HiloCliente para manejarlo sin bloquear
 * el servidor. Esto permite que varios usuarios puedan conectarse y comunicarse al mismo
 * tiempo, y que los tickets se procesen según la prioridad de urgencia.
 */

public class ServidorChat {

    // Cola priorizada de tickets
    public static PriorityBlockingQueue<Ticket> colaTickets =
            new PriorityBlockingQueue<>(10, (t1, t2) -> Integer.compare(t2.getUrgencia(), t1.getUrgencia()));

    // Mapas para almacenar sockets conectados
    public static ConcurrentHashMap<String, Socket> empleadosConectados = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Socket> tecnicosConectados = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, String> empToTechName = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, String> techToEmpName = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(5000)) {
            System.out.println("Servidor activo en puerto 5000...");

            while (true) {
                Socket socket = server.accept();
                System.out.println("Cliente conectado: " + socket.getInetAddress());

                // Crear hilo para manejar este cliente
                HiloCliente hilo = new HiloCliente(socket);
                new Thread(hilo).start();
            }

        } catch (Exception e) {
            System.out.println("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}