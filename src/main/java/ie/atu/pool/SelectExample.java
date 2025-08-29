package ie.atu.pool;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SelectExample {
    public static void main(String[] args) {
        String sql = "SELECT book_id, title, author, type, available FROM Books";

        try (Connection conn = DatabaseUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("=== All Books ===");
            while (rs.next()) {
                int id = rs.getInt("book_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String type = rs.getString("type");
                boolean available = rs.getBoolean("available");

                System.out.printf("%d | %s | %s | %s | available=%b%n",
                        id, title, author, type, available);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

