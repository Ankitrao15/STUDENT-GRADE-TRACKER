#Student Grade Tracker (Core Java)

A console-based student record management project built with Core Java and OOP.

## Features
- Add students with unique IDs
- Enter marks for five subjects (0-100)
- Calculate total, percentage, and grade automatically
- View all student records
- Search by student ID
- Update student name and/or marks
- Delete student records with confirmation
- View class average, pass/fail count, and topper
- Save and load records using Java object serialization

## Requirements
- JDK 8 or newer
- VS Code, IntelliJ IDEA, Eclipse, or a terminal

## Project files
- `Main.java` - menu and application logic
- `Student.java` - student model and calculations
- `GradeCalculator.java` - grade rules
- `students.dat` - generated automatically when records are saved

## Run from terminal
Open a terminal in this folder and run:

```bash
javac Main.java Student.java GradeCalculator.java
java Main
```

On Windows PowerShell, the same commands work if the JDK `bin` folder is on PATH.

## Grade scale
| Percentage | Grade |
|---|---|
| 90-100 | A+ |
| 80-89.99 | A |
| 70-79.99 | B |
| 60-69.99 | C |
| 50-59.99 | D |
| 40-49.99 | E |
| Below 40 | F |

The percentage is calculated as total marks divided by the number of subjects because each subject is out of 100.

## Notes
- Student records are stored in `students.dat` in the current working directory.
- Keep `students.dat` if you want to retain your records.
- This is an educational console project; serialization files should only be loaded from trusted sources.
