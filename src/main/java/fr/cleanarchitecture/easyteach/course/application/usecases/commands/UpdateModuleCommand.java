package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;

public class UpdateModuleCommand implements Command<CourseViewModel> {
    private String courseId;
    private String moduleId;
    private String moduleTitle;
    private String moduleDescription;

    public UpdateModuleCommand(String courseId, String moduleId, String moduleTitle, String moduleDescription) {
        this.courseId = courseId;
        this.moduleId = moduleId;
        this.moduleTitle = moduleTitle;
        this.moduleDescription = moduleDescription;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public String getModuleTitle() {
        return moduleTitle;
    }

    public String getModuleDescription() {
        return moduleDescription;
    }
}
