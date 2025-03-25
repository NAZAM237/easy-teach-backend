package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.AddAnswerToQuestionCommand;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Question;
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.shared.domain.viewmodel.BaseViewModel;

public class AddAnswerToQuestionCommandHandler implements Command.Handler<AddAnswerToQuestionCommand, BaseViewModel<Question>> {

    private CourseRepository courseRepository;

    public AddAnswerToQuestionCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public BaseViewModel<Question> handle(AddAnswerToQuestionCommand addAnswerToQuestionCommand) {
        var course = courseRepository.findByCourseId(addAnswerToQuestionCommand.getIdsCourse().getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        course.addAnswerToQuestion(
                addAnswerToQuestionCommand.getIdsCourse().getModuleId(),
                addAnswerToQuestionCommand.getIdsCourse().getLessonId(),
                addAnswerToQuestionCommand.getIdsCourse().getQuestionId(),
                addAnswerToQuestionCommand.getAnswer()
        );
        courseRepository.save(course);
        var question = getQuestion(
                course,
                addAnswerToQuestionCommand.getIdsCourse().getModuleId(),
                addAnswerToQuestionCommand.getIdsCourse().getLessonId(),
                addAnswerToQuestionCommand.getIdsCourse().getQuestionId());
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
