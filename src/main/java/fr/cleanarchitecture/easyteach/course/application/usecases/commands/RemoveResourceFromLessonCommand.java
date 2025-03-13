package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;

public class RemoveResourceFromLessonCommand implements Command<Void> {
    private String courseId;
    private String moduleId;
    private String lessonId;
    private String resourceId;

    public RemoveResourceFromLessonCommand(String courseId, String moduleId, String lessonId, String resourceId) {
        this.courseId = courseId;
        this.moduleId = moduleId;
        this.lessonId = lessonId;
        this.resourceId = resourceId;
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

    public String getResourceId() {
        return resourceId;
    }
}
