package util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * Crea pools de conexiones con HikariCP para prac2 (MySQL) y prac2migra (H2)
 * @author Sofia
 */

public class ConnectionPoolFactory {
    private static HikariDataSource dataSourcePrac2;
    private static HikariDataSource dataSourcePrac2Migra;

    static {
        // --- MySQL ---
        HikariConfig mysqlConfig = new HikariConfig();
        mysqlConfig.setJdbcUrl("jdbc:mysql://localhost:3306/prac2");
        mysqlConfig.setUsername("root");
        mysqlConfig.setPassword("root");
        mysqlConfig.setMaximumPoolSize(5);
        dataSourcePrac2 = new HikariDataSource(mysqlConfig);

        // --- H2 embebida ---
        HikariConfig h2Config = new HikariConfig();
        h2Config.setJdbcUrl("jdbc:h2:~/prac2migra;MODE=MySQL;DB_CLOSE_ON_EXIT=FALSE;AUTO_RECONNECT=TRUE");
        h2Config.setUsername("sa");
        h2Config.setPassword("");
        h2Config.setMaximumPoolSize(5);
        dataSourcePrac2Migra = new HikariDataSource(h2Config);
    }

    public static DataSource getDataSourcePrac2() {
        return dataSourcePrac2;
    }

    public static DataSource getDataSourcePrac2Migra() {
        return dataSourcePrac2Migra;
    }
}
