package fr.cleanarchitecture.easyteach.course.domain.viewmodel;

import fr.cleanarchitecture.easyteach.course.domain.model.Course;

public class CourseViewModel {
    private String message;
    private Course newCourse;

    public CourseViewModel() {}

    public CourseViewModel(String message, Course newCourse) {
        this.message = message;
        this.newCourse = newCourse;
    }

    public Course getNewCourse() {
        return newCourse;
    }

    public String getMessage() {
        return message;
    }
}
