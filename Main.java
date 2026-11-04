import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static final String FILE_NAME = "students.dat";
    private static final String[] SUBJECTS = {
        "Java", "Python", "DBMS", "Operating System", "Computer Networks"
    };

    private static final List<Student> students = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadData();
        System.out.println("Welcome to Student Grade Tracker!");

        while (true) {
            printMenu();
            int choice = readInt("Enter your choice: ", 1, 7);

            switch (choice) {
                case 1: addStudent(); break;
                case 2: viewAllStudents(); break;
                case 3: searchStudent(); break;
                case 4: updateStudent(); break;
                case 5: deleteStudent(); break;
                case 6: showClassSummary(); break;
                case 7:
                    saveData();
                    System.out.println("Data saved. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\\n==================================");
        System.out.println("       STUDENT GRADE TRACKER");
        System.out.println("==================================");
        System.out.println("1. Add student");
        System.out.println("2. View all students");
        System.out.println("3. Search student by ID");
        System.out.println("4. Update student");
        System.out.println("5. Delete student");
        System.out.println("6. Class performance summary");
        System.out.println("7. Save and exit");
        System.out.println("==================================");
    }

    private static void addStudent() {
        int id = readInt("Student ID: ", 1, Integer.MAX_VALUE);
        if (findStudent(id) != null) {
            System.out.println("That ID already exists.");
            return;
        }

        String name = readNonEmpty("Student name: ");
        int[] marks = readMarks();
        students.add(new Student(id, name, marks));
        saveData();
        System.out.println("Student added successfully.");
    }

    private static int[] readMarks() {
        int[] marks = new int[SUBJECTS.length];
        System.out.println("Enter marks from 0 to 100:");
        for (int i = 0; i < SUBJECTS.length; i++) {
            marks[i] = readInt(SUBJECTS[i] + ": ", 0, 100);
        }
        return marks;
    }

    private static void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }
        System.out.println("\\n========== ALL STUDENTS ==========");
        for (Student student : students) displayStudent(student);
    }

    private static void searchStudent() {
        int id = readInt("Enter student ID: ", 1, Integer.MAX_VALUE);
        Student student = findStudent(id);
        if (student == null) System.out.println("Student not found.");
        else displayStudent(student);
    }

    private static void updateStudent() {
        int id = readInt("Enter student ID to update: ", 1, Integer.MAX_VALUE);
        Student student = findStudent(id);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("1. Update name");
        System.out.println("2. Update marks");
        System.out.println("3. Update name and marks");
        int choice = readInt("Choose: ", 1, 3);

        if (choice == 1 || choice == 3) {
            student.setName(readNonEmpty("New name: "));
        }
        if (choice == 2 || choice == 3) {
            student.setMarks(readMarks());
        }
        saveData();
        System.out.println("Student updated successfully.");
    }

    private static void deleteStudent() {
        int id = readInt("Enter student ID to delete: ", 1, Integer.MAX_VALUE);
        Student student = findStudent(id);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        String answer = readNonEmpty("Delete " + student.getName() + "? (yes/no): ");
        if (answer.equalsIgnoreCase("yes")) {
            students.remove(student);
            saveData();
            System.out.println("Student deleted.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static void showClassSummary() {
        if (students.isEmpty()) {
            System.out.println("No records available for summary.");
            return;
        }

        double totalPercentage = 0;
        Student topper = students.get(0);
        int passCount = 0;

        for (Student student : students) {
            double percentage = student.getPercentage();
            totalPercentage += percentage;
            if (percentage > topper.getPercentage()) topper = student;
            if (percentage >= 40) passCount++;
        }

        System.out.println("\\n========== CLASS SUMMARY ==========");
        System.out.println("Total students: " + students.size());
        System.out.printf(Locale.US, "Class average: %.2f%%%n",
                totalPercentage / students.size());
        System.out.println("Students passed: " + passCount);
        System.out.println("Students failed: " + (students.size() - passCount));
        System.out.println("Topper: " + topper.getName() + " ("
                + String.format(Locale.US, "%.2f", topper.getPercentage()) + "%)");
    }

    private static Student findStudent(int id) {
        for (Student student : students) {
            if (student.getId() == id) return student;
        }
        return null;
    }

    private static void displayStudent(Student student) {
        System.out.println("\\n----------------------------------");
        System.out.println("ID: " + student.getId());
        System.out.println("Name: " + student.getName());
        int[] marks = student.getMarks();
        for (int i = 0; i < SUBJECTS.length; i++) {
            System.out.println(SUBJECTS[i] + ": " + marks[i] + "/100");
        }
        System.out.println("Total: " + student.getTotal() + "/"
                + (SUBJECTS.length * 100));
        System.out.printf(Locale.US, "Percentage: %.2f%%%n",
                student.getPercentage());
        System.out.println("Grade: " + student.getGrade());
        System.out.println("----------------------------------");
    }

    private static void saveData() {
        try (ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream(FILE_NAME))) {
            output.writeObject(new ArrayList<>(students));
        } catch (IOException e) {
            System.out.println("Could not save data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (ObjectInputStream input = new ObjectInputStream(
                new FileInputStream(file))) {
            Object saved = input.readObject();
            if (saved instanceof ArrayList<?>) {
                for (Object item : (ArrayList<?>) saved) {
                    if (item instanceof Student) students.add((Student) item);
                }
            }
            System.out.println(students.size() + " saved student record(s) loaded.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Could not load saved data: " + e.getMessage());
        }
    }

    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            try {
                int number = Integer.parseInt(value);
                if (number >= min && number <= max) return number;
                System.out.println("Enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    private static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) return value;
            System.out.println("This field cannot be empty.");
        }
    }
}
