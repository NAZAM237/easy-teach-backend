package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.core.domain.viewmodel.BaseViewModel;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.AttachQuizToLessonCommand;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
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

        course.attachQuizToLesson(
                attachQuizToLessonCommand.getIdsCourse().getModuleId(),
                attachQuizToLessonCommand.getIdsCourse().getLessonId(),
                attachQuizToLessonCommand.getQuiz());
        courseRepository.save(course);
        var lesson = getLesson(
                course,
                attachQuizToLessonCommand.getIdsCourse().getModuleId(),
                attachQuizToLessonCommand.getIdsCourse().getLessonId());
        return new BaseViewModel<>(lesson);
    }

    private Lesson getLesson(Course course, String moduleId, String lessonId) {
        return course.getModules().stream().filter(module -> module.getModuleId().equals(moduleId)).findFirst().orElseThrow()
                .getLessons()
                .stream()
                .filter(lesson1 -> lesson1.getLessonId().equals(lessonId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Lesson not found"));
    }
}
