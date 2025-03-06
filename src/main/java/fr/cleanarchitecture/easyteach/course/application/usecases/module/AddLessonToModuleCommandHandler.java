package fr.cleanarchitecture.easyteach.course.application.usecases.module;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.ModuleRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.ModuleViewModel;

public class AddLessonToModuleCommandHandler implements Command.Handler<AddLessonToModuleCommand, ModuleViewModel> {

    private final ModuleRepository moduleRepository;

    public AddLessonToModuleCommandHandler(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @Override
    public ModuleViewModel handle(AddLessonToModuleCommand addLessonToModuleCommand) {
        var module = moduleRepository.findByModuleId(addLessonToModuleCommand.getModuleId())
                .orElseThrow(() -> new NotFoundException("Module not found"));
        var lesson = new Lesson(
                addLessonToModuleCommand.getLesson().getTitle(),
                addLessonToModuleCommand.getLesson().getContentType(),
                addLessonToModuleCommand.getLesson().getVideoUrl(),
                addLessonToModuleCommand.getLesson().getTextContent(),
                addLessonToModuleCommand.getLesson().getOrder()
        );
        module.addLesson(lesson);
        return new ModuleViewModel(module);
    }
}
