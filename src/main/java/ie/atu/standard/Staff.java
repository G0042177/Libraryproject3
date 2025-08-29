package ie.atu.standard;

import ie.atu.pool.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;


public class Staff {


    public static boolean isStaff(String email) {
        String sql = "SELECT type FROM Members WHERE email = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return "Staff".equalsIgnoreCase(rs.getString("type"));
                }
            }
        } catch (SQLException e) {
            System.out.println("check failed.");
            e.printStackTrace();
        }
        return false;
    }


    public static void deleteAccountByEmail(Connection conn, String targetEmail) {
        Account.deleteMember(conn, targetEmail);
    }


    public static ArrayList<Book> listBooks() {
        String sql = "SELECT book_id, title, author, type, quantity FROM Books ORDER BY book_id";
        ArrayList<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseUtils.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("type"),
                        rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            System.out.println("book listing failed.");
            e.printStackTrace();
        }
        return books;
    }

    public static boolean addBook(Connection conn, String title, String author, String type, int quantity) {
        String sql = "INSERT INTO Books (title, author, type, quantity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, type);
            ps.setInt(4, quantity);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("book listing failed.");
            e.printStackTrace();
            return false;
        }
    }


    public static boolean deleteBook(Connection conn, int bookId) {
        String delRentals = "DELETE FROM Rentals WHERE book_id = ?";
        String delBook    = "DELETE FROM Books WHERE book_id = ?";

        try {
            boolean old = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement dr = conn.prepareStatement(delRentals);
                 PreparedStatement db = conn.prepareStatement(delBook)) {

                dr.setInt(1, bookId);
                dr.executeUpdate();

                db.setInt(1, bookId);
                int rows = db.executeUpdate();

                conn.commit();
                System.out.println("Deleted  " + rows);
                return rows > 0;
            } catch (SQLException step) {
                conn.rollback();
                System.out.println("Delete failed ");
                step.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(old);
            }
        } catch (SQLException e) {
            System.out.println("Delete failed");
            e.printStackTrace();
            return false;
        }
    }
}
