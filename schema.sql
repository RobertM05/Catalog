CREATE TABLE IF NOT EXISTS notarii (
    id VARCHAR(36) PRIMARY KEY,
    student_id VARCHAR(36) NOT NULL,
    disciplina_id VARCHAR(36) NOT NULL,
    nota INT NOT NULL,
    data_notare TIMESTAMP NOT NULL,
    FOREIGN KEY (student_id) REFERENCES studenti(id),
    FOREIGN KEY (disciplina_id) REFERENCES discipline(id),
    CHECK (nota >= 1 AND nota <= 10)
); 