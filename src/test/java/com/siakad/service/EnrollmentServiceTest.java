package com.siakad.service;

import com.siakad.exception.*;
import com.siakad.model.Course;
import com.siakad.model.Student;
import com.siakad.repository.CourseRepository;
import com.siakad.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Ini adalah jawaban untuk Soal C: Unit Test menggunakan Mock.
 * Menguji class EnrollmentService secara penuh dengan Mockito.
 */
@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    // @Mock membuat objek tiruan (Mock)
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private GradeCalculator gradeCalculator;

    // @InjectMocks menyuntikkan semua @Mock di atas ke service
    @InjectMocks
    private EnrollmentService enrollmentService;

    private Student student;
    private Course course;

    @BeforeEach
    void setUp() {
        // Data Uji Umum
        student = new Student("s1", "Rifqi", "rifqi@email.com", "SIB",
                5, 3.5, "ACTIVE");
        course = new Course("c1", "Pemrograman Lanjut", 3, 40,
                10, "Pak Budi");
    }

    // --- Tes untuk enrollCourse ---

    @Test
    @DisplayName("enrollCourse: Success")
    void testEnrollCourse_Success() {
        // 1. Arrange (Atur perilaku Mock)
        // Repository mengembalikan data (bukan null)
        when(studentRepository.findById("s1")).thenReturn(student);
        when(courseRepository.findByCourseCode("c1")).thenReturn(course);
        // Prasyarat terpenuhi
        when(courseRepository.isPrerequisiteMet("s1", "c1")).thenReturn(true);
        // Status ACTIVE dan kapasitas OK (10 < 40)

        // 2. Act (Panggil metode)
        var enrollment = enrollmentService.enrollCourse("s1", "c1");

        // 3. Assert / Verify
        assertNotNull(enrollment);
        assertEquals("APPROVED", enrollment.getStatus());
        assertEquals("s1", enrollment.getStudentId());

        // Verifikasi bahwa count bertambah
        assertEquals(11, course.getEnrolledCount());
        // Verifikasi bahwa repository.update dipanggil
        verify(courseRepository).update(course);

        // Verifikasi bahwa email terkirim
        verify(notificationService).sendEmail(
                eq("rifqi@email.com"),
                eq("Enrollment Confirmation"),
                anyString()
        );
        // Pastikan SMS tidak terkirim
        verify(notificationService, never()).sendSMS(any(), any());
    }

    @Test
    @DisplayName("enrollCourse: Fail Student Not Found")
    void testEnrollCourse_Fail_StudentNotFound() {
        // Repository mengembalikan null
        when(studentRepository.findById("s1")).thenReturn(null);

        assertThrows(StudentNotFoundException.class, () -> {
            enrollmentService.enrollCourse("s1", "c1");
        });
    }

    @Test
    @DisplayName("enrollCourse: Fail Student Suspended")
    void testEnrollCourse_Fail_StudentSuspended() {
        student.setAcademicStatus("SUSPENDED"); //
        when(studentRepository.findById("s1")).thenReturn(student);

        assertThrows(EnrollmentException.class, () -> {
            enrollmentService.enrollCourse("s1", "c1");
        }, "Mahasiswa SUSPENDED tidak boleh mendaftar");
    }

    @Test
    @DisplayName("enrollCourse: Fail Course Not Found")
    void testEnrollCourse_Fail_CourseNotFound() {
        when(studentRepository.findById("s1")).thenReturn(student);
        // Repository mengembalikan null
        when(courseRepository.findByCourseCode("c1")).thenReturn(null);

        assertThrows(CourseNotFoundException.class, () -> {
            enrollmentService.enrollCourse("s1", "c1");
        });
    }

    @Test
    @DisplayName("enrollCourse: Fail Course Full")
    void testEnrollCourse_Fail_CourseFull() {
        course.setEnrolledCount(40); // Kelas penuh (40 >= 40)
        course.setCapacity(40);

        when(studentRepository.findById("s1")).thenReturn(student);
        when(courseRepository.findByCourseCode("c1")).thenReturn(course);

        assertThrows(CourseFullException.class, () -> {
            enrollmentService.enrollCourse("s1", "c1");
        }, "Kapasitas kelas tidak boleh melebihi");
    }

    @Test
    @DisplayName("enrollCourse: Fail Prerequisite Not Met")
    void testEnrollCourse_Fail_PrerequisiteNotMet() {
        when(studentRepository.findById("s1")).thenReturn(student);
        when(courseRepository.findByCourseCode("c1")).thenReturn(course);
        // Repository bilang prasyarat tidak terpenuhi
        when(courseRepository.isPrerequisiteMet("s1", "c1")).thenReturn(false);

        assertThrows(PrerequisiteNotMetException.class, () -> {
            enrollmentService.enrollCourse("s1", "c1");
        });
    }

    // --- Tes untuk validateCreditLimit ---

    @Test
    @DisplayName("validateCreditLimit (Mock): Success (Limit OK)")
    void testValidateCreditLimit_Success() {
        student.setGpa(3.5); // IPK 3.5 -> 24 SKS
        when(studentRepository.findById("s1")).thenReturn(student);
        when(gradeCalculator.calculateMaxCredits(3.5)).thenReturn(24);

        boolean result = enrollmentService.validateCreditLimit("s1", 20); // Minta 20
        assertTrue(result);
    }

    @Test
    @DisplayName("validateCreditLimit (Mock): Fail (Exceeds Limit)")
    void testValidateCreditLimit_Failure() {
        student.setGpa(2.8); // IPK 2.8 -> 21 SKS
        when(studentRepository.findById("s1")).thenReturn(student);
        when(gradeCalculator.calculateMaxCredits(2.8)).thenReturn(21);

        boolean result = enrollmentService.validateCreditLimit("s1", 24); // Minta 24
        assertFalse(result);
    }

    @Test
    @DisplayName("validateCreditLimit (Mock): Fail Student Not Found")
    void testValidateCreditLimit_StudentNotFound() {
        when(studentRepository.findById("s1")).thenReturn(null);

        assertThrows(StudentNotFoundException.class, () -> {
            enrollmentService.validateCreditLimit("s1", 10);
        });
    }

    // --- Tes untuk dropCourse ---

    @Test
    @DisplayName("dropCourse: Success")
    void testDropCourse_Success() {
        course.setEnrolledCount(11); // Awalnya ada 11
        when(studentRepository.findById("s1")).thenReturn(student);
        when(courseRepository.findByCourseCode("c1")).thenReturn(course);

        enrollmentService.dropCourse("s1", "c1");

        // Verifikasi count berkurang
        assertEquals(10, course.getEnrolledCount());
        // Verifikasi repository.update dipanggil
        verify(courseRepository).update(course);
        // Verifikasi email terkirim
        verify(notificationService).sendEmail(
                eq("rifqi@email.com"),
                eq("Course Drop Confirmation"),
                anyString()
        );
    }

    @Test
    @DisplayName("dropCourse: Fail Student Not Found")
    void testDropCourse_StudentNotFound() {
        when(studentRepository.findById("s1")).thenReturn(null);

        assertThrows(StudentNotFoundException.class, () -> {
            enrollmentService.dropCourse("s1", "c1");
        });
    }

    @Test
    @DisplayName("dropCourse: Fail Course Not Found")
    void testDropCourse_CourseNotFound() {
        when(studentRepository.findById("s1")).thenReturn(student);
        when(courseRepository.findByCourseCode("c1")).thenReturn(null);

        assertThrows(CourseNotFoundException.class, () -> {
            enrollmentService.dropCourse("s1", "c1");
        });
    }
}