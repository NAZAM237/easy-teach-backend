package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.UpdateLessonFromModuleCommand;
import fr.cleanarchitecture.easyteach.course.domain.enums.CourseStatus;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.shared.domain.viewmodel.BaseViewModel;

public class UpdateLessonFromModuleCommandHandler implements Command.Handler<UpdateLessonFromModuleCommand, BaseViewModel<Lesson>> {
    private final CourseRepository courseRepository;

    public UpdateLessonFromModuleCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public BaseViewModel<Lesson> handle(UpdateLessonFromModuleCommand updateLessonFromModuleCommand) {
        var course = courseRepository.findByCourseId(updateLessonFromModuleCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        var module = course.getModules()
                .stream()
                .filter(module1 -> module1.getModuleId().equals(updateLessonFromModuleCommand.getModuleId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Module not found"));

        if (hasContentFileUrl(course, updateLessonFromModuleCommand)) {
            throw new BadRequestException("ContentFile must not be null for " + updateLessonFromModuleCommand.getLesson().getContentType().name());
        }
        module.updateLessonData(
                updateLessonFromModuleCommand.getLessonId(),
                updateLessonFromModuleCommand.getLesson());
        courseRepository.save(course);

        var lesson = module.getLessons()
                .stream()
                .filter(lesson1 -> lesson1.getLessonId().equals(updateLessonFromModuleCommand.getLessonId()))
                .findFirst().orElseThrow();

        return new BaseViewModel<>(lesson);
    }

    private boolean hasContentFileUrl(Course course, UpdateLessonFromModuleCommand command) {
        return CourseStatus.PUBLISHED.equals(course.getStatus())
                && !ResourceType.TEXT.equals(command.getLesson().getContentType())
                && null == command.getLesson().getContentFileUrl();
    }
}
