public class Feedback {
    private Course course;
    private Staff staff;
    private int rating;
    private String comments;

    public Feedback(Course course, Staff staff, int rating, String comments) {
        this.course = course;
        this.staff = staff;
        this.rating = rating;
        this.comments = comments;
    }

    public Course getCourse() {
        return course;
    }

    public Staff getStaff() {
        return staff;
    }

    public int getRating() {
        return rating;
    }

    public String getComments() {
        return comments;
    }

    // Ensure this method returns a String
    public String displayFeedback() {
        return "Course: " + course.getCourseName() + ", Staff: " + staff.getStaffName() + 
               ", Rating: " + rating + ", Comments: " + comments;
    }
}
