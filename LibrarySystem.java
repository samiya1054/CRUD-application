import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

class Student {
    private int studentId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String department;
    private LocalDate enrollmentDate;
    private double gpa;
    
    public Student() {
        this.enrollmentDate = LocalDate.now();
        this.gpa = 0.0;
    }
    
    public Student(int studentId, String firstName, String lastName, String email, 
                  String phoneNumber, String department) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.enrollmentDate = LocalDate.now();
        this.gpa = 0.0;
    }
    
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    
    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    @Override
    public String toString() {
        return String.format("Student{ID=%d, Name='%s %s', Email='%s', Phone='%s', Dept='%s', GPA=%.2f, Enrolled=%s}",
                studentId, firstName, lastName, email, phoneNumber, department, gpa, enrollmentDate);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return studentId == student.studentId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
}

class StudentRepository {
    private Map<Integer, Student> studentDatabase;
    private int nextId;
    
    public StudentRepository() {
        this.studentDatabase = new HashMap<>();
        this.nextId = 1;
        initializeSampleData();
    }
    
    private void initializeSampleData() {
        addStudent(new Student(generateId(), "Alice", "Johnson", "alice.j@email.com", "555-0101", "Computer Science"));
        addStudent(new Student(generateId(), "Bob", "Smith", "bob.smith@email.com", "555-0102", "Mathematics"));
        addStudent(new Student(generateId(), "Carol", "Williams", "carol.w@email.com", "555-0103", "Physics"));
    }
    
    private int generateId() {
        return nextId++;
    }
    
    public Student addStudent(Student student) {
        if (student.getStudentId() <= 0) {
            student.setStudentId(generateId());
        }
        studentDatabase.put(student.getStudentId(), student);
        return student;
    }
    
    public List<Student> getAllStudents() {
        return new ArrayList<>(studentDatabase.values());
    }
    
    public Optional<Student> getStudentById(int id) {
        return Optional.ofNullable(studentDatabase.get(id));
    }
    
    public List<Student> getStudentsByDepartment(String department) {
        return studentDatabase.values().stream()
                .filter(s -> s.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }
    
    public List<Student> searchStudentsByName(String nameQuery) {
        String query = nameQuery.toLowerCase();
        return studentDatabase.values().stream()
                .filter(s -> s.getFullName().toLowerCase().contains(query))
                .collect(Collectors.toList());
    }
    
    public boolean updateStudent(int id, Student updatedStudent) {
        if (studentDatabase.containsKey(id)) {
            updatedStudent.setStudentId(id);
            studentDatabase.put(id, updatedStudent);
            return true;
        }
        return false;
    }
    
    public boolean deleteStudent(int id) {
        return studentDatabase.remove(id) != null;
    }
    
    public int getTotalStudents() {
        return studentDatabase.size();
    }
    
    public boolean studentExists(int id) {
        return studentDatabase.containsKey(id);
    }
    
    public List<String> getAllDepartments() {
        return studentDatabase.values().stream()
                .map(Student::getDepartment)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}

class StudentService {
    private StudentRepository repository;
    
    public StudentService() {
        this.repository = new StudentRepository();
    }
    
    public Student createStudent(String firstName, String lastName, String email, 
                               String phone, String department) {
        validateStudentData(firstName, lastName, email, phone, department);
        
        Student student = new Student(0, firstName, lastName, email, phone, department);
        return repository.addStudent(student);
    }
    
    public List<Student> getAllStudents() {
        return repository.getAllStudents();
    }
    
    public Student getStudentById(int id) {
        Optional<Student> student = repository.getStudentById(id);
        return student.orElse(null);
    }
    
    public List<Student> findStudentsByDepartment(String department) {
        return repository.getStudentsByDepartment(department);
    }
    
    public List<Student> searchStudents(String searchTerm) {
        return repository.searchStudentsByName(searchTerm);
    }
    
    public boolean updateStudent(int id, String firstName, String lastName, 
                               String email, String phone, String department, double gpa) {
        if (!repository.studentExists(id)) {
            return false;
        }
        
        validateStudentData(firstName, lastName, email, phone, department);
        
        Student existingStudent = repository.getStudentById(id).orElse(null);
        if (existingStudent != null) {
            Student updatedStudent = new Student(id, firstName, lastName, email, phone, department);
            updatedStudent.setGpa(gpa);
            updatedStudent.setEnrollmentDate(existingStudent.getEnrollmentDate());
            return repository.updateStudent(id, updatedStudent);
        }
        return false;
    }
    
    public boolean updateStudentGpa(int id, double gpa) {
        Optional<Student> studentOpt = repository.getStudentById(id);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            student.setGpa(gpa);
            return repository.updateStudent(id, student);
        }
        return false;
    }
    
    public boolean deleteStudent(int id) {
        return repository.deleteStudent(id);
    }
    
    public int getTotalStudentCount() {
        return repository.getTotalStudents();
    }
    
    public List<String> getAvailableDepartments() {
        return repository.getAllDepartments();
    }
    
    public double getAverageGpa() {
        List<Student> students = repository.getAllStudents();
        return students.stream()
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0.0);
    }
    
