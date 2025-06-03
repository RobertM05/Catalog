package org.example.util;

import org.example.dao.DisciplinaDAO;
import org.example.dao.NotaDAO;
import org.example.dao.StudentDAO;
import org.example.model.Disciplina;
import org.example.model.Nota;
import org.example.model.Student;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class CatalogExporter {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private static final String CSV_SEPARATOR = ",";
    private static final double NOTA_PROMOVARE = 5.0;

    private final StudentDAO studentDAO;
    private final NotaDAO notaDAO;
    private final DisciplinaDAO disciplinaDAO;

    public CatalogExporter() {
        this.studentDAO = new StudentDAO();
        this.notaDAO = new NotaDAO();
        this.disciplinaDAO = new DisciplinaDAO();
    }

    public void exportToCSV(String filePath) throws SQLException, IOException {
        List<Student> studenti = studentDAO.selectAllStudents();
        List<Disciplina> discipline = disciplinaDAO.getAllDiscipline();

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("Nume Student,Prenume Student,Grupa,Email");
            for (Disciplina disciplina : discipline) {
                writer.append(CSV_SEPARATOR).append("Media ").append(disciplina.getNume());
                writer.append(CSV_SEPARATOR).append("Status ").append(disciplina.getNume());
            }
            writer.append(CSV_SEPARATOR).append("Media Generala");
            writer.append(CSV_SEPARATOR).append("Status General");
            writer.append("\n");

            for (Student student : studenti) {
                writer.append(escapeSpecialCharacters(student.getNume())).append(CSV_SEPARATOR);
                writer.append(escapeSpecialCharacters(student.getPrenume())).append(CSV_SEPARATOR);
                writer.append(escapeSpecialCharacters(student.getGrupa())).append(CSV_SEPARATOR);
                writer.append(escapeSpecialCharacters(student.getEmail())).append(CSV_SEPARATOR);

                List<Nota> noteStudent = notaDAO.getNoteStudent(student.getId());
                Map<UUID, List<Nota>> notePerDisciplina = noteStudent.stream()
                        .collect(Collectors.groupingBy(Nota::getDisciplinaId));

                double sumaMediaTotala = 0.0;
                int numarDisciplineCuNote = 0;

                for (Disciplina disciplina : discipline) {
                    List<Nota> noteDisciplina = notePerDisciplina.getOrDefault(disciplina.getId(), new ArrayList<>());
                    
                    if (!noteDisciplina.isEmpty()) {
                        double media = noteDisciplina.stream()
                                .mapToInt(Nota::getNota)
                                .average()
                                .orElse(0.0);
                        
                        String status = media >= NOTA_PROMOVARE ? "PROMOVAT" : "NEPROMOVAT";
                        
                        writer.append(String.format("%.2f", media)).append(CSV_SEPARATOR);
                        writer.append(status).append(CSV_SEPARATOR);
                        
                        sumaMediaTotala += media;
                        numarDisciplineCuNote++;
                    } else {
                        writer.append("N/A").append(CSV_SEPARATOR);
                        writer.append("N/A").append(CSV_SEPARATOR);
                    }
                }

                if (numarDisciplineCuNote > 0) {
                    double mediaGenerala = sumaMediaTotala / numarDisciplineCuNote;
                    String statusGeneral = mediaGenerala >= NOTA_PROMOVARE ? "PROMOVAT" : "NEPROMOVAT";
                    
                    writer.append(String.format("%.2f", mediaGenerala)).append(CSV_SEPARATOR);
                    writer.append(statusGeneral);
                } else {
                    writer.append("N/A").append(CSV_SEPARATOR);
                    writer.append("N/A");
                }
                
                writer.append("\n");
            }
        }
    }

    private String escapeSpecialCharacters(String text) {
        if (text == null) {
            return "";
        }
        String escaped = text.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
} 
