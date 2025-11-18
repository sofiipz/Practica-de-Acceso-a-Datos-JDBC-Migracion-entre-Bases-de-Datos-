package dao;

import logger.LoggerPropio;
import util.ConnectionPoolFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Esta clase se encarga de insertar datos masivos a cada tabla de la base de datos (V2)
 * @author Sofia
 */

public class DMLManager {

    private LoggerPropio logger;

    public DMLManager(LoggerPropio logger) {
        this.logger = logger;
    }

    public void insertarDatos() {
        try (Connection conexion = ConnectionPoolFactory.getDataSourcePrac2().getConnection()) {
            insertarClientes(conexion);
            insertarMuebles(conexion);
            insertarPedidos(conexion);
        } catch (SQLException e) {
            logger.log("ERROR", "DMLManager", "Error al obtener conexión: " + e.getMessage());
        }
    }

    public void insertarClientes(Connection conexion) {
        String sql = "INSERT INTO clientes (nombre, dni, email, credito, activo) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            logger.log("INFO", "DMLManager", "Insertando clientes en MySQL...");

            for (int i = 1; i <= 50; i++) {
                ps.setString(1, "Cliente " + i);
                ps.setString(2, "DNI" + i);
                ps.setString(3, "cliente" + i + "@correo.com");
                ps.setDouble(4, 100 + i * 10);
                ps.setBoolean(5, true);
                ps.addBatch();
            }

            ps.executeBatch();
            logger.log("INFO", "DMLManager", "Clientes insertados correctamente en MySQL.");
        } catch (SQLException e) {
            logger.log("ERROR", "DMLManager", "Error al insertar clientes: " + e.getMessage());
        }

    }

    public void insertarMuebles(Connection conexion) {
        String sql = "INSERT INTO muebles (nombre, descripcion, precio, stock, categoria, activo) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            logger.log("INFO", "DMLManager", "Insertando muebles en MySQL...");

            String[] categorias = {"Sillas", "Mesas", "Sofás", "Camas", "Estanterías"};

            for (int i = 1; i <= 30; i++) {
                ps.setString(1, "Mueble " + i);
                ps.setString(2, "Descripción del mueble " + i);
                ps.setDouble(3, 50 + i * 5);
                ps.setInt(4, 10 + i);
                ps.setString(5, categorias[i % categorias.length]);
                ps.setBoolean(6, true);
                ps.addBatch();
            }

            ps.executeBatch();
            logger.log("INFO", "DMLManager", "Muebles insertados correctamente en MySQL.");
        } catch (SQLException e) {
            logger.log("ERROR", "DMLManager", "Error al insertar muebles: " + e.getMessage());
        }
    }

    public void insertarPedidos(Connection conexion) {
        String sql = "INSERT INTO pedidos (cliente_id, fecha, total, estado, direccion, comentarios) VALUES (?, NOW(), ?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            logger.log("INFO", "DMLManager", "Insertando pedidos en MySQL...");

            String[] estados = {"Pendiente", "Enviado", "Entregado"};

            for (int i = 1; i <= 50; i++) {
                ps.setInt(1, (i % 10) + 1);
                ps.setDouble(2, 100 + i * 20);
                ps.setString(3, estados[i % estados.length]);
                ps.setString(4, "Dirección " + i);
                ps.setString(5, "Comentario " + i);
                ps.addBatch();
            }

            ps.executeBatch();
            logger.log("INFO", "DMLManager", "Pedidos insertados correctamente en MySQL.");
        } catch (SQLException e) {
            logger.log("ERROR", "DMLManager", "Error al insertar pedidos: " + e.getMessage());
        }


    }

}
