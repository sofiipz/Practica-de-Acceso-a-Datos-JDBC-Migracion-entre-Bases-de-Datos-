package dao;

import logger.LoggerPropio;
import util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Esta clase se encarga de migrar los datos desde prac2 hacia prac2migra.
 * @author Sofia
 */

public class MigracionManager {
    private LoggerPropio logger;

    public MigracionManager(LoggerPropio logger) {
        this.logger = logger;
    }

    public void migrarDatos() {
        try (Connection conexionPrac2 = ConnectionFactory.getConnectionPrac2();
        Connection conexionMig = ConnectionFactory.getConnectionPrac2Migra()){

            conexionMig.setAutoCommit(false);

            logger.log("INFO", "MigracionManager", "Iniciando migración de datos...");

            migrarClientes(conexionPrac2, conexionMig);
            migrarMuebles(conexionPrac2, conexionMig);
            migrarPedidos(conexionPrac2, conexionMig);

            conexionMig.commit();
            logger.log("INFO", "MigracionManager", "Migración completada con éxito.");


        } catch (SQLException e) {
            logger.log("ERROR", "MigracionManager", "Error en la migración: " + e.getMessage());
        }
    }

    public void migrarClientes(Connection conexionPrac2, Connection conexionMig) {
        logger.log("INFO", "MigracionManager", "Migrando tabla clientes...");

        String selectSQL = "SELECT * FROM clientes";
        String insertSQL = "INSERT INTO clientes_migra (id_migra, nombre_migra, dni_migra, email_migra, fecha_registro_migra, credito_migra, activo_migra, migrado_migra) VALUES (?, ?, ?, ?, ?, ?, ?, 1)";

        try (PreparedStatement psSelect = conexionPrac2.prepareStatement(selectSQL);
             ResultSet rs = psSelect.executeQuery();
             PreparedStatement psInsertar = conexionMig.prepareStatement(insertSQL)){

            while (rs.next()) {
                psInsertar.setInt(1, rs.getInt("id"));
                psInsertar.setString(2, rs.getString("nombre"));
                psInsertar.setString(3, rs.getString("dni"));
                psInsertar.setString(4, rs.getString("email"));
                psInsertar.setTimestamp(5, rs.getTimestamp("fecha_registro"));
                psInsertar.setBigDecimal(6, rs.getBigDecimal("credito"));
                psInsertar.setInt(7, rs.getInt("activo"));
                psInsertar.addBatch();
            }

            psInsertar.executeBatch();
            logger.log("INFO", "MigracionManager", "Clientes migrados correctamente.");

        } catch (SQLException e) {
            logger.log("ERROR", "MigracionManager", "Error en la migración de la tabla clientes: " + e.getMessage());
        }

    }

    public void migrarMuebles (Connection conexionPrac2, Connection conexionMig) {
        logger.log("INFO", "MigracionManager", "Migrando tabla MUEBLES...");

        String selectSQL = "SELECT * FROM muebles";
        String insertSQL = "INSERT INTO muebles_migra (id_migra, nombre_migra, precio_migra, categoria_migra, activo_migra, migrado_migra) VALUES (?, ?, ?, ?, ?, 1)";

        try (PreparedStatement psSelect = conexionPrac2.prepareStatement(selectSQL);
         ResultSet rs = psSelect.executeQuery();
         PreparedStatement psInsertar = conexionMig.prepareStatement(insertSQL)){

            while (rs.next()) {
                psInsertar.setInt(1, rs.getInt("id"));
                psInsertar.setString(2, rs.getString("nombre"));
                psInsertar.setBigDecimal(3, rs.getBigDecimal("precio"));
                psInsertar.setString(4, rs.getString("categoria"));
                psInsertar.setInt(5, rs.getInt("activo"));
                psInsertar.addBatch();
            }

            psInsertar.executeBatch();
            logger.log("INFO", "MigracionManager", "Muebles migrados correctamente.");


        } catch (SQLException e) {
            logger.log("ERROR", "MigracionManager", "Error en la migración de la tabla muebles: " + e.getMessage());
        }

    }


    public void migrarPedidos(Connection conexionPrac2, Connection conexionMig) {
        logger.log("INFO", "MigracionManager", "Migrando tabla PEDIDOS...");

        String selectSQL = "SELECT * FROM pedidos";
        String insertSQL = "INSERT INTO pedidos_migra (id_migra, cliente_id_migra, fecha_migra, total_migra, estado_migra, direccion_migra, migrado_migra) VALUES (?, ?, ?, ?, ?, ?, 1)";

        try(PreparedStatement psSelect = conexionPrac2.prepareStatement(selectSQL);
          ResultSet rs = psSelect.executeQuery();
          PreparedStatement psInsertar = conexionMig.prepareStatement(insertSQL)) {

            while (rs.next()) {
                psInsertar.setInt(1, rs.getInt("id"));
                psInsertar.setInt(2, rs.getInt("cliente_id"));
                psInsertar.setTimestamp(3, rs.getTimestamp("fecha"));
                psInsertar.setBigDecimal(4, rs.getBigDecimal("total"));
                psInsertar.setString(5, rs.getString("estado"));
                psInsertar.setString(6, rs.getString("direccion"));
                psInsertar.addBatch();
            }

            psInsertar.executeBatch();
            logger.log("INFO", "MigracionManager", "Pedidos migrados correctamente.");

        } catch (SQLException e) {
            logger.log("ERROR", "MigracionManager", "Error en la migración de la tabla pedidos: " + e.getMessage());
        }

    }

}
