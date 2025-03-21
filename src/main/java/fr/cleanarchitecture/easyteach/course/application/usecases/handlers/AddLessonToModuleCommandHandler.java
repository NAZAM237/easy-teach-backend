package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.core.domain.viewmodel.BaseViewModel;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.AddLessonToModuleCommand;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;

public class AddLessonToModuleCommandHandler implements Command.Handler<AddLessonToModuleCommand, BaseViewModel<Lesson>> {

    private final CourseRepository courseRepository;

    public AddLessonToModuleCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public BaseViewModel<Lesson> handle(AddLessonToModuleCommand addLessonToModuleCommand) {
        var course = courseRepository.findByCourseId(addLessonToModuleCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        var lesson = new Lesson(
                addLessonToModuleCommand.getLesson().getTitle(),
                addLessonToModuleCommand.getLesson().getContentType(),
                addLessonToModuleCommand.getLesson().getContentFileUrl(),
                addLessonToModuleCommand.getLesson().getTextContent(),
                addLessonToModuleCommand.getLesson().getOrder()
        );
        course.addLessonToModule(
                addLessonToModuleCommand.getModuleId(),
                lesson
        );
        courseRepository.save(course);

        return new BaseViewModel<>(lesson);
    }
}
