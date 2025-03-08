package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.InputLesson;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;

public class AddLessonToModuleCommand implements Command<CourseViewModel> {
    private String courseId;
    private String moduleId;
    private InputLesson lesson;

    public AddLessonToModuleCommand() {}

    public AddLessonToModuleCommand(String courseId, String moduleId, InputLesson lesson) {
        this.courseId = courseId;
        this.moduleId = moduleId;
        this.lesson = lesson;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public InputLesson getLesson() {
        return lesson;
    }
}
