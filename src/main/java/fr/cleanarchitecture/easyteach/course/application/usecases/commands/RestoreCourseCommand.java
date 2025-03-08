package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;

public class RestoreCourseCommand implements Command<CourseViewModel> {
    private String courseId;

    public RestoreCourseCommand(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseId() {
        return courseId;
    }
}
