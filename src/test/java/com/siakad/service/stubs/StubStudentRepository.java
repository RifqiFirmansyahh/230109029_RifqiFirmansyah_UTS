package com.siakad.service.stubs;

import com.siakad.model.Course;
import com.siakad.model.Student;
import com.siakad.repository.StudentRepository;
import java.util.List;

/**
 * Ini adalah STUB. Implementasi manual untuk testing (Soal B).
 * Dibuat di src/test/java dan TIDAK melanggar aturan.
 */
public class StubStudentRepository implements StudentRepository {

    @Override
    public Student findById(String studentId) {
        if ("s1_gpa_3.5".equals(studentId)) {
            // (studentId, name, email, major, semester, gpa, status)
            return new Student("s1_gpa_3.5", "Rifqi", "rifqi@email.com",
                    "SIB", 5, 3.5, "ACTIVE");
        }
        if ("s2_gpa_2.8".equals(studentId)) {
            return new Student("s2_gpa_2.8", "Budi", "budi@email.com",
                    "SIB", 3, 2.8, "ACTIVE");
        }
        return null; // Return null jika studentId lain
    }

    @Override
    public void update(Student student) {
        // Do nothing in stub
    }

    @Override
    public List<Course> getCompletedCourses(String studentId) {
        return List.of(); // Do nothing
    }
}