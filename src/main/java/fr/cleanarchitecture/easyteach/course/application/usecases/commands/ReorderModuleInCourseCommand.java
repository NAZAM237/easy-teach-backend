package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;

import java.util.List;

public class ReorderModuleInCourseCommand implements Command<CourseViewModel> {
    private String courseId;
    private List<Module> modules;

    public ReorderModuleInCourseCommand(String courseId, List<Module> newOrderList) {
        this.courseId = courseId;
        this.modules = newOrderList;
    }

    public String getCourseId() {
        return courseId;
    }

    public List<Module> getModules() {
        return modules;
    }
}
