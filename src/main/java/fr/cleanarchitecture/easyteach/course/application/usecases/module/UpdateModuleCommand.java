package fr.cleanarchitecture.easyteach.course.application.usecases.module;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.ModuleViewModel;

public class UpdateModuleCommand implements Command<ModuleViewModel> {
    private String moduleId;
    private String moduleTitle;
    private String moduleDescription;

    public UpdateModuleCommand(String moduleId, String moduleTitle, String moduleDescription) {
        this.moduleId = moduleId;
        this.moduleTitle = moduleTitle;
        this.moduleDescription = moduleDescription;
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
