import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isValidUser(String enteredPassword) {
        return this.password.equals(enteredPassword);
    }
}

class Reservation {  public void makeReservation(User user) {
    // Implement res
    System.out.println("Reservation process is started");
    System.out.println("Reservation is successful for user: " + user.getUsername());
    System.out.println("Database status updated: Reservation saved.");
}

    public void cancelReservation(User user, String pnrNumber) {
        // Implement reservation cancellation in the database
        System.out.println("Cancellation process is started");
        System.out.println("Reservation is cancelled for user: " + user.getUsername() + ", PNR: " + pnrNumber);
        System.out.println("Database status updated: Reservation cancelled.");
    }
}

public class OnlineReservation {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            // Database connection
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinereservation", "root", "")) {
                System.out.println("Provide your username: ");
                String username = sc.nextLine();
                System.out.println("Enter your password: ");
                String password = sc.nextLine();

                // Query to fetch user details
                String query = "SELECT password FROM users WHERE Username = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, username);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            String dbPassword = rs.getString("password");
                            User user = new User(username, dbPassword);
                            if (user.isValidUser(password)) {
                                Reservation reservation = new Reservation();
                                System.out.println("Login successful for " + user.getUsername() + "!");
                                System.out.println("1. Make Reservation");
                                System.out.println("2. Cancel Reservation");
                                System.out.println("Please select an option: ");
                                int option = sc.nextInt();
                                sc.nextLine();
                                switch (option) {
                                    case 1:
                                        reservation.makeReservation(user);
                                        break;
                                    case 2:
                                        System.out.println("Enter your PNR number: ");
                                        String pnrNumber = sc.nextLine();
                                        reservation.cancelReservation(user, pnrNumber);
                                        break;
                                    default:
                                        System.out.println("Select option correctly!");
                                }
                            } else {
                                System.out.println("Invalid password.");
                            }
                        } else {
                            System.out.println("User not found.");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}