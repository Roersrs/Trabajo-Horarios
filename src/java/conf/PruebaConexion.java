package conf;

import java.sql.Connection;
import java.sql.SQLException;

public class PruebaConexion {

    public static void main(String[] args) {
        System.out.println("Intentando conectar a MySQL...");

        try (Connection conexion = ConexionBD.obtenerConexion()) {
            if (conexion != null && !conexion.isClosed()) {
                System.out.println("Conexion exitosa a la base de datos.");
                System.out.println("Base de datos: " + conexion.getCatalog());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar:");
            e.printStackTrace();
        }
    }
}