package dao;

import logger.LoggerPropio;
import util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Esta clase gestiona los clientes, con métodos CRUD y consultas con JDBC.
 * @author Sofia Petrova
 */


public class ClienteDAO {

    private LoggerPropio logger;

    public ClienteDAO(LoggerPropio logger) {
        this.logger = logger;
    }

    public void insert(String nombre, String dni, String email, double credito, boolean activo) {
        String sql = "INSERT INTO clientes (nombre, dni, email, credito, activo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = ConnectionFactory.getConnectionPrac2();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, dni);
            ps.setString(3, email);
            ps.setDouble(4, credito);
            ps.setBoolean(5, activo);
            ps.executeUpdate();

            logger.log("INFO", "ClienteDAO", "Cliente insertado correctamente: " + nombre);
        } catch (SQLException e) {
            logger.log("ERROR", "ClienteDAO", "Error al insertar cliente: " + e.getMessage());
        }
    }

    public List<String> findAll() {
        List<String> clientes = new ArrayList<>();

        String sql = "SELECT id, nombre, email FROM clientes";

        try (Connection conexion = ConnectionFactory.getConnectionPrac2();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {


            while (rs.next()) {
                clientes.add(rs.getInt("id") + " - " + rs.getString("nombre") + " (" + rs.getString("email") + ")");
            }

            logger.log("INFO", "ClienteDAO", "Se han encontrado " + clientes.size() + " clientes en la base de datos.");

        } catch (SQLException e) {
            logger.log("ERROR", "ClienteDAO", "Error al listar clientes: " + e.getMessage());
        }

        return clientes;
    }

    public String get(int id) {
        String sql = "SELECT nombre FROM clientes WHERE id = ?";
        try (Connection conexion = ConnectionFactory.getConnectionPrac2();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("nombre");
            }


        } catch (SQLException e) {
            logger.log("ERROR", "ClienteDAO", "Error al buscar cliente: " + e.getMessage());
        }
        return null;
    }

    public void update(int id, String nuevoEmail) {
        String sql = "UPDATE clientes SET email = ? WHERE id = ?";
        try (Connection conexion = ConnectionFactory.getConnectionPrac2();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, nuevoEmail);
            ps.setInt(2, id);
            ps.executeUpdate();

            logger.log("INFO", "ClienteDAO", "Cliente actualizado correctamente (ID " + id + ")");

        } catch (SQLException e) {
            logger.log("ERROR", "ClienteDAO", "Error al actualizar cliente: " + e.getMessage());
        }
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM clientes WHERE id = ?";

        try (Connection con = ConnectionFactory.getConnectionPrac2();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            logger.log("INFO", "ClienteDAO", "Cliente eliminado (ID " + id + ")");

        } catch (SQLException e) {
            logger.log("ERROR", "ClienteDAO", "Error al eliminar cliente: " + e.getMessage());
        }
    }

    public boolean exist(int id) {
        String sql = "SELECT COUNT(*) FROM clientes WHERE id = ?";

        try (Connection con = ConnectionFactory.getConnectionPrac2();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            logger.log("ERROR", "ClienteDAO", "Error al comprobar existencia: " + e.getMessage());
        }
        return false;
    }

}