    private void validateStudentData(String firstName, String lastName, String email, 
                                   String phone, String department) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Valid email is required");
        }
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty");
        }
    }
}

public class StudentManagementSystem {
    private StudentService studentService;
    private Scanner scanner;
    
    public StudentManagementSystem() {
        this.studentService = new StudentService();
        this.scanner = new Scanner(System.in);
    }
    
    public void startApplication() {
        System.out.println("=== Welcome to Student Management System ===");
        System.out.println("Developed by: [Your Name]");
        System.out.println("============================================");
        
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    createStudentFlow();
                    break;
                case 2:
                    viewAllStudents();
                    break;
                case 3:
                    viewStudentById();
                    break;
                case 4:
                    searchStudentsFlow();
                    break;
                case 5:
                    updateStudentFlow();
                    break;
                case 6:
                    deleteStudentFlow();
                    break;
                case 7:
                    showStatistics();
                    break;
                case 8:
                    filterByDepartment();
                    break;
                case 9:
                    updateGpaFlow();
                    break;
                case 0:
                    running = false;
                    System.out.println("Thank you for using Student Management System!");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }
    
    private void displayMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("            STUDENT MANAGEMENT MENU");
        System.out.println("=".repeat(50));
        System.out.println("1. Add New Student");
        System.out.println("2. View All Students");
        System.out.println("3. Find Student by ID");
        System.out.println("4. Search Students by Name");
        System.out.println("5. Update Student Information");
        System.out.println("6. Delete Student");
        System.out.println("7. View Statistics");
        System.out.println("8. Filter by Department");
        System.out.println("9. Update Student GPA");
        System.out.println("0. Exit Application");
        System.out.println("=".repeat(50));
    }
    
    private void createStudentFlow() {
        System.out.println("\n=== ADD NEW STUDENT ===");
        try {
            System.out.print("First Name: ");
            String firstName = scanner.nextLine();
            
            System.out.print("Last Name: ");
            String lastName = scanner.nextLine();
            
            System.out.print("Email: ");
            String email = scanner.nextLine();
            
            System.out.print("Phone Number: ");
            String phone = scanner.nextLine();
            
            System.out.print("Department: ");
            String department = scanner.nextLine();
            
            Student newStudent = studentService.createStudent(firstName, lastName, email, phone, department);
            System.out.println("\nStudent added successfully!");
            System.out.println("Assigned Student ID: " + newStudent.getStudentId());
            System.out.println(newStudent);
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void viewAllStudents() {
        System.out.println("\n=== ALL STUDENTS ===");
        List<Student> students = studentService.getAllStudents();
        
        if (students.isEmpty()) {
            System.out.println("No students found in the system.");
            return;
        }
        
        System.out.printf("%-5s %-15s %-15s %-25s %-15s %-15s %-8s%n",
                "ID", "First Name", "Last Name", "Email", "Phone", "Department", "GPA");
        System.out.println("-".repeat(100));
        
        for (Student student : students) {
            System.out.printf("%-5d %-15s %-15s %-25s %-15s %-15s %-8.2f%n",
                    student.getStudentId(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getEmail(),
                    student.getPhoneNumber(),
                    student.getDepartment(),
                    student.getGpa());
        }
        System.out.println("\nTotal Students: " + students.size());
    }
    
    private void viewStudentById() {
        System.out.println("\n=== FIND STUDENT BY ID ===");
        int id = getIntInput("Enter Student ID: ");
        
        Student student = studentService.getStudentById(id);
        if (student != null) {
            System.out.println("\nStudent Details:");
            displayStudentDetails(student);
        } else {
            System.out.println("No student found with ID: " + id);
        }
    }
    
    private void searchStudentsFlow() {
        System.out.println("\n=== SEARCH STUDENTS ===");
        System.out.print("Enter name to search: ");
        String searchTerm = scanner.nextLine();
        
        List<Student> results = studentService.searchStudents(searchTerm);
        if (results.isEmpty()) {
            System.out.println("No students found matching: " + searchTerm);
        } else {
            System.out.println("\nSearch Results (" + results.size() + " found):");
            for (Student student : results) {
                System.out.println(student);
            }
        }
    }
    
    private void updateStudentFlow() {
        System.out.println("\n=== UPDATE STUDENT ===");
        int id = getIntInput("Enter Student ID to update: ");
        
        Student existingStudent = studentService.getStudentById(id);
        if (existingStudent == null) {
            System.out.println("No student found with ID: " + id);
            return;
        }
        
        System.out.println("\nCurrent Information:");
        displayStudentDetails(existingStudent);
        
        System.out.println("\nEnter new information (press Enter to keep current value):");
        
        System.out.print("First Name [" + existingStudent.getFirstName() + "]: ");
        String firstName = getStringOrDefault(existingStudent.getFirstName());
        
        System.out.print("Last Name [" + existingStudent.getLastName() + "]: ");
        String lastName = getStringOrDefault(existingStudent.getLastName());
        
        System.out.print("Email [" + existingStudent.getEmail() + "]: ");
        String email = getStringOrDefault(existingStudent.getEmail());
        
        System.out.print("Phone [" + existingStudent.getPhoneNumber() + "]: ");
        String phone = getStringOrDefault(existingStudent.getPhoneNumber());
        
        System.out.print("Department [" + existingStudent.getDepartment() + "]: ");
        String department = getStringOrDefault(existingStudent.getDepartment());
        
        System.out.print("GPA [" + existingStudent.getGpa() + "]: ");
        double gpa = getDoubleOrDefault(existingStudent.getGpa());
        
        try {
            boolean updated = studentService.updateStudent(id, firstName, lastName, email, phone, department, gpa);
            if (updated) {
                System.out.println("Student updated successfully!");
                displayStudentDetails(studentService.getStudentById(id));
            } else {
                System.out.println("Failed to update student.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void deleteStudentFlow() {
        System.out.println("\n=== DELETE STUDENT ===");
        int id = getIntInput("Enter Student ID to delete: ");
        
        Student student = studentService.getStudentById(id);
        if (student == null) {
            System.out.println("No student found with ID: " + id);
            return;
        }
        
        System.out.println("\nStudent to be deleted:");
        displayStudentDetails(student);
        
        System.out.print("\nAre you sure you want to delete this student? (yes/no): ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("yes") || confirmation.equalsIgnoreCase("y")) {
            boolean deleted = studentService.deleteStudent(id);
            if (deleted) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Failed to delete student.");
            }
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }
    
    private void showStatistics() {
        System.out.println("\n=== SYSTEM STATISTICS ===");
        int totalStudents = studentService.getTotalStudentCount();
        double averageGpa = studentService.getAverageGpa();
        List<String> departments = studentService.getAvailableDepartments();
        
        System.out.println("Total Students: " + totalStudents);
        System.out.printf("Average GPA: %.2f%n", averageGpa);
        System.out.println("Departments: " + departments.size());
        
        System.out.println("\nDepartment List:");
        for (String dept : departments) {
            int count = studentService.findStudentsByDepartment(dept).size();
            System.out.printf("  %s: %d students%n", dept, count);
        }
    }
    
    private void filterByDepartment() {
        System.out.println("\n=== FILTER BY DEPARTMENT ===");
        List<String> departments = studentService.getAvailableDepartments();
        
        System.out.println("Available Departments:");
        for (int i = 0; i < departments.size(); i++) {
            System.out.println((i + 1) + ". " + departments.get(i));
        }
        
        System.out.print("\nEnter department name: ");
        String department = scanner.nextLine();
        
        List<Student> students = studentService.findStudentsByDepartment(department);
        if (students.isEmpty()) {
            System.out.println("No students found in department: " + department);
        } else {
            System.out.println("\nStudents in " + department + " Department:");
            for (Student student : students) {
                System.out.println(student);
            }
        }
    }
    
    private void updateGpaFlow() {
        System.out.println("\n=== UPDATE STUDENT GPA ===");
        int id = getIntInput("Enter Student ID: ");
        
        Student student = studentService.getStudentById(id);
        if (student == null) {
            System.out.println("No student found with ID: " + id);
            return;
        }
        
        System.out.println("\nStudent: " + student.getFullName());
        System.out.println("Current GPA: " + student.getGpa());
        
        double newGpa = getDoubleInput("Enter new GPA (0.0 - 4.0): ");
        
        if (newGpa < 0.0 || newGpa > 4.0) {
            System.out.println("Invalid GPA. Must be between 0.0 and 4.0");
            return;
        }
        
        boolean updated = studentService.updateStudentGpa(id, newGpa);
        if (updated) {
            System.out.println("GPA updated successfully!");
            System.out.printf("New GPA: %.2f%n", newGpa);
        } else {
            System.out.println("Failed to update GPA.");
        }
    }
    
    private void displayStudentDetails(Student student) {
        System.out.println("  ID: " + student.getStudentId());
        System.out.println("  Name: " + student.getFullName());
        System.out.println("  Email: " + student.getEmail());
        System.out.println("  Phone: " + student.getPhoneNumber());
        System.out.println("  Department: " + student.getDepartment());
        System.out.printf("  GPA: %.2f%n", student.getGpa());
        System.out.println("  Enrollment Date: " + student.getEnrollmentDate());
    }
    
    private int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private double getDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private String getStringOrDefault(String defaultValue) {
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? defaultValue : input;
    }
    
    private double getDoubleOrDefault(double defaultValue) {
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    public static void main(String[] args) {
        StudentManagementSystem app = new StudentManagementSystem();
        app.startApplication();
    }
}
