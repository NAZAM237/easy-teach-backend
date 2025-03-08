package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.UpdateModuleFromCourseCommand;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;

public class UpdateModuleFromCourseCommandHandler implements Command.Handler<UpdateModuleFromCourseCommand, CourseViewModel> {

    private CourseRepository courseRepository;

    public UpdateModuleFromCourseCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public CourseViewModel handle(UpdateModuleFromCourseCommand updateModuleFromCourseCommand) {
        var course = courseRepository.findByCourseId(updateModuleFromCourseCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        course.updateModuleData(
                updateModuleFromCourseCommand.getModuleId(),
                updateModuleFromCourseCommand.getModuleTitle(),
                updateModuleFromCourseCommand.getModuleDescription()
        );
        courseRepository.save(course);
        return new CourseViewModel(
                "Module " + updateModuleFromCourseCommand.getModuleId()+ "has been update successfully",
                course);
    }
}
