package com.siakad.service;

import com.siakad.exception.StudentNotFoundException;
import com.siakad.repository.CourseRepository;
import com.siakad.repository.StudentRepository;
import com.siakad.service.stubs.StubGradeCalculator;
import com.siakad.service.stubs.StubStudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ini adalah jawaban untuk Soal B: Unit Test menggunakan Stub.
 * Menguji metode validateCreditLimit sesuai petunjuk di kode main.
 */
class EnrollmentServiceStubTest {

    private EnrollmentService enrollmentService;
    private StudentRepository studentRepositoryStub;
    private GradeCalculator gradeCalculatorStub;

    // Dependensi lain yang tidak dipakai di-set null
    private CourseRepository courseRepository = null;
    private NotificationService notificationService = null;

    @BeforeEach
    void setUp() {
        // Inisiasi Stub secara manual
        studentRepositoryStub = new StubStudentRepository();
        gradeCalculatorStub = new StubGradeCalculator();

        // Suntikkan Stub ke service
        enrollmentService = new EnrollmentService(
                studentRepositoryStub,
                courseRepository,
                notificationService,
                gradeCalculatorStub
        );
    }

    @Test
    @DisplayName("validateCreditLimit (Stub): Success")
    void testValidateCreditLimit_Success() {
        // Stub akan mengembalikan Student("s1_gpa_3.5", gpa=3.5)
        // Stub GradeCalculator akan mengembalikan 24 SKS untuk gpa 3.5
        boolean result = enrollmentService.validateCreditLimit("s1_gpa_3.5", 20);
        assertTrue(result, "Harusnya TRUE karena 20 SKS <= 24 SKS");
    }

    @Test
    @DisplayName("validateCreditLimit (Stub): Failure")
    void testValidateCreditLimit_Failure() {
        // Stub akan mengembalikan Student("s2_gpa_2.8", gpa=2.8)
        // Stub GradeCalculator akan mengembalikan 21 SKS untuk gpa 2.8
        boolean result = enrollmentService.validateCreditLimit("s2_gpa_2.8", 24);
        assertFalse(result, "Harusnya FALSE karena 24 SKS > 21 SKS");
    }

    @Test
    @DisplayName("validateCreditLimit (Stub): Student Not Found")
    void testValidateCreditLimit_StudentNotFound() {
        // ID "unknown" akan mengembalikan null dari stub
        assertThrows(StudentNotFoundException.class, () -> {
            enrollmentService.validateCreditLimit("unknown", 10);
        });
    }
}