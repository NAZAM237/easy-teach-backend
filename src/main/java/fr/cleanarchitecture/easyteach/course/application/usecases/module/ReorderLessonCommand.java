package fr.cleanarchitecture.easyteach.course.application.usecases.module;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.ModuleViewModel;

import java.util.List;

public class ReorderLessonCommand implements Command<ModuleViewModel> {
    private final String moduleId;
    private final List<Lesson> lessons;

    public ReorderLessonCommand(String moduleId, List<Lesson> lessons) {
        this.moduleId = moduleId;
        this.lessons = lessons;
    }

    public String getModuleId() {
        return moduleId;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }
}
