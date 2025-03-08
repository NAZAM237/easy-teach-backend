package fr.cleanarchitecture.easyteach.course.infrastructure.spring;

import an.awesome.pipelinr.Pipeline;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.*;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.InputLesson;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.GetCourseViewModel;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.ModuleFromCourseViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("courses")
public class CourseController {

    private final Pipeline pipeline;

    public CourseController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @GetMapping("/")
    public ResponseEntity<List<GetCourseViewModel>> getAllCourses() {
        var result = this.pipeline.send(new GetAllCoursesCommand());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<GetCourseViewModel> getCourseById(@PathVariable String courseId) {
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
    public ResponseEntity<CourseViewModel> createCourse(@RequestBody CreateCourseDto course) {
        var result = this.pipeline.send(
                new CreateCourseCommand(course.getTitle(), course.getDescription(), course.getTeacherUuid(), new Price(course.getAmount(), course.getCurrency())));
        return new ResponseEntity<>(new CourseViewModel(result.getMessage(), result.getCourse()),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{courseId}")
    public ResponseEntity<CourseViewModel> updateCourse(@RequestBody UpdateCourseDto course, @PathVariable String courseId) {
        var result = this.pipeline.send(
                new UpdateCourseCommand(courseId, course.getCourseTitle(), course.getCourseDescription(), new Price(course.getCoursePrice(), course.getCurrency())));
        return new ResponseEntity<>(new CourseViewModel(result.getMessage(), result.getCourse()),
                HttpStatus.OK);
    }

    @PatchMapping("/{courseId}/publish")
    public ResponseEntity<CourseViewModel> publishCourse(@PathVariable String courseId) {
        var result = this.pipeline.send(new PublishCourseCommand(courseId));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/{courseId}/archive")
    public ResponseEntity<CourseViewModel> archiveCourse(@PathVariable String courseId) {
        var result = this.pipeline.send(new ArchiveCourseCommand(courseId));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/{courseId}/restore")
    public ResponseEntity<CourseViewModel> restoreCourse(@PathVariable String courseId) {
        var result = this.pipeline.send(new RestoreCourseCommand(courseId));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/{courseId}/add-module-to-course")
    public ResponseEntity<Void> addModuleToCourse(@PathVariable String courseId, @RequestBody AddModuleToCourseDto module) {
        this.pipeline.send(
                new AddModuleToCourseCommand(
                        courseId,
                        module.getModuleTitle(),
                        module.getModuleDescription(),
                        module.getModuleOrder()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{courseId}/remove-module-from-course")
    public ResponseEntity<Void> removeModuleFromCourse(@PathVariable String courseId, @RequestBody RemoveModuleFromCourseDto module) {
        this.pipeline.send(
                new RemoveModuleFromCourseCommand(
                        courseId,
                        module.getModuleId()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String courseId) {
        this.pipeline.send(new DeleteCourseCommand(courseId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{courseId}/modules/{moduleId}/lessons")
    public ResponseEntity<CourseViewModel> reorderLessonsFromModule(
            @PathVariable String courseId,
            @PathVariable String moduleId,
            @RequestBody ReorderLessonToModuleDto reOrderLessonToModuleDto) {
        var result = this.pipeline.send(
                new ReorderLessonFromModuleCommand(courseId, moduleId, reOrderLessonToModuleDto.getLessons())
        );
        return ResponseEntity.ok(result);
    }

    @PatchMapping("{courseId}/modules/{moduleId}/add-lesson-to-module")
    public ResponseEntity<CourseViewModel> addLessonToModule(
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
                                addLessonToModuleDto.getVideoUrl(),
                                addLessonToModuleDto.getTextContent(),
                                addLessonToModuleDto.getOrder()))
        );
        return ResponseEntity.ok(result);
    }

    @PatchMapping("{courseId}/modules/{moduleId}/remove-lesson-from-module")
    public ResponseEntity<Void> removeLessonFromModule(
            @PathVariable String courseId,
            @PathVariable String moduleId,
            @RequestBody RemoveLessonFromModuleDto removeLessonFromModuleDto) {
        this.pipeline.send(
                new RemoveLessonFromModuleCommand(
                        courseId,
                        moduleId,
                        removeLessonFromModuleDto.getLessonId()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{courseId}/modules/{moduleId}")
    public ResponseEntity<CourseViewModel> updateModule(
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
    public ResponseEntity<CourseViewModel> reorderModulesFromCourse(
            @PathVariable String courseId,
            @RequestBody List<Module> newModules) {
        var result = this.pipeline.send(
                new ReorderModuleInCourseCommand(
                        courseId,
                        newModules));
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{courseId}/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<CourseViewModel> updateLessonFromModule(
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
                                updateLessonFromModuleDto.getVideoUrl(),
                                updateLessonFromModuleDto.getLessonTextContent()
                        )));
        return ResponseEntity.ok(result);
    }
}
