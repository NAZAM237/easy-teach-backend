package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.UpdateQuestionFromQuizCommand;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Question;
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.shared.domain.viewmodel.BaseViewModel;

public class UpdateQuestionFromQuizCommandHandler implements Command.Handler<UpdateQuestionFromQuizCommand, BaseViewModel<Question>> {

    private CourseRepository courseRepository;

    public UpdateQuestionFromQuizCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public BaseViewModel<Question> handle(UpdateQuestionFromQuizCommand updateQuestionFromQuizCommand) {
        var course = courseRepository.findByCourseId(updateQuestionFromQuizCommand.getIdsCourse().getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        course.updateQuestionFromQuiz(
                updateQuestionFromQuizCommand.getIdsCourse().getModuleId(),
                updateQuestionFromQuizCommand.getIdsCourse().getLessonId(),
                updateQuestionFromQuizCommand.getIdsCourse().getQuestionId(),
                updateQuestionFromQuizCommand.getQuestion());
        courseRepository.save(course);
        var question = getQuestion(
                course,
                updateQuestionFromQuizCommand.getIdsCourse().getModuleId(),
                updateQuestionFromQuizCommand.getIdsCourse().getLessonId(),
                updateQuestionFromQuizCommand.getIdsCourse().getQuestionId(),
                updateQuestionFromQuizCommand.getQuestion());
        return new BaseViewModel<>(question);
    }

    private Question getQuestion(Course course, String moduleId, String lessonId, String questionId, Question question) {
        var module = course.getModules().stream().filter(module1 -> module1.getModuleId().equals(moduleId)).findFirst()
                .orElseThrow(() -> new NotFoundException("Module not found"));
        var quiz = module.getLessons().stream().filter(lesson -> lesson.getLessonId().equals(lessonId)).findFirst()
                .orElseThrow(() -> new NotFoundException("Lesson not found")).getQuiz();
        return quiz.getQuestions().stream().filter(question1 -> question1.getQuestionId().equals(questionId)).findFirst()
                .orElseThrow(() -> new NotFoundException("Question not found"));
    }
}
