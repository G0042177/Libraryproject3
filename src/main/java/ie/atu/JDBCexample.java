package ie.atu;

import java.sql.*;

public class JDBCexample {

        public static void main(String[] args) {
            String url = "jdbc:mysql://localhost:3306/library_db";
            String user = "root";
            String password = "password";

            String sql = "SELECT * FROM Books";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    int id = rs.getInt("book_id");
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    String type = rs.getString("type");
                    boolean available = rs.getBoolean("available");

                    System.out.printf("%d | %s | %s | %s | %b%n",
                            id, title, author, type, available);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


