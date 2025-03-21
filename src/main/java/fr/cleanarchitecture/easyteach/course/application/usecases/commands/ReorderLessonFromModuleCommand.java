package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.viewmodel.BaseViewModel;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;

import java.util.List;

public class ReorderLessonFromModuleCommand implements Command<BaseViewModel<Course>> {
    private String courseId;
    private final String moduleId;
    private final List<Lesson> lessons;

    public ReorderLessonFromModuleCommand(String courseId, String moduleId, List<Lesson> lessons) {
        this.courseId = courseId;
        this.moduleId = moduleId;
        this.lessons = lessons;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }
}
