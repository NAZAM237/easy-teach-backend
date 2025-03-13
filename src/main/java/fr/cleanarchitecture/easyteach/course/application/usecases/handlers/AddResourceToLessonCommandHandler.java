package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.ports.FileFunctions;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.AddResourceToLessonCommand;
import fr.cleanarchitecture.easyteach.course.domain.model.Resource;

public class AddResourceToLessonCommandHandler implements Command.Handler<AddResourceToLessonCommand, Void> {

    private final CourseRepository courseRepository;
    private final FileFunctions fileFunctions;

    public AddResourceToLessonCommandHandler(CourseRepository courseRepository, FileFunctions fileFunctions) {
        this.courseRepository = courseRepository;
        this.fileFunctions = fileFunctions;
    }

    @Override
    public Void handle(AddResourceToLessonCommand addResourceToLessonCommand) {
        var course = courseRepository.findByCourseId(addResourceToLessonCommand.getIdsCourse().getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        var module = course.getModules().stream().filter(module1 -> module1.getModuleId().equals(addResourceToLessonCommand.getIdsCourse().getModuleId()))
                .findFirst().orElseThrow(() -> new NotFoundException("Module not found"));
        var lesson = module.getLessons().stream().filter(lesson1 -> lesson1.getLessonId().equals(addResourceToLessonCommand.getIdsCourse().getLessonId()))
                .findFirst().orElseThrow(() -> new NotFoundException("Lesson not found"));

        var uploadResponse = fileFunctions.uploadFile(
                addResourceToLessonCommand.getFile(),
                addResourceToLessonCommand.getResourceType());
        var resource = new Resource(uploadResponse.getFileName(), uploadResponse.getFilePath());
        lesson.addResource(resource);
        courseRepository.save(course);
        return null;
    }
}
