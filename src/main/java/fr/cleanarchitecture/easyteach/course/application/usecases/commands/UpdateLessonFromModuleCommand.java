package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.InputLesson;
import fr.cleanarchitecture.easyteach.shared.domain.viewmodel.BaseViewModel;

public class UpdateLessonFromModuleCommand implements Command<BaseViewModel<Lesson>> {
    private String courseId;
    private String moduleId;
    private String lessonId;
    private InputLesson lesson;

    public UpdateLessonFromModuleCommand(String courseId, String moduleId, String lessonId, InputLesson inputLesson) {
        this.courseId = courseId;
        this.moduleId = moduleId;
        this.lessonId = lessonId;
        this.lesson = inputLesson;
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

    public InputLesson getLesson() {
        return lesson;
    }
}
