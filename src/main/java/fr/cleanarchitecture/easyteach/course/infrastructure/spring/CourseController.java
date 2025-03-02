package fr.cleanarchitecture.easyteach.course.infrastructure.spring;

import an.awesome.pipelinr.Pipeline;
import fr.cleanarchitecture.easyteach.course.application.usecases.*;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.GetCourseViewModel;
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

    @GetMapping("/{courseId}")
    public ResponseEntity<GetCourseViewModel> getCourseById(@PathVariable String courseId) {
        var result = this.pipeline.send(
                new GetCourseByIdCommand(courseId)
        );
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/")
    public ResponseEntity<List<GetCourseViewModel>> getAllCourses() {
        var result = this.pipeline.send(new GetAllCoursesCommand());
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
}
