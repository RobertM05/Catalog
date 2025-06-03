package org.example.dao;

import org.example.model.Student;
import org.example.util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;
import java.util.UUID;

public class StudentDAO extends BaseDAO<Student> {
    private static final Logger logger = LoggerFactory.getLogger(StudentDAO.class);

    public void adaugaStudent(Student student) throws SQLException {
        logger.debug("Adaugare student: {}", student.getNume());
        String sql = "INSERT INTO studenti (id, nume, prenume, email, grupa) VALUES (?,?,?,?,?)";
        
        executeUpdate(sql, stmt -> {
            setUUID(stmt, 1, student.getId());
            stmt.setString(2, student.getNume());
            stmt.setString(3, student.getPrenume());
            stmt.setString(4, student.getEmail());
            stmt.setString(5, student.getGrupa());
        });
        logger.info("Student adaugat cu succes: {}", student.getId());
    }

    public List<Student> selectAllStudents() throws SQLException {
        logger.debug("Selectare toti studentii");
        return executeQueryList("SELECT * FROM studenti ORDER BY nume, prenume", stmt -> {});
    }

    @Override
    protected Student extractFromResultSet(ResultSet rs) throws SQLException {
        return new Student(
            getUUID(rs, "id"),
            rs.getString("nume"),
            rs.getString("prenume"),
            rs.getString("email"),
            rs.getString("grupa")
        );
    }

    public void stergeStudent(UUID studentId) throws SQLException {
        logger.info("Incercare de stergere student cu ID: {}", studentId);
        
        String deleteNoteSQL = "DELETE FROM note WHERE student_id = ?";
        String deleteStudentSQL = "DELETE FROM studenti WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                try (PreparedStatement pstmtNote = conn.prepareStatement(deleteNoteSQL)) {
                    pstmtNote.setString(1, studentId.toString());
                    pstmtNote.executeUpdate();
                }
                
                try (PreparedStatement pstmtStudent = conn.prepareStatement(deleteStudentSQL)) {
                    pstmtStudent.setString(1, studentId.toString());
                    int affectedRows = pstmtStudent.executeUpdate();
                    
                    if (affectedRows == 0) {
                        throw new SQLException("Stergerea studentului a esuat, niciun rand afectat.");
                    }
                }
                
                conn.commit();
                logger.info("Student sters cu succes, ID: {}", studentId);
                
            } catch (SQLException e) {
                conn.rollback();
                logger.error("Eroare la stergerea studentului: {}", e.getMessage());
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void actualizeazaStudent(Student student) throws SQLException {
        logger.debug("Actualizare student: {}", student.getNume());
        String sql = "UPDATE studenti SET nume = ?, prenume = ?, email = ?, grupa = ? WHERE id = ?";
        
        executeUpdate(sql, stmt -> {
            stmt.setString(1, student.getNume());
            stmt.setString(2, student.getPrenume());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getGrupa());
            setUUID(stmt, 5, student.getId());
        });
        logger.info("Student actualizat cu succes: {}", student.getId());
    }

    public Student getStudentDupaId(UUID id) throws SQLException {
        logger.debug("Cautare student cu ID: {}", id);
        return executeQuerySingle(
            "SELECT * FROM studenti WHERE id = ?",
            stmt -> setUUID(stmt, 1, id)
        ).orElse(null);
    }
}

