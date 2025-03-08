package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;

public class RemoveLessonFromModuleCommand implements Command<Void> {
    private String courseId;
    private String moduleId;
    private String lessonId;

    public RemoveLessonFromModuleCommand() {}

    public RemoveLessonFromModuleCommand(String courseId, String moduleId, String lessonId) {
        this.courseId = courseId;
        this.moduleId = moduleId;
        this.lessonId = lessonId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public String getLessonId() {
        return lessonId;
    }
}
