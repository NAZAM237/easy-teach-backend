package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.core.domain.viewmodel.BaseViewModel;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.AttachQuizToLessonCommand;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;

public class AttachQuizToLessonCommandHandler implements Command.Handler<AttachQuizToLessonCommand, BaseViewModel<Lesson>> {

    public CourseRepository courseRepository;

    public AttachQuizToLessonCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public BaseViewModel<Lesson> handle(AttachQuizToLessonCommand attachQuizToLessonCommand) {
        var course = courseRepository.findByCourseId(attachQuizToLessonCommand.getIdsCourse().getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        var module = course.getModules()
                .stream()
                .filter(module1 -> module1.getModuleId().equals(attachQuizToLessonCommand.getIdsCourse().getModuleId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Module not found"));
        var lesson = module.getLessons()
                .stream()
                .filter(lesson1 -> lesson1.getLessonId().equals(attachQuizToLessonCommand.getIdsCourse().getLessonId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Lesson not found"));

        lesson.attachQuiz(attachQuizToLessonCommand.getQuiz());
        courseRepository.save(course);
        return new BaseViewModel<>(lesson);
    }
}
