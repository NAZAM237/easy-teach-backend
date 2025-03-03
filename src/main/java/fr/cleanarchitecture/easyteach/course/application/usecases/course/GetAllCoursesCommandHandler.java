package fr.cleanarchitecture.easyteach.course.application.usecases.course;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.GetCourseViewModel;

import java.util.List;

public class GetAllCoursesCommandHandler implements Command.Handler<GetAllCoursesCommand, List<GetCourseViewModel>> {

    private final CourseRepository courseRepository;

    public GetAllCoursesCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<GetCourseViewModel> handle(GetAllCoursesCommand getAllCoursesCommand) {
        var courses = courseRepository.findAll();
        return courses
                .stream()
                .map(GetCourseViewModel::new)
                .toList();
    }
}
