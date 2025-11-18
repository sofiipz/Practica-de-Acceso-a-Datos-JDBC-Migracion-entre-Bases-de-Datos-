package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Esta clase se encarga de hacer la conexión con las bases de datos
 * @author Sofía Petrova
 */

public class ConnectionFactory {
    private static final String URL_PRAC2 = "jdbc:mysql://localhost:3306/prac2";
    private static final String URL_MIGRA = "jdbc:mysql://localhost:3306/prac2migra";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static Connection getConnectionPrac2() throws SQLException {
        return DriverManager.getConnection(URL_PRAC2, USER, PASS);
    }

    public static Connection getConnectionPrac2Migra() throws SQLException {
        return DriverManager.getConnection(URL_MIGRA, USER, PASS);
    }


}
