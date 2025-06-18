import java.util.*;

public class AttendanceManagementSystem {

    static class Student {
        String name;
        int roll;
        boolean isPresent;

        Student(String name, int roll) {
            this.name = name;
            this.roll = roll;
            this.isPresent = false;
        }
    }

    static ArrayList<Student> students = new ArrayList<>();

    public static void addStudent(String name, int roll) {
        students.add(new Student(name, roll));
        System.out.println("Student added: " + name);
    }

    public static void markAttendance(int roll) {
        for (Student s : students) {
            if (s.roll == roll) {
                s.isPresent = true;
                System.out.println("Marked present: " + s.name);
                return;
            }
        }
        System.out.println("Roll not found.");
    }

    public static void displayAttendance() {
        System.out.println("\n--- Attendance Report ---");
        for (Student s : students) {
            System.out.println("Roll: " + s.roll + ", Name: " + s.name + ", Status: " + (s.isPresent ? "Present" : "Absent"));
        }
    }

    public static void main(String[] args) {
        addStudent("Alice", 1);
        addStudent("Bob", 2);
        addStudent("Charlie", 3);

        markAttendance(1);
        markAttendance(3);

        displayAttendance();
    }
}
