package dao;

import logger.LoggerPropio;
import util.ConnectionPoolFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DDLManager {
    private LoggerPropio logger;

    public DDLManager(LoggerPropio logger) {
        this.logger = logger;
    }


    public void crearTablasPrac2() {
        try(Connection conexion = ConnectionPoolFactory.getDataSourcePrac2().getConnection();
        Statement st = conexion.createStatement()) {

            logger.log("INFO", "DDLManager", "Creando tablas en prac2...");

            st.executeUpdate("DROP TABLE IF EXISTS pedidos");
            st.executeUpdate("DROP TABLE IF EXISTS muebles");
            st.executeUpdate("DROP TABLE IF EXISTS clientes");

            String sqlCrearClientes = "CREATE TABLE clientes (" +
                                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                "nombre VARCHAR(100), " +
                                "dni CHAR(9), " +
                                "email VARCHAR(120), " +
                                "fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP, "+
                                "credito DECIMAL(10,2), " +
                                "activo TINYINT(1) DEFAULT 1)";

            String sqlCrearMuebles = "CREATE TABLE muebles (" +
                                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                "nombre VARCHAR(150), "+
                                "descripcion TEXT, "+
                                "precio DECIMAL(10,2), "+
                                "stock INT, "+
                                "categoria VARCHAR(50), "+
                                "activo TINYINT(1))";

            String sqlCrearPedidos = "CREATE TABLE pedidos (" +
                                "id INT AUTO_INCREMENT PRIMARY KEY," +
                                "cliente_id INT, " +
                                "fecha DATETIME, " +
                                "total DECIMAL(10,2), " +
                                "estado VARCHAR(30), " +
                                "direccion VARCHAR(200), " +
                                "comentarios TEXT, " +
                                "FOREIGN KEY (cliente_id) REFERENCES clientes(id))";

            st.executeUpdate(sqlCrearClientes);
            st.executeUpdate(sqlCrearMuebles);
            st.executeUpdate(sqlCrearPedidos);

            logger.log("INFO", "DDLManager", "Tablas creadas correctamente en prac2");

        } catch (SQLException e) {
            logger.log("ERROR", "DDLManager", "Error al crear tablas en prac2: " + e.getMessage());
        }
    }

    public void crearTablasPrac2Mig () {
        try(Connection conexion = ConnectionPoolFactory.getDataSourcePrac2Migra().getConnection();
          Statement st = conexion.createStatement()) {

            logger.log("INFO", "DDLManager", "Creando tablas en prac2migra...");

            st.executeUpdate("DROP TABLE IF EXISTS pedidos_migra");
            st.executeUpdate("DROP TABLE IF EXISTS muebles_migra");
            st.executeUpdate("DROP TABLE IF EXISTS clientes_migra");

            String sqlTablaClientesMigra = "CREATE TABLE clientes_migra (" +
                                "id_migra INT AUTO_INCREMENT PRIMARY KEY,"+
                                "nombre_migra VARCHAR(100), " +
                                "dni_migra CHAR(9), " +
                                "email_migra VARCHAR(120), " +
                                "fecha_registro_migra DATETIME, " +
                                "credito_migra DECIMAL(10,2), " +
                                "activo_migra TINYINT(1), " +
                                "migrado_migra TINYINT(1) DEFAULT 0)";

            String sqlTablaMueblesMigra = "CREATE TABLE muebles_migra (" +
                                "id_migra INT AUTO_INCREMENT PRIMARY KEY, " +
                                "nombre_migra VARCHAR(150), " +
                                "precio_migra DECIMAL(10,2), " +
                                "categoria_migra VARCHAR(50), " +
                                "activo_migra TINYINT(1), " +
                                "migrado_migra TINYINT(1) DEFAULT 0)";


            String sqlTablaPedidosMigra = "CREATE TABLE pedidos_migra (" +
                                "id_migra INT AUTO_INCREMENT PRIMARY KEY," +
                                "cliente_id_migra INT, " +
                                "fecha_migra DATETIME, " +
                                "total_migra DECIMAL(10,2), " +
                                "estado_migra VARCHAR(30), " +
                                "direccion_migra VARCHAR(200), " +
                                "migrado_migra TINYINT(1) DEFAULT 0, " +
                                "FOREIGN KEY (cliente_id_migra) REFERENCES clientes_migra(id_migra))";

            st.executeUpdate(sqlTablaClientesMigra);
            st.executeUpdate(sqlTablaMueblesMigra);
            st.executeUpdate(sqlTablaPedidosMigra);

            logger.log("INFO", "DDLManager", "Tablas creadas correctamente en prac2migra");


        } catch (SQLException e) {
            logger.log("ERROR", "DDLManager", "Error al crear tablas en prac2migra: " + e.getMessage());
        }
    }

}
