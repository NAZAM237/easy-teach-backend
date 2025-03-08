package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;

public class PublishCourseCommand implements Command<CourseViewModel> {
    private String courseId;

    public PublishCourseCommand(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseId() {
        return courseId;
    }
}
