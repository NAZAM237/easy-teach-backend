package fr.cleanarchitecture.easyteach.course.infrastructure.spring;

import an.awesome.pipelinr.Pipeline;
import fr.cleanarchitecture.easyteach.course.application.usecases.CreateCourseCommand;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("courses")
public class CourseController {

    private final Pipeline pipeline;

    public CourseController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping
    public ResponseEntity<CourseViewModel> createCourse(@RequestBody CreateCourseDto course) {
        var result = this.pipeline.send(
                new CreateCourseCommand(course.getTitle(), course.getDescription(), course.getTeacherUuid(), new Price(course.getAmount(), course.getCurrency())));
        return new ResponseEntity<>(new CourseViewModel(result.getMessage(), result.getNewCourse()),
                HttpStatus.CREATED);
    }
}
