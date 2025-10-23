package studentapp.dao;

import studentapp.model.Student;
import studentapp.util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // CREATE
    public void addStudent(Student s) throws SQLException {
        String query = "INSERT INTO students (student_id, name, email, birth_date, major, gender) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, s.getStudentId());
            ps.setString(2, s.getName());
            ps.setString(3, s.getEmail());
            ps.setDate(4, new java.sql.Date(s.getBirthDate().getTime()));
            ps.setString(5, s.getMajor());
            ps.setString(6, s.getGender());
            ps.executeUpdate();
        }
    }

    // READ (semua mahasiswa)
    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        String query = "SELECT * FROM students";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                Student s = new Student(
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getDate("birth_date"),
                        rs.getString("major"),
                        rs.getString("gender")
                );
                list.add(s);
            }
        }
        return list;
    }

    public List<Student> searchStudentByNim(String nimKeyword) throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE student_id LIKE ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nimKeyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Student s = new Student(
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getDate("birth_date"),
                        rs.getString("major"),
                        rs.getString("gender")
                );
                list.add(s);
            }
        }
        return list;
    }


    // UPDATE
    public void updateStudent(Student s) throws SQLException {
        String query = "UPDATE students SET name=?, email=?, birth_date=?, major=?, gender=? WHERE student_id=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getEmail());
            ps.setDate(3, new java.sql.Date(s.getBirthDate().getTime()));
            ps.setString(4, s.getMajor());
            ps.setString(5, s.getGender());
            ps.setInt(6, s.getStudentId());
            ps.executeUpdate();
        }
    }

    // DELETE
    public void deleteStudent(int id) throws SQLException {
        String query = "DELETE FROM students WHERE student_id=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // SEARCH (by NIM)
    public Student getStudentById(int id) throws SQLException {
        String query = "SELECT * FROM students WHERE student_id=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getDate("birth_date"),
                        rs.getString("major"),
                        rs.getString("gender")
                );
            }
        }
        return null;
    }
}
