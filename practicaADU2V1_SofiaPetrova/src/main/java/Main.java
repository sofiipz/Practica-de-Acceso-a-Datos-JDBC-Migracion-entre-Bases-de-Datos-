import dao.ClienteDAO;
import dao.DDLManager;
import dao.DMLManager;
import dao.MigracionManager;
import logger.ConfigLoader;
import logger.ConfigLog;
import logger.LoggerPropio;

import java.io.InputStream;

/**
 * Clase principal, demostración
 * @author Sofia Petrova
 */


public class Main {
    public static void main(String[] args) {
        InputStream input = Main.class.getClassLoader().getResourceAsStream("configlog.xml");
        ConfigLog config = ConfigLoader.cargar(input);
        LoggerPropio logger = new LoggerPropio(config);

        try {
            DDLManager ddl = new DDLManager(logger);
            ddl.crearTablasPrac2();
            ddl.crearTablasPrac2Migra();

            DMLManager dml = new DMLManager(logger);
            dml.insertarClientes();
            dml.insertarMuebles();
            dml.insertarPedidos();

            MigracionManager migracion = new MigracionManager(logger);
            migracion.migrarDatos();

            // probar la clase ClienteDAO

            ClienteDAO clienteDAO = new ClienteDAO(logger);

            // Insertar
            clienteDAO.insert("Sofía Petrova", "123A", "sofia@gmail.com", 500.0, true);

            // Listar
            System.out.println("Clientes actuales: " + clienteDAO.findAll());

            // Actualizar
            clienteDAO.update(1, "nuevoemail@gmail.com");

            // Comprobar existencia
            System.out.println("¿Existe cliente con ID 1?: " + clienteDAO.exist(1));

            // Eliminar
            clienteDAO.deleteById(1);

        } catch (Exception e) {
            logger.log("ERROR", "Main", "Error al insertar clientes: " + e.getMessage());

        }
    }
}
