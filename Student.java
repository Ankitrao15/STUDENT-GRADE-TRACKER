import java.io.Serializable;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    private String name;
    private int[] marks;

    public Student(int id, String name, int[] marks) {
        this.id = id;
        this.name = name;
        this.marks = marks.clone();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int[] getMarks() { return marks.clone(); }

    public void setName(String name) { this.name = name; }
    public void setMarks(int[] marks) { this.marks = marks.clone(); }

    public int getTotal() {
        int total = 0;
        for (int mark : marks) total += mark;
        return total;
    }

    public double getPercentage() {
        return marks.length == 0 ? 0 : (double) getTotal() / marks.length;
    }

    public String getGrade() {
        return GradeCalculator.calculate(getPercentage());
    }
}
