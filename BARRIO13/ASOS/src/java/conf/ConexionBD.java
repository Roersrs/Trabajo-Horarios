package conf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/asos?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Bogota";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "root"; // Cambia esto si tu MySQL usa otra clave

    private ConexionBD() {}

    public static Connection obtenerConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver MySQL en WEB-INF/lib", e);
        }
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}
