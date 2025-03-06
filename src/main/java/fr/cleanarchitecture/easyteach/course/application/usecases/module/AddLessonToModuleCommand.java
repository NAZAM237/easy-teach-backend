package fr.cleanarchitecture.easyteach.course.application.usecases.module;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.InputLesson;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.ModuleViewModel;

public class AddLessonToModuleCommand implements Command<ModuleViewModel> {
    private String moduleId;
    private InputLesson lesson;

    public AddLessonToModuleCommand() {}

    public AddLessonToModuleCommand(String moduleId, InputLesson lesson) {
        this.moduleId = moduleId;
        this.lesson = lesson;
    }

    public String getModuleId() {
        return moduleId;
    }

    public InputLesson getLesson() {
        return lesson;
    }
}
