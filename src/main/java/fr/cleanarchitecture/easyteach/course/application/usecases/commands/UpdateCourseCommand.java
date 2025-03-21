package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.viewmodel.BaseViewModel;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;

public class UpdateCourseCommand implements Command<BaseViewModel<Course>> {
    private String courseId;
    private String courseTitle;
    private String courseDescription;
    private Price price;

    public UpdateCourseCommand() {}

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
