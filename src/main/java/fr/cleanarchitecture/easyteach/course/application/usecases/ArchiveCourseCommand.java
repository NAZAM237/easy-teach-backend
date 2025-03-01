package fr.cleanarchitecture.easyteach.course.application.usecases;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;

public class ArchiveCourseCommand implements Command<CourseViewModel> {
    private String courseId;

    public ArchiveCourseCommand() {}

    public ArchiveCourseCommand(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseId() {
        return courseId;
    }
}
