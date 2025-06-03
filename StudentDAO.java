package org.example.dao;

import org.example.model.Student;
import org.example.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StudentDAO {
    public void adaugaStudent(Student student) throws SQLException {
        String sql = "INSERT INTO studenti (id, nume, prenume, email, grupa) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getId().toString());
            stmt.setString(2, student.getNume());
            stmt.setString(3, student.getPrenume());
            stmt.setString(4, student.getEmail());
            stmt.setString(5, student.getGrupa());
            stmt.executeUpdate();
        }
    }

    public List<Student> selectAllStudents() throws SQLException {
        List<Student> studenti = new ArrayList<>();
        String sql = "SELECT * FROM studenti";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                UUID id = UUID.fromString(rs.getString("id"));
                String nume = rs.getString("nume");
                String prenume = rs.getString("prenume");
                String email = rs.getString("email");
                String grupa = rs.getString("grupa");

                Student student = new Student(id, nume, prenume, email, grupa);
                studenti.add(student);
            }
        }
        return studenti;
    }

    public void actualizaStudent(Student student) throws SQLException {
        String sql = "UPDATE studenti SET nume = ?, prenume = ?, email = ?, grupa = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getNume());
            stmt.setString(2, student.getPrenume());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getGrupa());
            stmt.setString(5, student.getId().toString());
            stmt.executeUpdate();
        }
    }

    public void stergeStudent(UUID id) throws SQLException {
        String sql = "DELETE FROM studenti WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id.toString());
            stmt.executeUpdate();
        }
    }
}
