package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.shared.domain.viewmodel.BaseViewModel;

public class AddModuleToCourseCommand implements Command<BaseViewModel<Module>> {
    private String courseId;
    private String moduleTitle;
    private String moduleDescription;
    private int moduleOrder;

    public AddModuleToCourseCommand(String courseId, String moduleTitle, String moduleDescription, int moduleOrder) {
        this.moduleTitle = moduleTitle;
        this.moduleDescription = moduleDescription;
        this.moduleOrder = moduleOrder;
        this.courseId = courseId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getModuleTitle() {
        return moduleTitle;
    }

    public String getModuleDescription() {
        return moduleDescription;
    }

    public int getModuleOrder() {
        return moduleOrder;
    }

}
