package dao;

import logger.LoggerPropio;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class ProcedimientosManager {

    private LoggerPropio logger;

    public ProcedimientosManager(LoggerPropio logger) {
        this.logger = logger;
    }

    public long upsertCliente(Connection conexion, String dni, String nombre, String email) {
        String sql = "{CALL sp_cliente_upsert(?, ?, ?, ?)}";
        long id = -1;

        try (CallableStatement cs = conexion.prepareCall(sql)) {
            cs.setString(1, dni);
            cs.setString(2, nombre);
            cs.setString(3, email);
            cs.registerOutParameter(4, Types.BIGINT);

            cs.execute();
            id = cs.getLong(4);
            logger.log("INFO", "ProcedimientosManager", "Cliente insertado/actualizado correctamente. DNI: " + dni + ", ID: " + id);


        } catch (SQLException e) {
            logger.log("ERROR", "ProcedimientosManager", "Error al ejecutar sp_cliente_upsert: " + e.getMessage());

        }

        return id;
    }
}
