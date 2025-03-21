package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.viewmodel.BaseViewModel;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.GetAllCoursesCommand;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;

import java.util.List;

public class GetAllCoursesCommandHandler implements Command.Handler<GetAllCoursesCommand, BaseViewModel<List<Course>>> {

    private final CourseRepository courseRepository;

    public GetAllCoursesCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public BaseViewModel<List<Course>> handle(GetAllCoursesCommand getAllCoursesCommand) {
        var courses = courseRepository.findAll();
        return new BaseViewModel<>(courses);
    }
}
