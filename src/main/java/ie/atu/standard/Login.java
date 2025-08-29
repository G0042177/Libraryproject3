package ie.atu.standard;

import ie.atu.pool.DatabaseUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
    public AccountInfo login(String email) {
        String sql = "SELECT member_id, name, email, type FROM Members WHERE email = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AccountInfo(
                            rs.getInt("member_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("type")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Login error.");
            e.printStackTrace();
        }
        return null;
    }
}
