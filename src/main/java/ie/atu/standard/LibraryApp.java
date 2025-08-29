package ie.atu.standard;

import ie.atu.pool.DatabaseUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class LibraryApp {
    public static void main(String[] args) {
        Login login = new Login();
        Service service = new Service();
        Scanner sc = new Scanner(System.in);

        AccountInfo me = Signup.start(sc);
        if (me == null) {
            System.out.println("Login/Signup failed.");
            return;
        }
        service.login(me);
        System.out.println("Welcome " + me.getName() + " (" + me.getType() + ")");


        while (true) {
            boolean isStaff = "Staff".equalsIgnoreCase(me.getType());
            Menu.showMain(isStaff);
            String choice = sc.nextLine().trim().toUpperCase();

            switch (choice) {
                case "B": {
                    ArrayList<Book> allBooks = listBooks();
                    if (allBooks.isEmpty()) {
                        System.out.println("Not found.");
                    } else {
                        for (Book b : allBooks) System.out.println(b);
                    }
                    break;
                }

                case "S": {
                    System.out.print("Title or keywords: ");
                    String kw = sc.nextLine().trim();
                    ArrayList<Book> found = searchBooks(kw);
                    if (found.isEmpty()) {
                        System.out.println("Not found.");
                    } else {
                        for (Book b : found) System.out.println(b);
                    }
                    break;
                }

                case "R": {
                    System.out.print("Book ID: ");
                    int rBid = Integer.parseInt(sc.nextLine().trim());
                    boolean ok = Rental.borrowBook(me.getMemberId(), rBid);
                    System.out.println(ok ? "Rented." : "No copies.");
                    break;
                }

                case "T": {
                    System.out.print("Book ID: ");
                    int tBid = Integer.parseInt(sc.nextLine().trim());
                    boolean back = Rental.giveBackBook(me.getMemberId(), tBid);
                    System.out.println(back ? "Returned." : "N/A.");
                    break;
                }

                case "V": {
                    ArrayList<Rental> mine = Rental.getByMember(me.getMemberId());
                    if (mine.isEmpty()) {
                        System.out.println("No rentals.");
                    } else {
                        for (Rental r : mine) System.out.println(r);
                    }
                    break;
                }

                case "A": {
                    accountInfo(sc, me.getEmail());
                    break;
                }

                case "Z": {
                    if (!isStaff) {
                        System.out.println("unauthorized.");
                    } else {
                        staffAdmin(sc);
                    }
                    break;
                }

                case "X": {
                    System.out.println("Bye");
                    return;
                }

                default: {
                    System.out.println("Invalid");
                    break;
                }
            }
        }
    }

    private static ArrayList<Book> listBooks() {
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
            System.out.println("failed.");
            e.printStackTrace();
        }
        return books;
    }

    private static ArrayList<Book> searchBooks(String kw) {
        String sql = "SELECT book_id, title, author, type, quantity FROM Books WHERE title LIKE ? ORDER BY title";
        ArrayList<Book> results = new ArrayList<>();
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + kw + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(new Book(
                            rs.getInt("book_id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("type"),
                            rs.getInt("quantity")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("failed.");
            e.printStackTrace();
        }
        return results;
    }

    private static void accountInfo(Scanner sc, String email) {
        System.out.println("\n1) Update Name  2) Update Email  3) Update Type  4) Delete  5) Back");
        String s = sc.nextLine().trim();
        try (Connection conn = DatabaseUtils.getConnection()) {
            switch (s) {
                case "1": {
                    System.out.print("New name: ");
                    String nn = sc.nextLine().trim();
                    Account.updateName(conn, nn, email);
                    break;
                }
                case "2": {
                    System.out.print("New email: ");
                    String ne = sc.nextLine().trim();
                    Account.updateEmail(conn, ne, email);
                    break;
                }
                case "3": {
                    System.out.print("New type: ");
                    String nt = sc.nextLine().trim();
                    Account.updateType(conn, nt, email);
                    break;
                }
                case "4": {
                    Account.deleteAccount(conn, email);
                    System.out.println("deleted.");
                    System.exit(0);
                    break;
                }
                default: {
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println("failed.");
            e.printStackTrace();
        }
    }

    private static void staffAdmin(Scanner sc) {
        System.out.println("\nStaff Admin");
        System.out.println("1) Delete an account by email");
        System.out.println("2) Add new book");
        System.out.println("3) List all books");
        System.out.println("4) Delete a book by ID");
        System.out.println("5) Back");
        System.out.print("Select: ");
        String s = sc.nextLine().trim();

        try (Connection conn = DatabaseUtils.getConnection()) {
            switch (s) {
                case "1": {
                    System.out.print("email to delete: ");
                    String targetEmail = sc.nextLine().trim();
                    Staff.deleteAccountByEmail(conn, targetEmail);
                    break;
                }
                case "2": {
                    System.out.print("Book title: ");
                    String title = sc.nextLine().trim();
                    System.out.print("Author: ");
                    String author = sc.nextLine().trim();
                    System.out.print("Type Printed/EBook: ");
                    String type = sc.nextLine().trim();
                    System.out.print("Quantity: ");
                    int qty = Integer.parseInt(sc.nextLine().trim());
                    boolean ok = Staff.addBook(conn, title, author, type, qty);
                    System.out.println(ok ? "added." : "failed.");
                    break;
                }
                case "3": {
                    ArrayList<Book> all = Staff.listBooks();
                    if (all.isEmpty()) System.out.println("Not found.");
                    else for (Book b : all) System.out.println(b);
                    break;
                }
                case "4": {
                    System.out.print("Book ID to delete: ");
                    int bookId = Integer.parseInt(sc.nextLine().trim());
                    boolean ok = Staff.deleteBook(conn, bookId);
                    System.out.println(ok ? "deleted." : "Failed");
                    break;
                }
                default: {
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println("action failed.");
            e.printStackTrace();
        }
    }
}

