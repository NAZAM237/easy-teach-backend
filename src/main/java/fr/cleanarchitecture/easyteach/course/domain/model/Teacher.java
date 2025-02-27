package fr.cleanarchitecture.easyteach.course.domain.model;

public class Teacher {
    private String teacherId;

    public Teacher() {}

    public Teacher(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherId() {
        return teacherId;
    }
}
