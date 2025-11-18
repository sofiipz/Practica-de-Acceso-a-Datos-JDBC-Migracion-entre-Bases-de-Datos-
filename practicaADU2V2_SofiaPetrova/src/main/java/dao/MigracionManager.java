package dao;

import logger.LoggerPropio;
import util.ConnectionPoolFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Esta clase se encarga de migrar los datos de MySQL a H2 (prac2 → prac2migra)
 * @author Sofia
 */

public class MigracionManager {

    private LoggerPropio logger;

    public MigracionManager(LoggerPropio logger) {
        this.logger = logger;
    }

    public void migrarDatos() {
        try (Connection conexionPrac2 = ConnectionPoolFactory.getDataSourcePrac2().getConnection();
          Connection conexionMigra = ConnectionPoolFactory.getDataSourcePrac2Migra().getConnection()){

            conexionMigra.setAutoCommit(false);
            migrarClientes(conexionPrac2, conexionMigra);
            migrarMuebles(conexionPrac2, conexionMigra);
            migrarPedidos(conexionPrac2, conexionMigra);

            conexionMigra.commit();
            logger.log("INFO", "MigracionManager", "Migración completa con éxito.");

        } catch (SQLException e) {
            logger.log("ERROR", "MigracionManager", "Error durante la migración: " + e.getMessage());
        }
    }

    private void migrarClientes(Connection conexion1, Connection conexion2) {
        String sqlSelect = "SELECT id, nombre, dni, email, fecha_registro, credito, activo FROM clientes";
        String sqlInsert = "INSERT INTO clientes_migra (id_migra, nombre_migra, dni_migra, email_migra, fecha_registro_migra, credito_migra, activo_migra, migrado_migra) VALUES (?, ?, ?, ?, ?, ?, ?, 1)";

        try (PreparedStatement psSelect = conexion1.prepareStatement(sqlSelect);
        PreparedStatement psInsert = conexion2.prepareStatement(sqlInsert)){

            ResultSet rs = psSelect.executeQuery();

            while (rs.next()) {
                psInsert.setInt(1, rs.getInt("id"));
                psInsert.setString(2, rs.getString("nombre"));
                psInsert.setString(3, rs.getString("dni"));
                psInsert.setString(4, rs.getString("email"));
                psInsert.setTimestamp(5, rs.getTimestamp("fecha_registro"));
                psInsert.setDouble(6, rs.getDouble("credito"));
                psInsert.setBoolean(7, rs.getBoolean("activo"));
                psInsert.addBatch();
            }
            psInsert.executeBatch();
            logger.log("INFO", "MigracionManager", "Clientes migrados correctamente.");

        } catch (SQLException e) {
            logger.log("ERROR", "MigracionManager", "Error durante la migración de la tabla clientes: " + e.getMessage());
        }
    }

    private void migrarMuebles(Connection conexion1, Connection conexion2) {
        String sqlSelect = "SELECT id, nombre, precio, categoria, activo FROM muebles";
        String sqlInsert = "INSERT INTO muebles_migra (id_migra, nombre_migra, precio_migra, categoria_migra, activo_migra, migrado_migra) VALUES (?, ?, ?, ?, ?, 1)";

        try (PreparedStatement psSelect = conexion1.prepareStatement(sqlSelect);
        PreparedStatement psInsert = conexion2.prepareStatement(sqlInsert)){

            ResultSet rs = psSelect.executeQuery();
            while (rs.next()) {
                psInsert.setInt(1, rs.getInt("id"));
                psInsert.setString(2, rs.getString("nombre"));
                psInsert.setDouble(3, rs.getDouble("precio"));
                psInsert.setString(4, rs.getString("categoria"));
                psInsert.setBoolean(5, rs.getBoolean("activo"));
                psInsert.addBatch();
            }

            psInsert.executeBatch();
            logger.log("INFO", "MigracionManager", "Muebles migrados correctamente.");

        }catch (SQLException e) {
            logger.log("ERROR", "MigracionManager", "Error durante la migración de la tabla muebles: " + e.getMessage());
        }
    }

    private void migrarPedidos (Connection conexion1, Connection conexion2) {

        String sqlSelect = "SELECT id, cliente_id, fecha, total, estado, direccion, comentarios FROM pedidos";
        String sqlInsert = "INSERT INTO pedidos_migra (id_migra, cliente_id_migra, fecha_migra, total_migra, estado_migra, direccion_migra, migrado_migra) VALUES (?, ?, ?, ?, ?, ?, 1)";

        try (PreparedStatement psSelect = conexion1.prepareStatement(sqlSelect);
        PreparedStatement psInsert = conexion2.prepareStatement(sqlInsert)) {

            ResultSet rs = psSelect.executeQuery();
            while (rs.next()) {
                psInsert.setInt(1, rs.getInt("id"));
                psInsert.setInt(2, rs.getInt("cliente_id"));
                psInsert.setTimestamp(3, rs.getTimestamp("fecha"));
                psInsert.setDouble(4, rs.getDouble("total"));
                psInsert.setString(5, rs.getString("estado"));
                psInsert.setString(6, rs.getString("direccion"));
                psInsert.addBatch();
            }
            psInsert.executeBatch();
            logger.log("INFO", "MigracionManager", "Pedidos migrados correctamente.");

        }catch (SQLException e) {
            logger.log("ERROR", "MigracionManager", "Error durante la migración de la tabla pedidos: " + e.getMessage());
        }
    }

}
