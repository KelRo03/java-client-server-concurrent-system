/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectoc.s.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Esta clase la hice para poder conectarme a mi base de datos MySQL sin tener que 
 * estar escribiendo el código de conexión en todas partes. Aquí guardé la URL 
 * de la base de datos, el usuario y la contraseña, y creé un método estático 
 * llamado getConnection() que me devuelve la conexión lista para usar. 
 * Así, cada vez que otra parte del programa necesite acceder a la base de datos, 
 * solo llama a este método y listo, no repito código y todo está más ordenado.
 */

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/ClienteServidor";
    private static final String USER = "root";
    private static final String PASS = "Kelvin0306";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
