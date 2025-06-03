package org.example.dao;

import org.example.model.Disciplina;
import org.example.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DisciplinaDAO {

    public void adaugaDisciplina(Disciplina disciplina) throws SQLException {
        String sql = "INSERT INTO discipline (id, nume, acronim, tip_evaluare) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, disciplina.getId().toString());
            stmt.setString(2, disciplina.getNume());
            stmt.setString(3, disciplina.getAcronim());
            stmt.setString(4, disciplina.getTipEvaluare());
            stmt.executeUpdate();
        }
    }

    public List<Disciplina> toateDisciplinele() throws SQLException {
        List<Disciplina> discipline = new ArrayList<>();
        String sql = "SELECT * FROM discipline";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UUID id = UUID.fromString(rs.getString("id"));
                String nume = rs.getString("nume");
                String acronim = rs.getString("acronim");
                String tipEvaluare = rs.getString("tip_evaluare");

                Disciplina d = new Disciplina(id, nume, acronim, tipEvaluare);
                discipline.add(d);
            }
        }
        return discipline;
    }

    public void actualizeazaDisciplina(Disciplina disciplina) throws SQLException {
        String sql = "UPDATE discipline SET nume = ?, acronim = ?, tip_evaluare = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, disciplina.getNume());
            stmt.setString(2, disciplina.getAcronim());
            stmt.setString(3, disciplina.getTipEvaluare());
            stmt.setString(4, disciplina.getId().toString());
            stmt.executeUpdate();
        }
    }

    public void stergeDisciplina(UUID id) throws SQLException {
        String sql = "DELETE FROM discipline WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());
            stmt.executeUpdate();
        }
    }

    public Disciplina getDisciplinaDupaId(UUID id) throws SQLException {
        String sql = "SELECT * FROM discipline WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nume = rs.getString("nume");
                    String acronim = rs.getString("acronim");
                    String tipEvaluare = rs.getString("tip_evaluare");
                    return new Disciplina(id, nume, acronim, tipEvaluare);
                }
            }
        }
        return null;
    }
}
