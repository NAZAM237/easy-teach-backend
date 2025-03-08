package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.GetCourseViewModel;

public class GetCourseByIdCommand implements Command<GetCourseViewModel> {
    private String courseId;

    public GetCourseByIdCommand(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseId() {
        return courseId;
    }
}
