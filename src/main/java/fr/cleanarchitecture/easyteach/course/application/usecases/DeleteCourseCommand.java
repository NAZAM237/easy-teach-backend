package fr.cleanarchitecture.easyteach.course.application.usecases;

import an.awesome.pipelinr.Command;

public class DeleteCourseCommand implements Command<Void> {
    private String courseId;

    public DeleteCourseCommand(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseId() {
        return courseId;
    }
}
