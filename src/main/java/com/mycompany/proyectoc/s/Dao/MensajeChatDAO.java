package com.mycompany.proyectoc.s.Dao;

import com.mycompany.proyectoc.s.Modelo.MensajeChat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para manejar los mensajes del chat por ticket.
 */
public class MensajeChatDAO {

    /**
     * Inserta un nuevo mensaje en el chat de un ticket.
     */
    public void insertarMensaje(MensajeChat mensaje) {
        String sql = "INSERT INTO mensajes_chat (ticket_id, remitente, tipo_remitente, mensaje) VALUES (?,?,?,?)";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, mensaje.getTicketId());
            ps.setString(2, mensaje.getRemitente());
            ps.setString(3, mensaje.getTipoRemitente());
            ps.setString(4, mensaje.getMensaje());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene todos los mensajes de un ticket específico.
     */
    public List<MensajeChat> obtenerMensajesPorTicket(int ticketId) {
        List<MensajeChat> lista = new ArrayList<>();
        String sql = "SELECT * FROM mensajes_chat WHERE ticket_id = ? ORDER BY fecha_envio ASC";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ticketId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                MensajeChat mensaje = new MensajeChat(
                        rs.getInt("id"),
                        rs.getInt("ticket_id"),
                        rs.getString("remitente"),
                        rs.getString("tipo_remitente"),
                        rs.getString("mensaje"),
                        rs.getString("fecha_envio")
                );
                lista.add(mensaje);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Elimina todos los mensajes de un ticket (usado cuando se elimina un ticket).
     */
    public void eliminarMensajesPorTicket(int ticketId) {
        String sql = "DELETE FROM mensajes_chat WHERE ticket_id = ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ticketId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}