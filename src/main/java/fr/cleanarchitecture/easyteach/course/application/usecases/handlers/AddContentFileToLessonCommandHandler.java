package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.ports.FileFunctions;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.AddContentFileToLessonCommand;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.FileUploadResponse;

import java.io.IOException;

public class AddContentFileToLessonCommandHandler implements Command.Handler<AddContentFileToLessonCommand, FileUploadResponse> {

    private CourseRepository courseRepository;
    private FileFunctions fileFunctions;

    public AddContentFileToLessonCommandHandler(CourseRepository courseRepository, FileFunctions fileFunctions) {
        this.courseRepository = courseRepository;
        this.fileFunctions = fileFunctions;
    }

    @Override
    public FileUploadResponse handle(AddContentFileToLessonCommand addContentFileToLessonCommand) {
        var course = courseRepository.findByCourseId(addContentFileToLessonCommand.getIdsCourse().getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        var module = course.getModules().stream().filter(module1 -> module1.getModuleId().equals(addContentFileToLessonCommand.getIdsCourse().getModuleId()))
                .findFirst().orElseThrow(() -> new NotFoundException("Module not found"));
        var lesson = module.getLessons().stream().filter(lesson1 -> lesson1.getLessonId().equals(addContentFileToLessonCommand.getIdsCourse().getLessonId()))
                .findFirst().orElseThrow(() -> new NotFoundException("Lesson not found"));

        FileUploadResponse uploadResponse;
        try {
            uploadResponse = fileFunctions.uploadLessonContentFile(
                    addContentFileToLessonCommand.getFile(),
                    addContentFileToLessonCommand.getLessonContentType());
        } catch (IOException e) {
            throw new BadRequestException("Failed to store file: " + e.getMessage());
        }
        lesson.updateContentFileUrl(uploadResponse.getFilePath());
        courseRepository.save(course);
        return uploadResponse;
    }
}
