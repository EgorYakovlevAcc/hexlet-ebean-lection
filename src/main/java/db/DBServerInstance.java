package db;

import io.ebean.EbeanServer;
import io.ebean.EbeanServerFactory;
import io.ebean.config.ServerConfig;

import java.util.Properties;

public class DBServerInstance {
    private static EbeanServer ebeanServer;

    private DBServerInstance() {
    }

    public static EbeanServer getInstance() {
        if (ebeanServer == null) {
            ebeanServer = getEbeanServer();
        }
        return ebeanServer;
    }

    private static EbeanServer getEbeanServer() {
        ServerConfig cfg = new ServerConfig();

        Properties properties = new Properties();
//        properties.put("ebean.db.ddl.generate", "true");
//        properties.put("ebean.db.ddl.run", "true");
        properties.put("datasource.db.username", "postgres");
        properties.put("datasource.db.password", "root");
        properties.put("datasource.db.databaseUrl", "jdbc:postgresql://localhost:5432/hexlet_example_db");
        properties.put("datasource.db.databaseDriver", "org.postgresql.Driver");

        cfg.loadFromProperties(properties);
        return EbeanServerFactory.create(cfg);
    }
}
