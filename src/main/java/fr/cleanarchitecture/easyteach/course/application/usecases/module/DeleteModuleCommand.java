package fr.cleanarchitecture.easyteach.course.application.usecases.module;

import an.awesome.pipelinr.Command;

public class DeleteModuleCommand implements Command<Void> {
    private String moduleId;

    public DeleteModuleCommand() {}

    public DeleteModuleCommand(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleId() {
        return moduleId;
    }
}
