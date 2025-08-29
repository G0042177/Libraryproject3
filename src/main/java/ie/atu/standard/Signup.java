package ie.atu.standard;

import ie.atu.pool.DatabaseUtils;

import java.sql.*;
import java.util.Scanner;


public class Signup {

    public static AccountInfo start(Scanner sc) {
        while (true) {
            System.out.print("Press 'L' Login or 'S' Sign up? ");
            String pick = sc.nextLine().trim().toUpperCase();

            if ("L".equals(pick)) {
                return doLogin(sc);
            } else if ("S".equals(pick)) {
                boolean ok = doSignup(sc);
                if (ok) {
                    return doLogin(sc);
                } else {
                    System.out.println("failed.");
                }
            } else {
                System.out.println("press L or S.");
            }
        }
    }


    private static AccountInfo doLogin(Scanner sc) {
        System.out.print("Email: ");
        String email = sc.nextLine().trim();

        Login login = new Login();
        AccountInfo info = login.login(email);
        if (info == null) {
            System.out.println("not found.");
        }
        return info;
    }


    private static boolean doSignup(Scanner sc) {
        System.out.println("\nCreate Account");
        System.out.print("Enter name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter email: ");
        String email = sc.nextLine().trim();


        System.out.print("Type Student or Staff: ");
        String type = sc.nextLine().trim();
        if (!"Student".equalsIgnoreCase(type) && !"Staff".equalsIgnoreCase(type)) {
            type = "Student";
        }

        if (name.isEmpty() || email.isEmpty()) {
            System.out.println("Enter name and email");
            return false;
        }

        if (emailExists(email)) {
            System.out.println("account with email exits");
            return false;
        }

        String sql = "INSERT INTO Members (name, email, type) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, capitalise(type));
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Account made" : "failed to create");
            return rows > 0;

        } catch (SQLIntegrityConstraintViolationException dup) {

            System.out.println("account with email exits");
            return false;
        } catch (SQLException e) {
            System.out.println("error.");
            e.printStackTrace();
            return false;
        }
    }

    private static boolean emailExists(String email) {
        String sql = "SELECT 1 FROM Members WHERE email = ? LIMIT 1";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return true;
        }
    }

    private static String capitalise(String s) {
        if (s == null || s.isEmpty()) return s;
        String lower = s.toLowerCase();
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
