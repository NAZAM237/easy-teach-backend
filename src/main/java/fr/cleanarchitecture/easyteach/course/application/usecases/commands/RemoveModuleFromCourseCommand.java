package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;

public class RemoveModuleFromCourseCommand implements Command<Void> {
    private final String courseId;
    private final String moduleId;

    public RemoveModuleFromCourseCommand(String courseId, String moduleId) {
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
