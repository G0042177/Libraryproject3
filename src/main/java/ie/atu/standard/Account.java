package ie.atu.standard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class Account {

    private Account() {}

    public static void updateName(Connection conn, String updatedName, String email) {
        String sql = "UPDATE Members SET name = ? WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, updatedName);
            stmt.setString(2, email);
            stmt.executeUpdate();
            System.out.println("Updated.");
        } catch (SQLException ex) {
            System.out.println("Failed");
            ex.printStackTrace();
        }
    }

    public static void updateEmail(Connection conn, String updatedEmail, String currentEmail) {
        String sql = "UPDATE Members SET email = ? WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, updatedEmail);
            stmt.setString(2, currentEmail);
            stmt.executeUpdate();
            System.out.println("Updated.");
        } catch (SQLException ex) {
            System.out.println("Failed");
            ex.printStackTrace();
        }
    }

    public static void updateType(Connection conn, String updatedType, String email) {
        String sql = "UPDATE Members SET type = ? WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, updatedType);
            stmt.setString(2, email);
            stmt.executeUpdate();
            System.out.println("Updated.");
        } catch (SQLException ex) {
            System.out.println("Failed");
            ex.printStackTrace();
        }
    }

    public static void deleteAccount(Connection conn, String email) {
        deleteMember(conn, email);
    }


    public static void deleteMember(Connection conn, String email) {
        String findIdSql = "SELECT member_id FROM Members WHERE email = ?";
        String freeBooksSql =
                "UPDATE Books b " +
                        "JOIN Rentals r ON b.book_id = r.book_id " +
                        "SET b.quantity = b.quantity + 1 " +
                        "WHERE r.member_id = ? AND r.return_date IS NULL";
        String markReturnsSql =
                "UPDATE Rentals SET return_date = CURDATE() WHERE member_id = ? AND return_date IS NULL";
        String deleteRentalsSql = "DELETE FROM Rentals WHERE member_id = ?";
        String deleteMemberSql  = "DELETE FROM Members WHERE member_id = ?";

        try {
            int memberId = -1;
            try (PreparedStatement ps = conn.prepareStatement(findIdSql)) {
                ps.setString(1, email);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) memberId = rs.getInt("member_id");
                }
            }
            if (memberId == -1) {
                System.out.println("No account fouynd.");
                return;
            }

            boolean old = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement fb = conn.prepareStatement(freeBooksSql);
                 PreparedStatement mr = conn.prepareStatement(markReturnsSql);
                 PreparedStatement dr = conn.prepareStatement(deleteRentalsSql);
                 PreparedStatement dm = conn.prepareStatement(deleteMemberSql)) {


                fb.setInt(1, memberId); fb.executeUpdate();
                mr.setInt(1, memberId); mr.executeUpdate();


                dr.setInt(1, memberId); dr.executeUpdate();


                dm.setInt(1, memberId); dm.executeUpdate();

                conn.commit();
                System.out.println("Account deleted.");
            } catch (SQLException step) {
                conn.rollback();
                System.out.println("failed.");
                step.printStackTrace();
            } finally {
                conn.setAutoCommit(old);
            }
        } catch (SQLException ex) {
            System.out.println("error.");
            ex.printStackTrace();
        }
    }
}

