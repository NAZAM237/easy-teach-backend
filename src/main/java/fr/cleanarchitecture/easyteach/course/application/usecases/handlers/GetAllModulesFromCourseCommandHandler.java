package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.GetAllModulesFromCourseCommand;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.ModuleFromCourseViewModel;
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.NotFoundException;

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
