package uo.ri.util.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Jdbc {

    private static final ThreadLocal<
            Connection> threadConnection = new ThreadLocal<>();

    private static final String URL = ConnectionProperties
            .getProperty("DB_URL");
    private static final String USER = ConnectionProperties
            .getProperty("DB_USER");
    private static final String PASS = ConnectionProperties
            .getProperty("DB_PASS");

    public static Connection createThreadConnection() throws SQLException {

        Connection con = DriverManager.getConnection(URL, USER, PASS);
        threadConnection.set(con);
        return con;
    }

    public static Connection getCurrentConnection() {
        return threadConnection.get();
    }

}
