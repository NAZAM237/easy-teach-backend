package fr.cleanarchitecture.easyteach.course.application.usecases.course;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.ModuleFromCourseViewModel;

public class GetAllModulesFromCourseCommandHandler implements Command.Handler<GetAllModulesFromCourseCommand, ModuleFromCourseViewModel> {

    private final CourseRepository courseRepository;

    public GetAllModulesFromCourseCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public ModuleFromCourseViewModel handle(GetAllModulesFromCourseCommand getAllModulesFromCourseCommand) {
        var course = courseRepository.findByCourseId(getAllModulesFromCourseCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        var modules = course.getModules();
        return new ModuleFromCourseViewModel(course.getCourseId(), modules);
    }
}
