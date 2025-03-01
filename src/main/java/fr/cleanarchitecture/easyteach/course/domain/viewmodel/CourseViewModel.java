package fr.cleanarchitecture.easyteach.course.domain.viewmodel;

import fr.cleanarchitecture.easyteach.course.domain.model.Course;

public class CourseViewModel {
    private String message;
    private Course course;

    public CourseViewModel() {}

    public CourseViewModel(String message, Course course) {
        this.message = message;
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }

    public String getMessage() {
        return message;
    }
}
