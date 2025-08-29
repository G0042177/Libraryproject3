package ie.atu.pool;

import java.sql.*;

import javax.sql.DataSource;
import com.mysql.cj.jdbc.MysqlDataSource;

public final class DatabaseUtils {
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USERNAME = "root";       //
    private static final String PASSWORD = "password";   //

    private static final DataSource dataSource;

    // Static initializer runs once when the class is loaded
    static {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setURL(URL);
        ds.setUser(USERNAME);
        ds.setPassword(PASSWORD);



        dataSource = ds;
    }

    private DatabaseUtils() { /* no instances */ }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
