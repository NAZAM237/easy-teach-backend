package fr.cleanarchitecture.easyteach.course.application.usecases;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.model.Instructor;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;

public class CreateCourseCommand implements Command<CourseViewModel> {
    private String courseTitle;
    private String courseDescription;
    private Instructor instructor;
    private Price price;

    public CreateCourseCommand(String courseName, String courseDescription, Instructor instructor, Price price) {
        this.courseTitle = courseName;
        this.courseDescription = courseDescription;
        this.instructor = instructor;
        this.price = price;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public Price getPrice() {
        return price;
    }
}
