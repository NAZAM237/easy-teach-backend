package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.core.domain.viewmodel.BaseViewModel;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.AddQuestionToQuizCommand;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Quiz;

public class AddQuestionToQuizCommandHandler implements Command.Handler<AddQuestionToQuizCommand, BaseViewModel<Quiz>> {

    private CourseRepository courseRepository;

    public AddQuestionToQuizCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public BaseViewModel<Quiz> handle(AddQuestionToQuizCommand addQuestionToQuizCommand) {
        var course = courseRepository.findByCourseId(addQuestionToQuizCommand.getIdsCourse().getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        course.addQuestionToQuiz(
                addQuestionToQuizCommand.getIdsCourse().getModuleId(),
                addQuestionToQuizCommand.getIdsCourse().getLessonId(),
                addQuestionToQuizCommand.getQuestion());
        courseRepository.save(course);
        var quiz = getQuiz(
                course,
                addQuestionToQuizCommand.getIdsCourse().getModuleId(),
                addQuestionToQuizCommand.getIdsCourse().getLessonId());
        return new BaseViewModel<>(quiz);
    }

    private Quiz getQuiz(Course course, String moduleId, String lessonId) {
        return course.getModules()
                .stream()
                .filter(module -> module.getModuleId().equals(moduleId))
                .findFirst().orElseThrow(() -> new NotFoundException("Module not found"))
                .getLessons()
                .stream()
                .filter(lesson -> lesson.getLessonId().equals(lessonId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Lesson not found"))
                .getQuiz();
    }
}
