package fr.cleanarchitecture.easyteach.course.application.usecases.module;

import an.awesome.pipelinr.Command;

public class RemoveLessonFromModuleCommand implements Command<Void> {
    private String moduleId;
    private String lessonId;

    public RemoveLessonFromModuleCommand() {}

    public RemoveLessonFromModuleCommand(String moduleId, String lessonId) {
        this.moduleId = moduleId;
        this.lessonId = lessonId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public String getLessonId() {
        return lessonId;
    }
}
