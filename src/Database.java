import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:sqlite:db/feedback.db";

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return connection;
    }

    public static void saveFeedback(Feedback feedback) {
        String sql = "INSERT INTO feedback(course_name, staff_name, rating, comments) VALUES(?,?,?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, feedback.getCourse().getCourseName());
            pstmt.setString(2, feedback.getStaff().getStaffName());
            pstmt.setInt(3, feedback.getRating());
            pstmt.setString(4, feedback.getComments());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving feedback: " + e.getMessage());
        }
    }
}
