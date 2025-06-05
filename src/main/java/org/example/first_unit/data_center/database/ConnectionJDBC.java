package org.example.first_unit.data_center.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionJDBC {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionJDBC.class.getSimpleName());

    private static final String URL = "jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static ConnectionJDBC instance;
    private Connection connection;

    public ConnectionJDBC() throws SQLException {
        // String url = "jdbc:postgresql://localhost:5432/weather";
        // String username = "weather";
        // String password = "weather";

        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            LOG.info("Database Connection Created With success!!");
        } catch (ClassNotFoundException ex) {
            LOG.info("Database Connection Creation Failed : " + ex.getMessage());
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public static ConnectionJDBC getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConnectionJDBC();
        } else if (instance.getConnection().isClosed()) {
            instance = new ConnectionJDBC();
        }

        return instance;
    }
}
