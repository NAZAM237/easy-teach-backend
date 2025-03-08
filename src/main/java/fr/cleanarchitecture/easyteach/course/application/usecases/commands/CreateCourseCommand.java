package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;

public class CreateCourseCommand implements Command<CourseViewModel> {
    private String courseTitle;
    private String courseDescription;
    private String teacherUuid;
    private Price price;

    public CreateCourseCommand(String courseName, String courseDescription, String teacherUuid, Price price) {
        this.courseTitle = courseName;
        this.courseDescription = courseDescription;
        this.teacherUuid = teacherUuid;
        this.price = price;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public String getTeacherUuid() {
        return teacherUuid;
    }

    public Price getPrice() {
        return price;
    }
}
