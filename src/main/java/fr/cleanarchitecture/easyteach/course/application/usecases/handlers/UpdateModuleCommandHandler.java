package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.UpdateModuleCommand;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;

public class UpdateModuleCommandHandler implements Command.Handler<UpdateModuleCommand, CourseViewModel> {

    private CourseRepository courseRepository;

    public UpdateModuleCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public CourseViewModel handle(UpdateModuleCommand updateModuleCommand) {
        var course = courseRepository.findByCourseId(updateModuleCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        course.updateModuleData(
                updateModuleCommand.getModuleId(),
                updateModuleCommand.getModuleTitle(),
                updateModuleCommand.getModuleDescription()
        );
        courseRepository.save(course);
        return new CourseViewModel(
                "Module " +updateModuleCommand.getModuleId()+ "has been update successfully",
                course);
    }
}
