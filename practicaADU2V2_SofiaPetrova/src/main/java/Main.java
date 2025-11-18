import dao.*;
import logger.ConfigLoader;
import logger.ConfigLog;
import logger.LoggerPropio;
import util.ConnectionPoolFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        InputStream input = Main.class.getClassLoader().getResourceAsStream("configlog.xml");
        ConfigLog config = ConfigLoader.cargar(input);
        LoggerPropio logger = new LoggerPropio(config);

        try (Connection conexionPrac2 = ConnectionPoolFactory.getDataSourcePrac2().getConnection();
             Connection conexionMigra = ConnectionPoolFactory.getDataSourcePrac2Migra().getConnection()) {

            logger.log("INFO", "Main", "Conexión MySQL (prac2): " + (conexionPrac2 != null));
            logger.log("INFO", "Main", "Conexión H2 (prac2migra): " + (conexionMigra != null));

            // crear las tablas
            DDLManager ddlManager = new DDLManager(logger);
            ddlManager.crearTablasPrac2();
            ddlManager.crearTablasPrac2Mig();

            //insertar los datos en Prac2
            DMLManager dmlManager = new DMLManager(logger);
            dmlManager.insertarDatos();

            //probar el procedimeinto
            ProcedimientosManager procedimiento = new ProcedimientosManager(logger);
            long id1 = procedimiento.upsertCliente(conexionPrac2, "123456789", "Sofía", "sofia@gmail.com");
            long id2 = procedimiento.upsertCliente(conexionPrac2, "456987123", "Sofía", "sofia.p@mail.com");

            //mismo DNI pero con otro nombre y email para actualizar
            long idActualizado = procedimiento.upsertCliente(conexionPrac2, "123456789", "Sofía P.", "sofia.petrova@gmail.com");


            //migrar los datos de Prac2 a Prac2Mig
            MigracionManager migrarDatos = new MigracionManager(logger);
            migrarDatos.migrarDatos();

            //hacer el fichero con info de la migración
            FicheroMigracion ficheroInfo = new FicheroMigracion(logger);
            ficheroInfo.generarInformeMigracion(conexionPrac2, conexionMigra, "resultados_migracion.txt");

        } catch (SQLException e) {
            logger.log("ERROR", "Main", "Error al conectar: " + e.getMessage());
        }
    }
}
