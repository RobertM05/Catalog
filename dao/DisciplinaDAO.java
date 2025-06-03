package org.example.dao;

import org.example.model.Disciplina;
import org.example.util.DatabaseConnection;
import org.example.util.TransactionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DisciplinaDAO extends BaseDAO<Disciplina> {

    public void adaugaDisciplina(Disciplina disciplina) throws SQLException {
        logger.debug("Adaugare disciplina: {}", disciplina.getNume());
        String sql = "INSERT INTO discipline (id, nume, acronim, tip_evaluare) VALUES (?,?,?,?)";
        
        executeUpdate(sql, stmt -> {
            setUUID(stmt, 1, disciplina.getId());
            stmt.setString(2, disciplina.getNume());
            stmt.setString(3, disciplina.getAcronim());
            stmt.setString(4, disciplina.getTipEvaluare());
        });
        logger.info("Disciplina adaugata cu succes: {}", disciplina.getId());
    }

    public void actualizeazaDisciplina(Disciplina disciplina) throws SQLException {
        String sql = "UPDATE discipline SET nume = ?, acronim = ?, tip_evaluare = ? WHERE id = ?";
        executeUpdate(sql, stmt -> {
            stmt.setString(1, disciplina.getNume());
            stmt.setString(2, disciplina.getAcronim());
            stmt.setString(3, disciplina.getTipEvaluare());
            setUUID(stmt, 4, disciplina.getId());
        });
        logger.info("Disciplina actualizata cu succes: {}", disciplina.getId());
    }

    public void stergeDisciplina(UUID id) throws SQLException {
        logger.debug("Stergere disciplina cu ID: {}", id);
        
        TransactionManager.executeInTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM note WHERE id_disciplina = ?")) {
                setUUID(stmt, 1, id);
                int noteSterse = stmt.executeUpdate();
                logger.info("Au fost sterse {} note asociate disciplinei {}", noteSterse, id);
            }

            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM discipline WHERE id = ?")) {
                setUUID(stmt, 1, id);
                int result = stmt.executeUpdate();
                if (result == 0) {
                    throw new SQLException("Disciplina cu ID-ul " + id + " nu a fost gasita");
                }
                logger.info("Disciplina {} a fost stearsa cu succes", id);
            }
            return null;
        });
    }

    public List<Disciplina> getAllDiscipline() throws SQLException {
        logger.debug("Selectare toate disciplinele");
        return executeQueryList("SELECT * FROM discipline ORDER BY nume", stmt -> {});
    }

    public Disciplina getDisciplinaDupaId(UUID id) throws SQLException {
        logger.debug("Cautare disciplina cu ID: {}", id);
        return executeQuerySingle(
            "SELECT * FROM discipline WHERE id = ?",
            stmt -> setUUID(stmt, 1, id)
        ).orElse(null);
    }

    @Override
    protected Disciplina extractFromResultSet(ResultSet rs) throws SQLException {
        return new Disciplina(
            getUUID(rs, "id"),
            rs.getString("nume"),
            rs.getString("acronim"),
            rs.getString("tip_evaluare")
        );
    }
}



