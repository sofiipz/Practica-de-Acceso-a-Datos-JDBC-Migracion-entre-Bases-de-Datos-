package dao;

import logger.LoggerPropio;

import javax.swing.plaf.nimbus.State;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FicheroMigracion {
    private LoggerPropio logger;

    public FicheroMigracion(LoggerPropio logger) {
        this.logger = logger;
    }

    public void generarInformeMigracion(Connection conexion1, Connection conexion2, String nombreFichero) {
        String[] tablas = {"clientes", "muebles", "pedidos"};
        int totalDestino = 0;

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(nombreFichero))) {

            for (String tabla : tablas) {

                int original = contarRegistros(conexion1, tabla);
                int destino = contarRegistros(conexion2, tabla + "_migra");
                int migrado = original;
                totalDestino +=destino;

                String linea = String.format(
                        "Esquema original 'prac2' - Tabla '%s': %d%n" +
                        "Esquema de destino 'prac2migra' - Tabla '%s': %d%n" +
                        "Datos migrados 'prac2' -> 'prac2migra': %d%n",
                        tabla, original, tabla, destino, migrado
                );
                bw.write(linea);
                logger.log("INFO", "FicheroMigracion", linea);
            }

            String totalLinea = "Total registros en prac2migra: " + totalDestino;
            bw.write(totalLinea);
            logger.log("INFO", "FicheroMigracion", totalLinea);

        } catch (IOException e) {
            logger.log("ERROR", "FicheroMigracion", "Error al generar fichero de migración: " + e.getMessage());

        }
    }

    private int contarRegistros(Connection conexion, String tabla) {
        String sql = "SELECT COUNT(*) FROM " + tabla;
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)){

            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            logger.log("ERROR", "FicheroMigracion", "Error al contar el registro de la tabla" + tabla + ": " + e.getMessage());
            return 0;
        }
    }
}
