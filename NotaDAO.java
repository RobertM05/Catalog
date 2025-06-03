package org.example.dao;

import org.example.model.Nota;
import org.example.util.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotaDAO {
    private static final Logger logger = LoggerFactory.getLogger(NotaDAO.class);
    private static final int BATCH_SIZE = 100;

    public void adaugaNota(Nota nota) throws SQLException {
        logger.debug("Adaugare nota pentru student: {}, disciplina: {}", nota.getStudentId(), nota.getDisciplinaId());
        String sql = "INSERT INTO note (id, student_id, disciplina_id, nota, data_notare) VALUES (?,?,?,?,?)";
        
        TransactionManager.executeInTransaction(connection -> {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                setNotaParameters(stmt, nota);
                stmt.executeUpdate();
                logger.info("Nota adaugata cu succes: {}", nota.getId());
                return null;
            }
        });
    }

    public void adaugaNoteBatch(List<Nota> note) throws SQLException {
        logger.debug("Adaugare batch de {} note", note.size());
        String sql = "INSERT INTO note (id, student_id, disciplina_id, nota, data_notare) VALUES (?,?,?,?,?)";

        TransactionManager.executeInTransaction(connection -> {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                int count = 0;
                for (Nota nota : note) {
                    setNotaParameters(stmt, nota);
                    stmt.addBatch();
                    count++;

                    if (count % BATCH_SIZE == 0 || count == note.size()) {
                        stmt.executeBatch();
                        logger.debug("Executat batch de {} note", count % BATCH_SIZE == 0 ? BATCH_SIZE : count % BATCH_SIZE);
                    }
                }
                logger.info("Adaugate cu succes {} note in batch", note.size());
                return null;
            }
        });
    }

    public List<Nota> getListaNote() throws SQLException {
        logger.debug("Obtinere lista completa de note");
        String sql = "SELECT * FROM note";
        
        return TransactionManager.executeInTransaction(connection -> {
            List<Nota> note = new ArrayList<>();
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    note.add(extractNotaFromResultSet(rs));
                }
                logger.info("Obtinute {} note din baza de date", note.size());
                return note;
            }
        });
    }

    public void stergeNota(UUID id) throws SQLException {
        logger.debug("Stergere nota cu ID: {}", id);
        String sql = "DELETE FROM note WHERE id = ?";

        TransactionManager.executeInTransaction(connection -> {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, id.toString());
                int rowsAffected = stmt.executeUpdate();
                logger.info("Nota cu ID {} a fost stearsa. Randuri afectate: {}", id, rowsAffected);
                return null;
            }
        });
    }

    public void actualizeazaNota(Nota nota) throws SQLException {
        logger.debug("Actualizare nota cu ID: {}", nota.getId());
        String sql = "UPDATE note SET student_id = ?, disciplina_id = ?, nota = ?, data_notare = ? WHERE id = ?";

        TransactionManager.executeInTransaction(connection -> {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, nota.getStudentId().toString());
                stmt.setString(2, nota.getDisciplinaId().toString());
                stmt.setInt(3, nota.getNota());
                stmt.setTimestamp(4, Timestamp.valueOf(nota.getDataNotarii()));
                stmt.setString(5, nota.getId().toString());

                int rowsAffected = stmt.executeUpdate();
                logger.info("Nota actualizata cu succes. Randuri afectate: {}", rowsAffected);
                return null;
            }
        });
    }

    private void setNotaParameters(PreparedStatement stmt, Nota nota) throws SQLException {
        stmt.setString(1, nota.getId().toString());
        stmt.setString(2, nota.getStudentId().toString());
        stmt.setString(3, nota.getDisciplinaId().toString());
        stmt.setInt(4, nota.getNota());
        stmt.setTimestamp(5, Timestamp.valueOf(nota.getDataNotarii()));
    }

    private Nota extractNotaFromResultSet(ResultSet rs) throws SQLException {
        return new Nota(
            UUID.fromString(rs.getString("id")),
            UUID.fromString(rs.getString("student_id")),
            UUID.fromString(rs.getString("disciplina_id")),
            rs.getInt("nota"),
            rs.getTimestamp("data_notare").toLocalDateTime()
        );
    }
}