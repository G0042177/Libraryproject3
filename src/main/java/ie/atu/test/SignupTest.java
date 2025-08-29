package ie.atu.test;

import ie.atu.pool.DatabaseUtils;
import ie.atu.standard.AccountInfo;
import ie.atu.standard.Login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SignupTest {

    public static void main(String[] args) {
        testSignupAndLogin();
    }

    public static void testSignupAndLogin() {

        String name  = "sam";
        String email = "sam@school.com";
        String type  = "Student";
        Integer newId = null;
        boolean passed = true;

        try (Connection conn = DatabaseUtils.getConnection()) {


            try (PreparedStatement del = conn.prepareStatement("DELETE FROM Members WHERE email = ?")) {
                del.setString(1, email);
                del.executeUpdate();
            }

            try (PreparedStatement ins = conn.prepareStatement(
                    "INSERT INTO Members (name, email, type) VALUES (?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS)) {

                ins.setString(1, name);
                ins.setString(2, email);
                ins.setString(3, type);
                ins.executeUpdate();

                try (ResultSet keys = ins.getGeneratedKeys()) {
                    if (keys.next()) newId = keys.getInt(1);
                }
            }


            passed &= assertTrue(newId != null && newId > 0, "expected generated member_id");


            Login login = new Login();
            AccountInfo info = login.login(email);


            passed &= assertEquals(email, info != null ? info.getEmail() : null, "email should match");
            passed &= assertEquals(name,  info != null ? info.getName()  : null, "name should match");
            passed &= assertEquals(type,  info != null ? info.getType()  : null, "type should match");

        } catch (Exception e) {
            e.printStackTrace();
            passed = false;
        } finally {

            try (Connection conn = DatabaseUtils.getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM Members WHERE email = ?")) {
                ps.setString(1, email);
                ps.executeUpdate();
            } catch (Exception ignore) {}
        }


        if (passed) {
            System.out.println("Test passed.");
        } else {
            System.out.println("Test failed.");
        }
    }


    public static boolean assertEquals(Object expected, Object actual, String msg) {
        boolean ok = (expected == null) ? actual == null : expected.equals(actual);
        if (!ok) {
            System.out.println("assertEquals failed: " + msg +
                    " (expected=" + expected + ", actual=" + actual + ")");
        }
        return ok;
    }

    public static boolean assertTrue(boolean condition, String msg) {
        if (!condition) {
            System.out.println("assertTrue failed: " + msg);
        }
        return condition;
    }
}
