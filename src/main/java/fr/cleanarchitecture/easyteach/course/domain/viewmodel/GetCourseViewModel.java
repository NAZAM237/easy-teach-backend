package fr.cleanarchitecture.easyteach.course.domain.viewmodel;

import fr.cleanarchitecture.easyteach.course.domain.enums.CourseStatus;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;

import java.util.HashSet;
import java.util.Set;

public class GetCourseViewModel {
    private String courseId;
    private String courseTitle;
    private String courseDescription;
    private Teacher teacher;
    private Price price;
    private CourseStatus status;
    private Set<Module> modules = new HashSet<>();

    public GetCourseViewModel() {}

    public GetCourseViewModel(Course course) {
        this.courseId = course.getCourseId();
        this.courseTitle = course.getCourseTitle();
        this.courseDescription = course.getCourseDescription();
        this.teacher = course.getTeacher();
        this.price = course.getPrice();
        this.status = course.getStatus();
        this.modules = course.getModules();
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Price getPrice() {
        return price;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public Set<Module> getModules() {
        return modules;
    }
}
