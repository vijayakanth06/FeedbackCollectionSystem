import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Main {
    private static final String DB_URL = "jdbc:sqlite:C:\\Users\\vikym\\Downloads\\FeedbackCollectionSystem\\db\\feedback.db";
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> courseBox;
    private JComboBox<Integer> ratingBox;
    private JTextArea commentsArea;
    private JLabel staffLabel;
    private int studentId;

    public Main() {
        frame = new JFrame("Feedback Collection System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Login and Signup Panel
        JPanel loginPanel = new JPanel();
        usernameField = new JTextField(10);
        passwordField = new JPasswordField(10);
        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Sign Up");

        loginButton.addActionListener(new LoginListener());
        signupButton.addActionListener(new SignupListener());

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(signupButton);

        frame.add(loginPanel, BorderLayout.NORTH);

        // Feedback Submission Panel
        JPanel feedbackPanel = new JPanel(new GridLayout(6, 2));
        feedbackPanel.add(new JLabel("Select Course:"));

        String[] courses = {
            "Design and Analysis of Algorithms", "Data Processing and Visualization", 
            "Discrete Mathematics and Linear Algebra", "Machine Learning", 
            "Java Programming", "Environmental Science"
        };
        courseBox = new JComboBox<>(courses);
        courseBox.addActionListener(e -> updateStaff());

        feedbackPanel.add(courseBox);
        feedbackPanel.add(new JLabel("Staff:"));
        staffLabel = new JLabel();
        feedbackPanel.add(staffLabel);

        feedbackPanel.add(new JLabel("Rating (1-5):"));
        ratingBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        feedbackPanel.add(ratingBox);

        feedbackPanel.add(new JLabel("Comments:"));
        commentsArea = new JTextArea(3, 20);
        feedbackPanel.add(new JScrollPane(commentsArea));

        JButton submitButton = new JButton("Submit Feedback");
        submitButton.addActionListener(new SubmitButtonListener());
        feedbackPanel.add(submitButton);

        JButton viewFeedbackButton = new JButton("View Feedback");
        viewFeedbackButton.addActionListener(new ViewFeedbackListener());
        feedbackPanel.add(viewFeedbackButton);

        frame.add(feedbackPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    private void updateStaff() {
        String[] staff = {"Ms.S.Priyanka", "Ms.s.Benil Jeniffer", "Dr.V.S.HemaChandira", 
                          "Ms.S.Santhiya", "Ms.A.S.Renugadevi", "Dr.A.Revathi"};
        staffLabel.setText(staff[courseBox.getSelectedIndex()]);
    }

    private class LoginListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (login(username, password)) {
                JOptionPane.showMessageDialog(frame, "Login Successful");
            } else {
                JOptionPane.showMessageDialog(frame, "Login Failed. Please try again.");
            }
        }
    }

    private class SignupListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (signup(username, password)) {
                JOptionPane.showMessageDialog(frame, "Sign-Up Successful. Please log in.");
            } else {
                JOptionPane.showMessageDialog(frame, "Sign-Up Failed. Username may already exist.");
            }
        }
    }

    private boolean login(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String query = "SELECT id FROM Students WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                studentId = rs.getInt("id");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean signup(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String insert = "INSERT INTO Students (username, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insert);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private class SubmitButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (studentId > 0) {
                String courseName = (String) courseBox.getSelectedItem();
                String staffName = staffLabel.getText();
                int rating = (int) ratingBox.getSelectedItem();
                String comments = commentsArea.getText();
                saveFeedback(courseName, staffName, rating, comments);
            } else {
                JOptionPane.showMessageDialog(frame, "Please login first.");
            }
        }
    }

    private void saveFeedback(String courseName, String staffName, int rating, String comments) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String insert = "INSERT INTO Feedback (courseName, staffName, rating, comments, studentId) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insert);
            stmt.setString(1, courseName);
            stmt.setString(2, staffName);
            stmt.setInt(3, rating);
            stmt.setString(4, comments);
            stmt.setInt(5, studentId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Feedback submitted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private class ViewFeedbackListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (studentId > 0) {
                viewFeedback();
            } else {
                JOptionPane.showMessageDialog(frame, "Please login first.");
            }
        }
    }

    private void viewFeedback() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String query = "SELECT * FROM Feedback WHERE studentId = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, studentId);  
            ResultSet rs = stmt.executeQuery();
            StringBuilder feedback = new StringBuilder("Your Feedback:\n");
    
            while (rs.next()) {
                feedback.append("Course: ").append(rs.getString("courseName")).append("\n")
                        .append("Staff: ").append(rs.getString("staffName")).append("\n")
                        .append("Rating: ").append(rs.getInt("rating")).append("\n")
                        .append("Comments: ").append(rs.getString("comments")).append("\n\n");
            }
    
            JOptionPane.showMessageDialog(frame, feedback.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
