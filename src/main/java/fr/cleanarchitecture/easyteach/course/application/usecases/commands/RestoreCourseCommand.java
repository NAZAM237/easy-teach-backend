package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.viewmodel.BaseViewModel;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;

public class RestoreCourseCommand implements Command<BaseViewModel<Course>> {
    private String courseId;

    public RestoreCourseCommand(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseId() {
        return courseId;
    }
}
