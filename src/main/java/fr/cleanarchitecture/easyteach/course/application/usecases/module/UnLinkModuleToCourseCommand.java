package fr.cleanarchitecture.easyteach.course.application.usecases.module;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.ModuleViewModel;

public class UnLinkModuleToCourseCommand implements Command<ModuleViewModel> {
    private String courseId;
    private String moduleId;

    public UnLinkModuleToCourseCommand(String courseId, String moduleId) {
        this.courseId = courseId;
        this.moduleId = moduleId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getModuleId() {
        return moduleId;
    }
}
