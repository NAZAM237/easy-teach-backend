package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.core.domain.viewmodel.BaseViewModel;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.AddQuestionToQuizCommand;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Question;

public class AddQuestionToQuizCommandHandler implements Command.Handler<AddQuestionToQuizCommand, BaseViewModel<Question>> {

    private CourseRepository courseRepository;

    public AddQuestionToQuizCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public BaseViewModel<Question> handle(AddQuestionToQuizCommand addQuestionToQuizCommand) {
        var course = courseRepository.findByCourseId(addQuestionToQuizCommand.getIdsCourse().getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        course.addQuestionToQuiz(
                addQuestionToQuizCommand.getIdsCourse().getModuleId(),
                addQuestionToQuizCommand.getIdsCourse().getLessonId(),
                addQuestionToQuizCommand.getQuestion());
        courseRepository.save(course);
        var question = getQuestion(
                course,
                addQuestionToQuizCommand.getIdsCourse().getModuleId(),
                addQuestionToQuizCommand.getIdsCourse().getLessonId(),
                addQuestionToQuizCommand.getQuestion().getQuestionId());
        return new BaseViewModel<>(question);
    }

    private Question getQuestion(Course course, String moduleId, String lessonId, String questionId) {
        var module =  course.getModules()
                .stream()
                .filter(module1 -> module1.getModuleId().equals(moduleId))
                .findFirst().orElseThrow(() -> new NotFoundException("Module not found"));
        var quiz = module.getLessons().stream()
                .filter(lesson -> lesson.getLessonId().equals(lessonId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Lesson not found"))
                .getQuiz();
        return quiz.getQuestions().stream().filter(question -> question.getQuestionId().equals(questionId))
                .findFirst().orElse(null);
    }
}
