/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectoc.s.Dao;

import java.sql.*;

/**
 * Esta clase la hice para manejar todo lo que tiene que ver con los usuarios en la base de datos.
 * Con el método validarLogin() verifico si el nombre y la contraseña que escribe el usuario
 * existen en la tabla y así puedo dejarlo entrar al sistema. Además, con el método obtenerTipo()
 * puedo saber qué tipo de usuario es (por ejemplo, administrador o empleado) según su nombre.
 * Para todo esto uso la clase ConexionBD y consultas con PreparedStatement para que el código
 * sea más seguro y fácil de mantener.
 */

public class UsuarioDAO {

    private Connection conectar() throws SQLException {

        String url = "jdbc:mysql://localhost:3306/ClienteServidor";
        String usuario = "root";
        String password = "Kelvin0306";
        return DriverManager.getConnection(url, usuario, password);
    }


    public boolean validarLogin(String nombre, String pass) {
        String sql = "SELECT * FROM usuarios WHERE nombre=? AND password=?";
        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene el tipo de usuario: "empleado" o "tecnico".
     */
    public String obtenerTipo(String nombre) {
        String sql = "SELECT tipo FROM usuarios WHERE nombre=?";
        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("tipo");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}