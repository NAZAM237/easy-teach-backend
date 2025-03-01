package fr.cleanarchitecture.easyteach.course.domain.model;

public class Teacher {
    private String teacherId;
    private String teacherName;
    private String teacherBiography;
    private String teacherEmail;
    private String teacherPhone;
    private String teacherPhoto;

    public Teacher() {}

    public Teacher(String teacherId, String teacherName, String teacherBiography,
                   String teacherEmail, String teacherPhone, String teacherPhoto) {
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.teacherBiography = teacherBiography;
        this.teacherEmail = teacherEmail;
        this.teacherPhone = teacherPhone;
        this.teacherPhoto = teacherPhoto;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getTeacherBiography() {
        return teacherBiography;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public String getTeacherPhone() {
        return teacherPhone;
    }

    public String getTeacherPhoto() {
        return teacherPhoto;
    }
}
