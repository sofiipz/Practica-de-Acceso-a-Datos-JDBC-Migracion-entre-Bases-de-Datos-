package dao;

import logger.LoggerPropio;
import util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Esta clase se encarga de insertar datos masivos a cada tabla de la base de datos
 * @author Sofía Petrova
 */


public class DMLManager {
    private LoggerPropio logger;

    public DMLManager(LoggerPropio logger) {
        this.logger = logger;
    }

    public void insertarClientes() {

        String sql = "INSERT INTO clientes (nombre, dni, email, credito, activo) VALUES (?, ?, ?, ? ,?)";

        try(Connection conexion = ConnectionFactory.getConnectionPrac2();
            PreparedStatement ps = conexion.prepareStatement(sql)) {

            logger.log("INFO", "DMLManager", "Insertando clientes...");

            for (int i = 1; i <= 50; i++) {
                ps.setString(1, "Cliente " + i);
                ps.setString(2, "DNI" + i);
                ps.setString(3, "cliente" + i + "@correo.com");
                ps.setDouble(4, 100 + i * 10);
                ps.setBoolean(5, true);

                ps.addBatch();
            }

            ps.executeBatch();

            logger.log("INFO", "DMLManager", "Clientes insertados correctamente");

        } catch (SQLException e) {
            logger.log("ERROR", "DMLManager", "Error al insertar clientes: " + e.getMessage());
        }
    }

    public void insertarMuebles() {
        String sql = "INSERT INTO muebles (nombre, descripcion, precio, stock, categoria, activo) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConnectionFactory.getConnectionPrac2();
           PreparedStatement ps = conexion.prepareStatement(sql)){

            logger.log("INFO", "DMLManager", "Insertando muebles...");

            for (int i = 1; i <= 30; i++) {
                ps.setString(1, "Mueble " + i);
                ps.setString(2, "Descripción del mueble " + i);
                ps.setDouble(3, 50 + i * 5);
                ps.setInt(4, 10 + i);

                String[] categorias = {"Sillas", "Mesas", "Sofás", "Camas", "Estanterías"};
                ps.setString(5, categorias[i % categorias.length]);

                ps.setBoolean(6, true);
                ps.addBatch();
            }
            ps.executeBatch();

            logger.log("INFO", "DMLManager", "Muebles insertados correctamente");

        } catch (SQLException e) {
            logger.log("ERROR", "DMLManager", "Error al insertar muebles: " + e.getMessage());
        }
    }

    public void insertarPedidos() {
        String sql = "INSERT INTO pedidos (cliente_id, fecha, total, estado, direccion, comentarios) VALUES (?, NOW(), ?, ?, ?, ?)";

        try (Connection conexion = ConnectionFactory.getConnectionPrac2();
           PreparedStatement ps = conexion.prepareStatement(sql)){

            logger.log("INFO", "DMLManager", "Insertando pedidos...");

            for (int i = 1; i <= 50; i++) {
                ps.setInt(1, (i % 10) + 1);
                ps.setDouble(2, 100 + i * 20);

                String[] estados = {"Pendiente", "Enviado", "Entregado"};
                ps.setString(3, estados[i % estados.length]);

                ps.setString(4, "Dirección " + i);
                ps.setString(5, "Comentario " + i);
                ps.addBatch();
            }
            ps.executeBatch();

            logger.log("INFO", "DMLManager", "Pedidos insertados correctamente.");

        } catch (SQLException e) {
            logger.log("ERROR", "DMLManager", "Error al insertar pedidos: " + e.getMessage());
        }
    }


}
