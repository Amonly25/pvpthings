package com.ar.askgaming.pvpthings.DataBase;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import com.ar.askgaming.pvpthings.PvpThings;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariSource {
    
    private String databaseType;
    private String databaseUrl;
    private String username;
    private String password;
    private DatabaseType dbType;

    private HikariConfig hikariConfig;
    private HikariDataSource dataSource;

    private final PvpThings plugin;

    public HikariSource() {
        
        this.plugin = PvpThings.getInstance();
        load();
    }

    public enum DatabaseType {
        SQLITE,
        MYSQL
    }

    public void load(){
        databaseType = plugin.getConfig().getString("data_mode", "sqlite");
        hikariConfig = new HikariConfig();

        if (databaseType.equalsIgnoreCase("sqlite")) {
            // Load SQLite database
            loadSQLiteDatabase();
        } else if (databaseType.equalsIgnoreCase("mysql")) {
            // Load MySQL database
            loadMySQLDatabase();
        } else {
            plugin.getLogger().warning("Invalid database type specified in config.yml. Defaulting to SQLite.");
            loadSQLiteDatabase();
        }

        createTables();
    }
    private void createTables() {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            boolean isMySQL = meta.getURL().contains("mysql");

            String uuidType = isMySQL ? "VARCHAR(36)" : "TEXT"; // Usa VARCHAR(36) en MySQL, TEXT en SQLite

            String createTableSQL = "CREATE TABLE IF NOT EXISTS players (" +
                    "uuid " + uuidType + " PRIMARY KEY," +
                    "kills INTEGER DEFAULT 0," +
                    "deaths INTEGER DEFAULT 0," +
                    "killstreak INTEGER DEFAULT 0," +
                    "highestkillstreak INTEGER DEFAULT 0," +
                    "lastdeathtime INTEGER DEFAULT 0" +
                    ");";

            conn.createStatement().execute(createTableSQL);
        } catch (SQLException e) {
            plugin.getLogger().severe("Error creating tables: " + e.getMessage());
        }
    }

    private void loadSQLiteDatabase() {
        closeDataSource();
        
        dbType = DatabaseType.SQLITE;
        databaseUrl = "jdbc:sqlite:" + plugin.getDataFolder() + "/pvpthings.db";
        username = null; // SQLite does not require a username
        password = null; // SQLite does not require a password

        hikariConfig.setJdbcUrl(databaseUrl);

        hikariConfig.setMaximumPoolSize(1);  // Solo una conexión a la vez
        hikariConfig.setMinimumIdle(1);      // Siempre mantener una conexión abierta
        hikariConfig.setConnectionTimeout(30000); // Espera 30 seg antes de fallar al obtener conexión
        hikariConfig.setIdleTimeout(600000); // Cierra conexiones inactivas después de 10 min
        hikariConfig.setMaxLifetime(1800000); // Vida máxima de la conexión (30 min)

        hikariConfig.setPoolName("PvpThingsSQLitePool");

        dataSource = new HikariDataSource(hikariConfig);
        
    }
    private void loadMySQLDatabase() {
        dbType = DatabaseType.MYSQL;
        databaseUrl = "jdbc:mysql://" + plugin.getConfig().getString("mysql.url", "localhost:3306/pvpthings");
        username = plugin.getConfig().getString("mysql.username", "root");
        password = plugin.getConfig().getString("mysql.password", "password");

        hikariConfig.setJdbcUrl(databaseUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);

        hikariConfig.setMaximumPoolSize(10);  // Ajusta según tus necesidades
        hikariConfig.setMinimumIdle(2);       // Ajusta según tus necesidades
        hikariConfig.setConnectionTimeout(30000); // Espera 30 seg antes de fallar al obtener conexión
        hikariConfig.setIdleTimeout(600000); // Cierra conexiones inactivas después de 10 min
        hikariConfig.setMaxLifetime(1800000); // Vida máxima de la conexión (30 min)
        hikariConfig.setLeakDetectionThreshold(5000); // Detecta fugas después de 5 seg

        hikariConfig.setPoolName("PvpThingsMySQLPool");

        dataSource = new HikariDataSource(hikariConfig);
    }
    
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection(); // Lanza SQLException y obliga a manejarlo
    }
    
    private void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
    public DatabaseType getDbType() {
        return dbType;
    }
}
