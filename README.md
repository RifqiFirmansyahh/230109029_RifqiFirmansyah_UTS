# Proyek Unit Testing SIAKAD - UTS Pengujian Perangkat Lunak

Proyek ini adalah implementasi unit testing untuk studi kasus Sistem Informasi Akademik (SIAKAD) Politeknik Merdeka. Tugas ini dibuat untuk memenuhi Ujian Tengah Semester (UTS) mata kuliah Pengujian Perangkat Lunak.

Fokus dari proyek ini adalah untuk menguji logika bisnis yang terkandung dalam `GradeCalculator.java` dan `EnrollmentService.java` untuk memastikan fungsionalitasnya sudah benar dan mencapai target cakupan kode (code coverage) yang telah ditentukan.

## 🎯 Tujuan & Pengerjaan Soal

Tugas ini dibagi menjadi tiga bagian utama pengujian:
1.  **Soal A: Unit Test Biasa**
    * Menguji kelas `GradeCalculator` yang tidak memiliki dependensi.
    * Lokasi tes: `src/test/java/com/siakad/service/GradeCalculatorTest.java`

2.  **Soal B: Unit Test menggunakan Stub**
    * Menguji metode `validateCreditLimit` dari `EnrollmentService` dengan *stub* manual.
    * Lokasi tes: `src/test/java/com/siakad/service/EnrollmentServiceStubTest.java`
    * Lokasi stub: `src/test/java/com/siakad/service/stubs/`

3.  **Soal C: Unit Test menggunakan Mock**
    * Menguji kelas `EnrollmentService` secara penuh menggunakan *mock* dari Mockito.
    * Lokasi tes: `src/test/java/com/siakad/service/EnrollmentServiceTest.java`

## 🛠️ Teknologi yang Digunakan

* **Java 21**
* **Maven** (sebagai build tool dan dependency management)
* **JUnit 5** (untuk framework unit testing)
* **Mockito** (untuk membuat objek tiruan/mock dan stub)
* **JaCoCo** (untuk mengukur dan membuat laporan code coverage)

## 🚀 Cara Menjalankan Proyek

### 1. Prasyarat

* Java JDK 21 (atau yang lebih baru)
* Apache Maven

### 2. Menjalankan Tes

Anda dapat menjalankan semua unit test (JUnit dan Mockito) menggunakan perintah Maven berikut dari *root folder* proyek:

```bash
mvn test
