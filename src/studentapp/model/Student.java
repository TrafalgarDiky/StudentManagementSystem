package studentapp.model;

import java.util.Date;

public class Student {
    // 🧱 Field (atribut mahasiswa)
    private int studentId;     // NIM
    private String name;       // Nama mahasiswa
    private String email;      // Email
    private Date birthDate;    // Tanggal lahir
    private String major;      // Jurusan
    private String gender;     // Jenis kelamin

    // 🏗️ Constructor kosong (default)
    public Student() {}

    // 🏗️ Constructor lengkap (langsung isi data)
    public Student(int studentId, String name, String email, Date birthDate, String major, String gender) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.major = major;
        this.gender = gender;
    }

    // 🔍 Getter dan Setter
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // 🧩 Fungsi tambahan untuk debugging
    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birthDate=" + birthDate +
                ", major='" + major + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
