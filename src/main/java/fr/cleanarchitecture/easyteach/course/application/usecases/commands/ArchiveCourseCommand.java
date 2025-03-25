package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.shared.domain.viewmodel.BaseViewModel;

public class ArchiveCourseCommand implements Command<BaseViewModel<Course>> {
    private String courseId;

    public ArchiveCourseCommand() {}

    public ArchiveCourseCommand(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseId() {
        return courseId;
    }
}
