/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectoc.s.Modelo;

/**
 *
 * @author sheyr
 */
public class MensajeChat {

    private int id;              // ID del mensaje en la base de datos
    private int ticketId;        // ID del ticket al que pertenece
    private String remitente;    // Nombre de quien envía el mensaje
    private String tipoRemitente;// "empleado" o "tecnico"
    private String mensaje;      // Contenido del mensaje
    private String fechaEnvio;   // Fecha y hora del mensaje (como String)

    // Constructor completo
    public MensajeChat(int id, int ticketId, String remitente, String tipoRemitente, String mensaje, String fechaEnvio) {
        this.id = id;
        this.ticketId = ticketId;
        this.remitente = remitente;
        this.tipoRemitente = tipoRemitente;
        this.mensaje = mensaje;
        this.fechaEnvio = fechaEnvio;
    }

    // Constructor sin ID y fecha (para insertar nuevos mensajes)
    public MensajeChat(int ticketId, String remitente, String tipoRemitente, String mensaje) {
        this.ticketId = ticketId;
        this.remitente = remitente;
        this.tipoRemitente = tipoRemitente;
        this.mensaje = mensaje;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getTipoRemitente() {
        return tipoRemitente;
    }

    public void setTipoRemitente(String tipoRemitente) {
        this.tipoRemitente = tipoRemitente;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(String fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    // Método para mostrar el mensaje de forma legible (opcional)
    @Override
    public String toString() {
        return "[" + fechaEnvio + "] " + remitente + " (" + tipoRemitente + "): " + mensaje;
    }
}

