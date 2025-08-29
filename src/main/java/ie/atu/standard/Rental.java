package ie.atu.standard;

import ie.atu.pool.DatabaseUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Rental {
    private final int rentalId;
    private final String title;
    private final Date rentalDate;
    private final Date returnDate;

    private static final String SQL_GET_BY_MEMBER =
            "SELECT r.rental_id, b.title, r.rental_date, r.return_date " +
                    "FROM Rentals r JOIN Books b ON r.book_id = b.book_id " +
                    "WHERE r.member_id = ? ORDER BY r.rental_date DESC";

    private static final String SQL_DEC_QTY =
            "UPDATE Books SET quantity = quantity - 1 WHERE book_id = ? AND quantity > 0";
    private static final String SQL_INSERT_RENTAL =
            "INSERT INTO Rentals (member_id, book_id, rental_date) VALUES (?, ?, CURDATE())";

    private static final String SQL_FIND_ACTIVE_RENTAL =
            "SELECT rental_id FROM Rentals WHERE member_id = ? AND book_id = ? AND return_date IS NULL " +
                    "ORDER BY rental_id DESC LIMIT 1";
    private static final String SQL_MARK_RETURNED =
            "UPDATE Rentals SET return_date = CURDATE() WHERE rental_id = ?";
    private static final String SQL_INC_QTY =
            "UPDATE Books SET quantity = quantity + 1 WHERE book_id = ?";

    public Rental(int rentalId, String title, Date rentalDate, Date returnDate) {
        this.rentalId = rentalId;
        this.title = title;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
    }

    public int getRentalId() { return rentalId; }
    public String getTitle() { return title; }
    public Date getRentalDate() { return rentalDate; }
    public Date getReturnDate() { return returnDate; }

    @Override
    public String toString() {
        return "#" + rentalId + " | \"" + title + "\" | out: " + rentalDate + " | returned: " + returnDate;
    }


    public static ArrayList<Rental> getByMember(int memberId) {
        ArrayList<Rental> list = new ArrayList<>();
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_BY_MEMBER)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Rental(
                            rs.getInt("rental_id"),
                            rs.getString("title"),
                            rs.getDate("rental_date"),
                            rs.getDate("return_date")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed/ no rentals");
            e.printStackTrace();
        }
        return list;
    }

    public static boolean borrowBook(int memberId, int bookId) {
        try (Connection conn = DatabaseUtils.getConnection()) {
            boolean oldAuto = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try {
                int changed;
                try (PreparedStatement dec = conn.prepareStatement(SQL_DEC_QTY)) {
                    dec.setInt(1, bookId);
                    changed = dec.executeUpdate();
                }
                if (changed == 0) {
                    conn.setAutoCommit(oldAuto);
                    return false;
                }

                try (PreparedStatement ins = conn.prepareStatement(SQL_INSERT_RENTAL)) {
                    ins.setInt(1, memberId);
                    ins.setInt(2, bookId);
                    ins.executeUpdate();
                }

                conn.commit();
                return true;
            } catch (SQLException step) {
                conn.rollback();
                return false;
            } finally {
                conn.setAutoCommit(oldAuto);
            }
        } catch (SQLException e) {
            System.out.println("failed.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean giveBackBook(int memberId, int bookId) {
        try (Connection conn = DatabaseUtils.getConnection()) {
            boolean oldAuto = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try {
                Integer rentalId = null;
                try (PreparedStatement find = conn.prepareStatement(SQL_FIND_ACTIVE_RENTAL)) {
                    find.setInt(1, memberId);
                    find.setInt(2, bookId);
                    try (ResultSet rs = find.executeQuery()) {
                        if (rs.next()) rentalId = rs.getInt(1);
                    }
                }
                if (rentalId == null) {
                    conn.setAutoCommit(oldAuto);
                    return false;
                }

                try (PreparedStatement mark = conn.prepareStatement(SQL_MARK_RETURNED)) {
                    mark.setInt(1, rentalId);
                    mark.executeUpdate();
                }
                try (PreparedStatement inc = conn.prepareStatement(SQL_INC_QTY)) {
                    inc.setInt(1, bookId);
                    inc.executeUpdate();
                }

                conn.commit();
                return true;
            } catch (SQLException step) {
                conn.rollback();
                return false;
            } finally {
                conn.setAutoCommit(oldAuto);
            }
        } catch (SQLException e) {
            System.out.println("failed.");
            e.printStackTrace();
            return false;
        }
    }
}

