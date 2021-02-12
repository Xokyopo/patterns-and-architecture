package ru.geekbrains.arhitecture.patterns.objectmapper.dao;

import ru.geekbrains.arhitecture.patterns.objectmapper.dao.exceptions.DAOException;
import ru.geekbrains.arhitecture.patterns.objectmapper.dao.orm.Connectable;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SQLiteConnection implements Connectable, Closeable {
    private String diverName;
    private String connectionString;

    private Connection connection;

    public SQLiteConnection() {
        try {
            this.loadProperties();
            Class.forName(diverName);
        } catch (ClassNotFoundException e) {
            throw new DAOException("Driver class not fount", e);
        } catch (IOException e) {
            throw new DAOException("Property file not found", e);
        }
    }

    @Override
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                initConnection();
            }
        } catch (SQLException e) {
            throw new DAOException("Can't connected to database", e);
        }
        return connection;
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DAOException("Can't close DataBase connection", e);
            }
        }
    }

    private void loadProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(this.getClass().getResourceAsStream("/databaseconnection.properties"));
        this.diverName = properties.getProperty("driverName");
        this.connectionString = properties.getProperty("connectionString");
    }

    private void initConnection() throws SQLException {
        connection = DriverManager.getConnection(connectionString);
    }
}
