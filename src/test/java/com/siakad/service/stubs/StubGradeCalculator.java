package com.siakad.service.stubs;

import com.siakad.service.GradeCalculator;

/**
 * Ini adalah STUB. Implementasi manual untuk testing (Soal B).
 * Dibuat di src/test/java dan TIDAK melanggar aturan.
 */
public class StubGradeCalculator extends GradeCalculator { // extends class asli

    @Override
    public int calculateMaxCredits(double gpa) {
        // Mensimulasikan logika asli untuk metode yang kita butuhkan
        if (gpa >= 3.0) return 24;
        if (gpa >= 2.5) return 21;
        if (gpa >= 2.0) return 18;
        return 15;
    }

    // Metode lain tidak perlu di-override
}