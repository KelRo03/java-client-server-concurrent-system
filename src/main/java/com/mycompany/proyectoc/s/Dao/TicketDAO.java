/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectoc.s.Dao;

import com.mycompany.proyectoc.s.Modelo.Ticket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase la hice para manejar todo lo relacionado con los tickets en la base de datos.
 * Aquí tengo un método para insertar un nuevo ticket con su descripción, urgencia,
 * departamento y nombre del empleado, y otro método para obtener todos los tickets
 * que ya están guardados. Me conecto a la base de datos usando la clase ConexionBD
 * y utilizo PreparedStatement para evitar errores y hacer el código más seguro.
 * De esta forma, cada vez que necesite trabajar con tickets, solo llamo a estos
 * métodos y no tengo que escribir las consultas SQL en otras partes del programa.
 */

public class TicketDAO {

    /**
     * Inserta un nuevo ticket en la base de datos.
     */
    public void insertarTicket(Ticket ticket) {
        String sql = "INSERT INTO tickets (descripcion, urgencia, departamento, empleado_nombre, estado) VALUES (?,?,?,?,?)";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ticket.getDescripcion());
            ps.setInt(2, ticket.getUrgencia());
            ps.setString(3, ticket.getDepartamento());
            ps.setString(4, ticket.getNombreEmpleado());
            ps.setString(5, "PENDIENTE");
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene todos los tickets de la base de datos.
     */
    public List<Ticket> obtenerTickets() {
        List<Ticket> lista = new ArrayList<>();
        String sql = "SELECT * FROM tickets ORDER BY urgencia DESC, fecha_creacion ASC";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ticket t = new Ticket(
                        rs.getInt("id"),
                        rs.getString("descripcion"),
                        rs.getInt("urgencia"),
                        rs.getString("departamento"),
                        rs.getString("empleado_nombre"),
                        rs.getString("tecnico_asignado"),
                        rs.getString("estado")
                );
                lista.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Asigna un técnico a un ticket y cambia el estado a EN_PROCESO.
     */
    public void asignarTecnico(int ticketId, String nombreTecnico) {
        String sql = "UPDATE tickets SET tecnico_asignado = ?, estado = 'EN_PROCESO' WHERE id = ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombreTecnico);
            ps.setInt(2, ticketId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Transfiere un ticket de un técnico a otro.
     */
    public void transferirTicket(int ticketId, String nuevoTecnico) {
        String sql = "UPDATE tickets SET tecnico_asignado = ? WHERE id = ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoTecnico);
            ps.setInt(2, ticketId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Marca un ticket como completado.
     */
    public void completarTicket(int ticketId) {
        String sql = "UPDATE tickets SET estado = 'COMPLETADO', fecha_completado = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ticketId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene tickets asignados a un técnico específico.
     */
    public List<Ticket> obtenerTicketsPorTecnico(String nombreTecnico) {
        List<Ticket> lista = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE tecnico_asignado = ? AND estado != 'COMPLETADO' ORDER BY urgencia DESC";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombreTecnico);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Ticket t = new Ticket(
                        rs.getInt("id"),
                        rs.getString("descripcion"),
                        rs.getInt("urgencia"),
                        rs.getString("departamento"),
                        rs.getString("empleado_nombre"),
                        rs.getString("tecnico_asignado"),
                        rs.getString("estado")
                );
                lista.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Obtiene tickets pendientes (sin asignar) y en proceso disponibles para transferir.
     */
    public List<Ticket> obtenerTicketsDisponibles() {
        List<Ticket> lista = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE estado IN ('PENDIENTE', 'EN_PROCESO') ORDER BY urgencia DESC, fecha_creacion ASC";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ticket t = new Ticket(
                        rs.getInt("id"),
                        rs.getString("descripcion"),
                        rs.getInt("urgencia"),
                        rs.getString("departamento"),
                        rs.getString("empleado_nombre"),
                        rs.getString("tecnico_asignado"),
                        rs.getString("estado")
                );
                lista.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}