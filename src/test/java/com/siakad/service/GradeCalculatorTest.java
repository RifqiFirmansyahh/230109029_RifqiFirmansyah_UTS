package com.siakad.service;

import com.siakad.model.CourseGrade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ini adalah jawaban untuk Soal A: Unit Test.
 * Menguji class GradeCalculator tanpa dependensi eksternal.
 */
class GradeCalculatorTest {

    private GradeCalculator gradeCalculator;

    @BeforeEach
    void setUp() {
        gradeCalculator = new GradeCalculator();
    }

    // --- Tes untuk calculateGPA ---

    @Test
    @DisplayName("Should Calculate GPA Correctly")
    void testCalculateGPA() {
        // (4.0 * 3) + (3.0 * 2) + (2.0 * 3) = 12 + 6 + 6 = 24
        // Total SKS = 3 + 2 + 3 = 8
        // IPK = 24 / 8 = 3.0
        List<CourseGrade> grades = List.of(
                new CourseGrade("MK001", 3, 4.0), // A
                new CourseGrade("MK002", 2, 3.0), // B
                new CourseGrade("MK003", 3, 2.0)  // C
        );

        double gpa = gradeCalculator.calculateGPA(grades);
        assertEquals(3.0, gpa);
    }

    @Test
    @DisplayName("Should Return 0.0 GPA for Empty Grade List")
    void testCalculateGPAEmptyList() {
        List<CourseGrade> grades = List.of();
        double gpa = gradeCalculator.calculateGPA(grades);
        assertEquals(0.0, gpa);
    }

    @Test
    @DisplayName("Should Return 0.0 GPA for Null Grade List")
    void testCalculateGPANullList() {
        double gpa = gradeCalculator.calculateGPA(null);
        assertEquals(0.0, gpa);
    }

    @Test
    @DisplayName("Should Return 0.0 GPA if Total Credits is Zero")
    void testCalculateGPAZeroCredits() {
        List<CourseGrade> grades = List.of(
                new CourseGrade("MK001", 0, 4.0)
        );
        double gpa = gradeCalculator.calculateGPA(grades);
        assertEquals(0.0, gpa);
    }

    @Test
    @DisplayName("Should Throw Exception for Invalid Grade Point > 4.0")
    void testCalculateGPAInvalidGradeHigh() {
        List<CourseGrade> grades = List.of(
                new CourseGrade("MK001", 3, 5.0) // Invalid
        );
        assertThrows(IllegalArgumentException.class, () -> {
            gradeCalculator.calculateGPA(grades);
        });
    }

    @Test
    @DisplayName("Should Throw Exception for Invalid Grade Point < 0.0")
    void testCalculateGPAInvalidGradeLow() {
        List<CourseGrade> grades = List.of(
                new CourseGrade("MK001", 3, -1.0) // Invalid
        );
        assertThrows(IllegalArgumentException.class, () -> {
            gradeCalculator.calculateGPA(grades);
        });
    }

    // --- Tes untuk determineAcademicStatus ---

    @ParameterizedTest
    @CsvSource({
            // Semester 1-2
            "1, 2.0, ACTIVE",
            "2, 2.5, ACTIVE",
            "1, 1.99, PROBATION",
            "2, 0.0, PROBATION",
            // Semester 3-4
            "3, 2.25, ACTIVE",
            "4, 3.0, ACTIVE",
            "3, 2.24, PROBATION",
            "4, 2.0, PROBATION",
            "3, 1.99, SUSPENDED",
            "4, 1.5, SUSPENDED",
            // Semester 5+
            "5, 2.5, ACTIVE",
            "8, 4.0, ACTIVE",
            "5, 2.49, PROBATION",
            "6, 2.0, PROBATION",
            "5, 1.99, SUSPENDED",
            "7, 0.0, SUSPENDED"
    })
    @DisplayName("Should Determine Academic Status Correctly")
    void testDetermineAcademicStatus(int semester, double gpa, String expectedStatus) {
        String status = gradeCalculator.determineAcademicStatus(gpa, semester);
        assertEquals(expectedStatus, status);
    }

    @Test
    @DisplayName("Should Throw Exception for Invalid GPA (Status)")
    void testDetermineAcademicStatusInvalidGPA() {
        assertThrows(IllegalArgumentException.class, () -> {
            gradeCalculator.determineAcademicStatus(4.1, 3);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            gradeCalculator.determineAcademicStatus(-0.1, 3);
        });
    }

    @Test
    @DisplayName("Should Throw Exception for Invalid Semester (Status)")
    void testDetermineAcademicStatusInvalidSemester() {
        assertThrows(IllegalArgumentException.class, () -> {
            gradeCalculator.determineAcademicStatus(3.0, 0);
        });
    }

    // --- Tes untuk calculateMaxCredits ---

    @ParameterizedTest
    @CsvSource({
            "4.0, 24",  //
            "3.0, 24",  //
            "2.99, 21", //
            "2.5, 21",  //
            "2.49, 18", //
            "2.0, 18",  //
            "1.99, 15", //
            "0.0, 15"   //
    })
    @DisplayName("Should Determine SKS Limit Correctly")
    void testCalculateMaxCredits(double gpa, int expectedSks) {
        int maxSks = gradeCalculator.calculateMaxCredits(gpa);
        assertEquals(expectedSks, maxSks);
    }

    @Test
    @DisplayName("Should Throw Exception for Invalid GPA (Credits)")
    void testCalculateMaxCreditsInvalidGPA() {
        assertThrows(IllegalArgumentException.class, () -> {
            gradeCalculator.calculateMaxCredits(5.0);
        });
    }
}