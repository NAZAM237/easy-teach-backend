package fr.cleanarchitecture.easyteach.course.application.usecases;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;

public class UpdateCourseCommand implements Command<CourseViewModel> {
    private String courseId;
    private String courseTitle;
    private String courseDescription;
    private Price price;

    public UpdateCourseCommand(String courseId, String courseTitle, String courseDescription, Price price) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.price = price;
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

    public Price getPrice() {
        return price;
    }
}
