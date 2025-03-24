package fr.cleanarchitecture.easyteach.course.infrastructure.spring;

import an.awesome.pipelinr.Pipeline;
import fr.cleanarchitecture.easyteach.core.domain.viewmodel.BaseViewModel;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.*;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.*;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.InputLesson;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.FileUploadResponse;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.IdsCourse;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.ModuleFromCourseViewModel;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.dtos.*;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.mapper.FromQuestionDtoToQuestion;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("courses")
public class CourseController {

    private final Pipeline pipeline;

    public CourseController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @GetMapping
    public ResponseEntity<BaseViewModel<List<Course>>> getAllCourses() {
        var result = this.pipeline.send(new GetAllCoursesCommand());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<BaseViewModel<Course>> getCourseById(@PathVariable String courseId) {
        var result = this.pipeline.send(
                new GetCourseByIdCommand(courseId)
        );
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{courseId}/modules")
    public ResponseEntity<ModuleFromCourseViewModel> getAllModulesFromCourse(@PathVariable String courseId) {
        var result = this.pipeline.send(new GetAllModulesFromCourseCommand(courseId));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping
    public ResponseEntity<BaseViewModel<Course>> createCourse(@RequestBody CreateCourseDto course) {
        var result = this.pipeline.send(
                new CreateCourseCommand(course.getTitle(), course.getDescription(), new Price(course.getAmount(), course.getCurrency())));
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PatchMapping("/{courseId}")
    public ResponseEntity<BaseViewModel<Course>> updateCourse(@RequestBody UpdateCourseDto course, @PathVariable String courseId) {
        var result = this.pipeline.send(
                new UpdateCourseCommand(courseId, course.getCourseTitle(), course.getCourseDescription(), new Price(course.getCoursePrice(), course.getCurrency())));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/{courseId}/publish")
    public ResponseEntity<BaseViewModel<Course>> publishCourse(@PathVariable String courseId) {
        var result = this.pipeline.send(new PublishCourseCommand(courseId));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/{courseId}/archive")
    public ResponseEntity<BaseViewModel<Course>> archiveCourse(@PathVariable String courseId) {
        var result = this.pipeline.send(new ArchiveCourseCommand(courseId));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/{courseId}/restore")
    public ResponseEntity<BaseViewModel<Course>> restoreCourse(@PathVariable String courseId) {
        var result = this.pipeline.send(new RestoreCourseCommand(courseId));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/{courseId}/modules")
    public ResponseEntity<BaseViewModel<Module>> addModuleToCourse(@PathVariable String courseId, @RequestBody AddModuleToCourseDto module) {
        var result = this.pipeline.send(
                new AddModuleToCourseCommand(
                        courseId,
                        module.getModuleTitle(),
                        module.getModuleDescription(),
                        module.getModuleOrder()));
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/{courseId}/modules/{moduleId}")
    public ResponseEntity<Void> removeModuleFromCourse(@PathVariable String courseId, @PathVariable String moduleId) {
        this.pipeline.send(new RemoveModuleFromCourseCommand(courseId, moduleId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String courseId) {
        this.pipeline.send(new DeleteCourseCommand(courseId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{courseId}/modules/{moduleId}/lessons")
    public ResponseEntity<BaseViewModel<Course>> reorderLessonsFromModule(
            @PathVariable String courseId,
            @PathVariable String moduleId,
            @RequestBody List<Lesson> lessons) {
        var result = this.pipeline.send(
                new ReorderLessonFromModuleCommand(courseId, moduleId, lessons)
        );
        return ResponseEntity.ok(result);
    }

    @PostMapping("{courseId}/modules/{moduleId}/lessons")
    public ResponseEntity<BaseViewModel<Lesson>> addLessonToModule(
            @PathVariable String courseId,
            @PathVariable String moduleId,
            @RequestBody AddLessonToModuleDto addLessonToModuleDto) {
        var result = this.pipeline.send(
                new AddLessonToModuleCommand(
                        courseId,
                        moduleId,
                        new InputLesson(
                                addLessonToModuleDto.getTitle(),
                                addLessonToModuleDto.getLessonType(),
                                addLessonToModuleDto.getContentFileUrl(),
                                addLessonToModuleDto.getTextContent(),
                                addLessonToModuleDto.getOrder()))
        );
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("{courseId}/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Void> removeLessonFromModule(
            @PathVariable String courseId,
            @PathVariable String moduleId,
            @PathVariable String lessonId) {
        this.pipeline.send(new RemoveLessonFromModuleCommand(courseId, moduleId, lessonId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{courseId}/modules/{moduleId}")
    public ResponseEntity<BaseViewModel<Module>> updateModule(
            @PathVariable String courseId,
            @PathVariable String moduleId,
            @RequestBody UpdateModuleDto updateModuleDto) {
        var result = this.pipeline.send(
                new UpdateModuleFromCourseCommand(
                        courseId,
                        moduleId,
                        updateModuleDto.getModuleTitle(),
                        updateModuleDto.getModuleDescription()));
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{courseId}/modules")
    public ResponseEntity<BaseViewModel<Course>> reorderModulesFromCourse(
            @PathVariable String courseId,
            @RequestBody List<Module> newModules) {
        var result = this.pipeline.send(
                new ReorderModuleInCourseCommand(
                        courseId,
                        newModules));
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{courseId}/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<BaseViewModel<Lesson>> updateLessonFromModule(
            @PathVariable String courseId,
            @PathVariable String moduleId,
            @PathVariable String lessonId,
            @RequestBody UpdateLessonFromModuleDto updateLessonFromModuleDto) {
        var result = this.pipeline.send(
                new UpdateLessonFromModuleCommand(
                        courseId,
                        moduleId,
                        lessonId,
                        new InputLesson(
                                updateLessonFromModuleDto.getLessonTitle(),
                                updateLessonFromModuleDto.getLessonType(),
                                updateLessonFromModuleDto.getContentFileUrl(),
                                updateLessonFromModuleDto.getLessonTextContent()
                        )));
        return ResponseEntity.ok(result);
    }

    @PostMapping(
            value = "/{courseId}/modules/{moduleId}/lessons/{lessonId}/resources",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseViewModel<Resource>> addResourceToLesson(
            @PathVariable String courseId,
            @PathVariable String moduleId,
            @PathVariable String lessonId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String resourceType) {
        var result = this.pipeline.send(new AddResourceToLessonCommand(new IdsCourse(courseId, moduleId, lessonId), file, resourceType));
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping(
            value = "/{courseId}/modules/{moduleId}/lessons/{lessonId}/content",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseViewModel<FileUploadResponse>> addContentFileToLesson(
            @PathVariable String courseId,
            @PathVariable String moduleId,
            @PathVariable String lessonId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String lessonContentType) {
        var result = this.pipeline.send(new AddContentFileToLessonCommand(new IdsCourse(courseId, moduleId, lessonId), file, lessonContentType));
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/{courseId}/modules/{moduleId}/lessons/{lessonId}/resources/{resourceId}")
    public ResponseEntity<Void> removeResourceFromLesson(
            @PathVariable String courseId,
            @PathVariable String moduleId,
            @PathVariable String lessonId,
            @PathVariable String resourceId) {
        this.pipeline.send(new RemoveResourceFromLessonCommand(courseId, moduleId, lessonId, resourceId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{courseId}/modules/{moduleId}/lessons/{lessonId}/quiz")
    public ResponseEntity<BaseViewModel<Lesson>> attachQuizToLesson(
            @PathVariable String courseId,
            @PathVariable String moduleId,
            @PathVariable String lessonId,
            @RequestBody QuizDto quizDto) {
        var result = this.pipeline.send(
                new AttachQuizToLessonCommand(
                        new IdsCourse(courseId, moduleId, lessonId),
                        new Quiz(
                                quizDto.getQuizTitle(),
                                quizDto.getQuizDescription(),
                                quizDto.getPassingScore())
                )
        );
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/{courseId}/modules/{moduleId}/lessons/{lessonId}/questions")
    public ResponseEntity<BaseViewModel<Question>> addQuestionToQuiz(
            @PathVariable String courseId,
            @PathVariable String moduleId,
            @PathVariable String lessonId,
            @RequestBody QuestionDto questionDto) {
        var result = this.pipeline.send(
                new AddQuestionToQuizCommand(
                        new IdsCourse(courseId, moduleId, lessonId),
                        FromQuestionDtoToQuestion.execute(questionDto))
        );
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/{courseId}/modules/{moduleId}/lessons/{lessonId}/questions/{questionId}")
    public ResponseEntity<BaseViewModel<Void>> removeQuestionFromQuiz(
            @PathVariable String courseId,
            @PathVariable String moduleId,
            @PathVariable String lessonId,
            @PathVariable String questionId) {
        this.pipeline.send(
                new RemoveQuestionFromQuizCommand(
                        new IdsCourse(courseId, moduleId, lessonId, questionId))
        );
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{courseId}/modules/{moduleId}/lessons/{lessonId}/questions/{questionId}")
    public ResponseEntity<BaseViewModel<Question>> updateQuestionFromQuiz(
            @PathVariable String courseId,
            @PathVariable String moduleId,
            @PathVariable String lessonId,
            @PathVariable String questionId,
            @RequestBody QuestionDto questionDto) {
        var result = this.pipeline.send(
                new UpdateQuestionFromQuizCommand(
                        new IdsCourse(courseId, moduleId, lessonId, questionId),
                        FromQuestionDtoToQuestion.execute(questionDto)));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/{courseId}/modules/{moduleId}/lessons/{lessonId}/questions/{questionId}/answers")
    public ResponseEntity<BaseViewModel<Question>> addAnswerToQuestion(
            @PathVariable String courseId,
            @PathVariable String moduleId,
            @PathVariable String lessonId,
            @PathVariable String questionId,
            @RequestBody AnswerDto newAnswer) {
        var result = this.pipeline.send(
                new AddAnswerToQuestionCommand(
                        new IdsCourse(courseId, moduleId, lessonId, questionId),
                        new Answer(newAnswer.getAnswerText(), newAnswer.isCorrect())));
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PatchMapping("/{courseId}/modules/{moduleId}/lessons/{lessonId}/questions/{questionId}/answers/{answerId}")
    public ResponseEntity<BaseViewModel<Answer>> updateAnswerFromQuestion(
            @PathVariable String courseId,
            @PathVariable String moduleId,
            @PathVariable String lessonId,
            @PathVariable String questionId,
            @PathVariable String answerId,
            @RequestBody AnswerDto newAnswer) {
        var result = this.pipeline.send(
                new UpdateAnswerFromQuestionCommand(
                        new IdsCourse(courseId, moduleId, lessonId, questionId, answerId),
                        newAnswer.getAnswerText(),
                        newAnswer.isCorrect()));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{courseId}/modules/{moduleId}/lessons/{lessonId}/questions/{questionId}/answers/{answerId}")
    public ResponseEntity<BaseViewModel<Void>> removeAnswerFromQuestion(
            @PathVariable String courseId,
            @PathVariable String moduleId,
            @PathVariable String lessonId,
            @PathVariable String questionId,
            @PathVariable String answerId) {
        this.pipeline.send(
                new RemoveAnswerFromQuestionCommand(
                        new IdsCourse(courseId, moduleId, lessonId, questionId, answerId)));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{courseId}/modules/{moduleId}/lessons/{lessonId}/quiz")
    public ResponseEntity<BaseViewModel<Quiz>> updateQuizFromLesson(
            @PathVariable String courseId,
            @PathVariable String moduleId,
            @PathVariable String lessonId,
            @RequestBody QuizDto newQuiz) {
        var result = this.pipeline.send(
                new UpdateQuizFromLessonCommand(
                        new IdsCourse(courseId, moduleId, lessonId),
                        new Quiz(newQuiz.getQuizTitle(), newQuiz.getQuizDescription(), newQuiz.getPassingScore())));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{courseId}/modules/{moduleId}/lessons/{lessonId}/quiz")
    public ResponseEntity<BaseViewModel<Void>> detachQuizFromLesson(
            @PathVariable String courseId,
            @PathVariable String moduleId,
            @PathVariable String lessonId) {
        this.pipeline.send(
                new DetachQuizFromLessonCommand(
                        new IdsCourse(courseId, moduleId, lessonId)));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
