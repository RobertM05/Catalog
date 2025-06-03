package org.example.dao;

import org.example.model.Nota;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;
import java.util.UUID;

public class NotaDAO extends BaseDAO<Nota> {
    private static final Logger logger = LoggerFactory.getLogger(NotaDAO.class);

    public void adaugaNota(Nota nota) throws SQLException {
        logger.debug("Adaugare nota pentru student: {}, disciplina: {}", nota.getStudentId(), nota.getDisciplinaId());
        String sql = "INSERT INTO note (id, student_id, disciplina_id, nota, data_notare) VALUES (?,?,?,?,?)";
        
        executeUpdate(sql, stmt -> {
            setUUID(stmt, 1, nota.getId());
            setUUID(stmt, 2, nota.getStudentId());
            setUUID(stmt, 3, nota.getDisciplinaId());
            stmt.setInt(4, nota.getNota());
            stmt.setTimestamp(5, Timestamp.valueOf(nota.getDataNotarii()));
        });
        logger.info("Nota adaugata cu succes: {}", nota.getId());
    }

    public List<Nota> getNoteStudent(UUID studentId) throws SQLException {
        logger.debug("Preluare note pentru studentul: {}", studentId);
        return executeQueryList(
            "SELECT * FROM note WHERE student_id = ? ORDER BY data_notare DESC",
            stmt -> setUUID(stmt, 1, studentId)
        );
    }

    @Override
    protected Nota extractFromResultSet(ResultSet rs) throws SQLException {
        return new Nota(
            getUUID(rs, "id"),
            getUUID(rs, "student_id"),
            getUUID(rs, "disciplina_id"),
            rs.getInt("nota"),
            rs.getTimestamp("data_notare").toLocalDateTime()
        );
    }

    public double calculeazaMedieStudent(UUID studentId) throws SQLException {
        logger.debug("Calculare medie pentru studentul: {}", studentId);
        String sql = "SELECT AVG(nota) as medie FROM note WHERE student_id = ?";
        
        return executeQuery(sql, 
            stmt -> setUUID(stmt, 1, studentId),
            rs -> rs.next() ? rs.getDouble("medie") : 0.0
        );
    }
}
