package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.core.domain.viewmodel.BaseViewModel;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.ports.FileFunctions;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.AddResourceToLessonCommand;
import fr.cleanarchitecture.easyteach.course.domain.model.Resource;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.FileUploadResponse;

import java.io.IOException;

public class AddResourceToLessonCommandHandler implements Command.Handler<AddResourceToLessonCommand, BaseViewModel<Resource>> {

    private final CourseRepository courseRepository;
    private final FileFunctions fileFunctions;

    public AddResourceToLessonCommandHandler(CourseRepository courseRepository, FileFunctions fileFunctions) {
        this.courseRepository = courseRepository;
        this.fileFunctions = fileFunctions;
    }

    @Override
    public BaseViewModel<Resource> handle(AddResourceToLessonCommand addResourceToLessonCommand) {
        var course = courseRepository.findByCourseId(addResourceToLessonCommand.getIdsCourse().getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        var module = course.getModules().stream().filter(module1 -> module1.getModuleId().equals(addResourceToLessonCommand.getIdsCourse().getModuleId()))
                .findFirst().orElseThrow(() -> new NotFoundException("Module not found"));
        var lesson = module.getLessons().stream().filter(lesson1 -> lesson1.getLessonId().equals(addResourceToLessonCommand.getIdsCourse().getLessonId()))
                .findFirst().orElseThrow(() -> new NotFoundException("Lesson not found"));

        FileUploadResponse uploadResponse;
        try {
            uploadResponse = fileFunctions.uploadResourceFile(
                    addResourceToLessonCommand.getFile(),
                    addResourceToLessonCommand.getResourceType());
        } catch (IOException e) {
            throw new BadRequestException("Failed to store file: " + e.getMessage());
        }
        var resource = new Resource(uploadResponse.getFileName(), uploadResponse.getFilePath());
        lesson.addResource(resource);
        courseRepository.save(course);
        return new BaseViewModel<>(resource);
    }
}
