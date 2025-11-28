/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectoc.s.Modelo;

import java.io.Serializable;

/**
 * Esta clase representa un ticket dentro del sistema de soporte. Cada ticket tiene
 * un ID único, una descripción, un nivel de urgencia, el departamento al que pertenece
 * y el nombre del empleado que lo creó. Implementa Serializable para poder guardarlo
 * en archivos si es necesario, y Comparable para poder ordenar los tickets según su urgencia.
 * Así, puedo crear tickets, consultarlos, ordenarlos y mostrarlos de forma clara en la aplicación.
 */

public class Ticket implements Serializable, Comparable<Ticket> {

    private static int contador = 1;
    private int id;
    private String descripcion;
    private int urgencia;
    private String departamento;
    private String nombreEmpleado;
    private String tecnicoAsignado;
    private String estado; // PENDIENTE, EN_PROCESO, COMPLETADO
    private String fechaCreacion;
    private String fechaCompletado;

    // Constructor completo
    public Ticket(String descripcion, int urgencia, String departamento, String empleado) {
        this.id = contador++;
        this.descripcion = descripcion;
        this.urgencia = urgencia;
        this.departamento = departamento;
        this.nombreEmpleado = empleado;
        this.estado = "PENDIENTE";
        this.tecnicoAsignado = null;
    }

    // Constructor simplificado (para uso desde el chat)
    public Ticket(String nombreEmpleado, String descripcion, int urgencia) {
        this.id = contador++;
        this.descripcion = descripcion;
        this.urgencia = urgencia;
        this.departamento = "General";
        this.nombreEmpleado = nombreEmpleado;
        this.estado = "PENDIENTE";
        this.tecnicoAsignado = null;
    }

    // Constructor para cargar desde BD
    public Ticket(int id, String descripcion, int urgencia, String departamento, 
                  String empleado, String tecnicoAsignado, String estado) {
        this.id = id;
        this.descripcion = descripcion;
        this.urgencia = urgencia;
        this.departamento = departamento;
        this.nombreEmpleado = empleado;
        this.tecnicoAsignado = tecnicoAsignado;
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public int getUrgencia() {
        return urgencia;
    }

    public String getDepartamento() {
        return departamento;
    }

    public int getId() {
        return id;
    }

    public String getTecnicoAsignado() {
        return tecnicoAsignado;
    }

    public void setTecnicoAsignado(String tecnicoAsignado) {
        this.tecnicoAsignado = tecnicoAsignado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaCompletado() {
        return fechaCompletado;
    }

    public void setFechaCompletado(String fechaCompletado) {
        this.fechaCompletado = fechaCompletado;
    }

    @Override
    public int compareTo(Ticket otro) {
        // Ordenar por urgencia (mayor primero)
        return Integer.compare(otro.urgencia, this.urgencia);
    }

    @Override
    public String toString() {
        return "Ticket #" + id + " - " + descripcion +
               " (Urgencia: " + urgencia + ", Estado: " + estado + 
               ", Empleado: " + nombreEmpleado + 
               (tecnicoAsignado != null ? ", Técnico: " + tecnicoAsignado : "") + ")";
    }
}