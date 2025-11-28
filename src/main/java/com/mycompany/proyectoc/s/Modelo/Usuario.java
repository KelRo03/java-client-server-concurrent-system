/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectoc.s.Modelo;
import java.io.Serializable;
import java.net.Socket;

/**
 * Esta clase es la base para todos los tipos de usuarios en el sistema, como
 * empleados o técnicos. Tiene un nombre y un socket para la comunicación con
 * el servidor, pero como es abstracta, no se puede crear directamente. 
 * Uso Serializable para poder guardar objetos si es necesario, y el socket
 * es transient porque no se debe guardar al serializar. Con esta clase puedo
 * heredar estas propiedades a las demás clases de usuarios y mantener el código ordenado.
 */

public abstract class Usuario implements Serializable {

    protected String nombre;
    protected transient Socket socket;

    public Usuario(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }
}
