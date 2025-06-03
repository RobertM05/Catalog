# Catalog
Catalog Online
# Catalog Universitar

## Descriere

Acest proiect implementează un sistem simplu de catalog universitar cu interfață grafică (GUI) bazat pe Swing. Aplicația permite gestionarea studenților, disciplinelor și notelor.

## Arhitectură

Proiectul urmează o arhitectură stratificată (similară cu MVC) cu următoarele layere principale:

- **Model (`org.example.model`)**: Definește structura datelor (Studenți, Discipline, Note, Utilizatori).
- **DAO (`org.example.dao`)**: Gestionează operațiile de acces la baza de date (CRUD).
- **UI (`org.example.ui`)**: Implementează interfața grafică Swing și interacțiunea cu utilizatorul.
- **Utilități (`org.example.util`)**: Conține clase helper pentru conexiunea la baza de date, managementul tranzacțiilor etc.

## Tehnologii Folosite

- Java Swing
- JDBC pentru acces la baza de date
- MySQL Database
- HikariCP pentru connection pooling
- SLF4J pentru logging
- BCrypt pentru hashing parole

## Configurarea Bazei de Date

1. Asigură-te că ai instalat un server MySQL (de exemplu, folosind XAMPP sau MySQL Community Server).
2. Creează o bază de date numită `catalog`.
3. Importă schema bazei de date. (De obicei, dintr-un fișier `schema.sql`. Dacă ai șters fișierul, va trebui să recreezi manual tabelele `utilizatori`, `studenti`, `discipline`, `note` cu structura corespunzătoare, inclusiv coloanele și cheile primare/externe).

## Rularea Aplicației

1. Clonează repository-ul (dacă este cazul) sau descarcă codul sursă.
2. Deschide proiectul într-un IDE Java (precum IntelliJ IDEA, Eclipse, NetBeans).
3. Configurează conexiunea la baza de date:
   - În setările de rulare/configurare ale aplicației din IDE, adaugă următoarele argumente VM (Virtual Machine arguments):
     ``-Ddb.url=jdbc:mysql://localhost:3306/catalog``
     ``-Ddb.username=root``
     ``-Ddb.password=`(parola_ta_mysql)``
   - (Asigură-te că hostname-ul `localhost` și portul `3306` sunt corecte pentru instalarea ta MySQL)
4. Rulează clasa `org.example.Main`.

## Funcționalități Principale

- Autentificare utilizatori
- Vizualizare, adăugare, editare, ștergere studenți
- Vizualizare, adăugare, editare, ștergere discipline
- Adăugare note pentru studenți la discipline
- Vizualizare note și medii pentru studenți 
