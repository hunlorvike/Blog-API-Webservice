package group.six.api.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider implements AutoCloseable {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static final String DATABASE_NAME = "management";

    private Connection connection;

    public ConnectionProvider() {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(DB_URL + DATABASE_NAME, DB_USER, DB_PASSWORD);
            if (connection != null) {
                System.out.println("Kết nối đến cơ sở dữ liệu thành công");
            } else {
                System.out.println("Kết nối đến cơ sở dữ liệu thất bại");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Kết nối đến cơ sở dữ liệu thất bại");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}
