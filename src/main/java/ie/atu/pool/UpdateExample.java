package ie.atu.pool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateExample {
    public static void main(String[] args) {
        String updateSQL = "UPDATE Members SET email = ? WHERE member_id = ?";

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSQL)) {

            // Example values
            statement.setString(1, "alice.new@example.com");
            statement.setInt(2, 1); // member_id = 1

            int rowsUpdated = statement.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
