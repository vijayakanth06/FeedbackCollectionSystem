import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateDatabase {
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:db/feedback.db")) {
            String createTables = """
                CREATE TABLE IF NOT EXISTS Students (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE,
                    password TEXT
                );
                CREATE TABLE IF NOT EXISTS Feedback (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    courseName TEXT,
                    staffName TEXT,
                    rating INTEGER,
                    comments TEXT,
                    studentId INTEGER,
                    FOREIGN KEY(studentId) REFERENCES Students(id)
                );
            """;
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(createTables);
                System.out.println("Database created successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
