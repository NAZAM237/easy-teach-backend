package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.core.domain.viewmodel.BaseViewModel;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.UpdateQuizFromLessonCommand;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Quiz;

public class UpdateQuizFromLessonCommandHandler implements Command.Handler<UpdateQuizFromLessonCommand, BaseViewModel<Quiz>> {

    private CourseRepository courseRepository;

    public UpdateQuizFromLessonCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public BaseViewModel<Quiz> handle(UpdateQuizFromLessonCommand updateQuizFromLessonCommand) {
        var course = courseRepository.findByCourseId(updateQuizFromLessonCommand.getIdsCourse().getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        course.updateQuizFromLesson(
                updateQuizFromLessonCommand.getIdsCourse().getModuleId(),
                updateQuizFromLessonCommand.getIdsCourse().getLessonId(),
                updateQuizFromLessonCommand.getQuiz());
        var quiz = getQuiz(
                course,
                updateQuizFromLessonCommand.getIdsCourse().getModuleId(),
                updateQuizFromLessonCommand.getIdsCourse().getLessonId());
        return new BaseViewModel<>(quiz);
    }

    private Quiz getQuiz(Course course, String moduleId, String lessonId) {
        var module = course.getModules().stream().filter(m -> m.getModuleId().equals(moduleId))
                .findFirst().orElseThrow(() -> new NotFoundException("Module not found"));
        var lesson = module.getLessons().stream().filter(l -> l.getLessonId().equals(lessonId))
                .findFirst().orElseThrow(() -> new NotFoundException("Lesson not found"));
        return lesson.getQuiz();
    }
}
