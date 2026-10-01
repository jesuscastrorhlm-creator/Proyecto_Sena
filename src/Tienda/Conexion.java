package Tienda;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {

    public static Connection conectar() {

        Connection conexion = null;

        try {

            conexion = DriverManager.getConnection("jdbc:sqlite:tienda.db");

            System.out.println("Base de datos conectada");

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());

        }

        return conexion;
    }
}