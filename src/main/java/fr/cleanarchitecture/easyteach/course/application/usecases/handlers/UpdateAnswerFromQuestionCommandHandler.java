package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.core.domain.viewmodel.BaseViewModel;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.UpdateAnswerFromQuestionCommand;
import fr.cleanarchitecture.easyteach.course.domain.model.Answer;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;

public class UpdateAnswerFromQuestionCommandHandler implements Command.Handler<UpdateAnswerFromQuestionCommand, BaseViewModel<Answer>> {

    private final CourseRepository courseRepository;

    public UpdateAnswerFromQuestionCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public BaseViewModel<Answer> handle(UpdateAnswerFromQuestionCommand updateAnswerFromQuestionCommand) {
        var course = courseRepository.findByCourseId(updateAnswerFromQuestionCommand.getIdsCourse().getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        course.updateAnswerFromQuestion(
                updateAnswerFromQuestionCommand.getIdsCourse().getModuleId(),
                updateAnswerFromQuestionCommand.getIdsCourse().getLessonId(),
                updateAnswerFromQuestionCommand.getIdsCourse().getQuestionId(),
                updateAnswerFromQuestionCommand.getIdsCourse().getAnswerId(),
                new Answer(updateAnswerFromQuestionCommand.getAnswerText(), updateAnswerFromQuestionCommand.isCorrectAnswer())
        );
        var answer = getAnswer(
                course,
                updateAnswerFromQuestionCommand.getIdsCourse().getModuleId(),
                updateAnswerFromQuestionCommand.getIdsCourse().getLessonId(),
                updateAnswerFromQuestionCommand.getIdsCourse().getQuestionId(),
                updateAnswerFromQuestionCommand.getIdsCourse().getAnswerId()
        );
        return new BaseViewModel<>(answer);
    }

    private Answer getAnswer(Course course, String moduleId, String lessonId, String questionId, String answerId) {
        var module = course.getModules().stream().filter(module1 -> module1.getModuleId().equals(moduleId))
                .findFirst().orElseThrow(() -> new NotFoundException("Module not found"));
        var quiz = module.getLessons().stream().filter(lesson -> lesson.getLessonId().equals(lessonId))
                .findFirst().orElseThrow(() -> new NotFoundException("Lesson not found")).getQuiz();
        var question = quiz.getQuestions().stream().filter(q -> q.getQuestionId().equals(questionId))
                .findFirst().orElseThrow(() -> new NotFoundException("Question not found"));
        return question.getAnswers().stream().filter(answer -> answer.getAnswerId().equals(answerId))
                .findFirst().orElseThrow(() -> new NotFoundException("Answer not found"));
    }
}
