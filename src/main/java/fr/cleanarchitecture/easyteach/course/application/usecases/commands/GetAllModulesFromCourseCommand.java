package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.ModuleFromCourseViewModel;

public class GetAllModulesFromCourseCommand implements Command<ModuleFromCourseViewModel> {
    private String courseId;

    public GetAllModulesFromCourseCommand(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseId() {
        return courseId;
    }
}
