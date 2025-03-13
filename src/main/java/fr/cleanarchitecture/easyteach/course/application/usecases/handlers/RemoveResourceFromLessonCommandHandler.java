package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.ports.FileFunctions;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.RemoveResourceFromLessonCommand;

import java.io.IOException;

public class RemoveResourceFromLessonCommandHandler implements Command.Handler<RemoveResourceFromLessonCommand, Void> {

    private final CourseRepository courseRepository;
    private final FileFunctions fileFunctions;

    public RemoveResourceFromLessonCommandHandler(CourseRepository courseRepository, FileFunctions fileFunctions) {
        this.courseRepository = courseRepository;
        this.fileFunctions = fileFunctions;
    }

    @Override
    public Void handle(RemoveResourceFromLessonCommand removeResourceFromLessonCommand) {
        var course = courseRepository.findByCourseId(removeResourceFromLessonCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        var module = course.getModules().stream().filter(module1 -> module1.getModuleId().equals(removeResourceFromLessonCommand.getModuleId()))
                .findFirst().orElseThrow(() -> new NotFoundException("Module not found"));
        var lesson = module.getLessons().stream().filter(lesson1 -> lesson1.getLessonId().equals(removeResourceFromLessonCommand.getLessonId()))
                .findFirst().orElseThrow(() -> new NotFoundException("Lesson not found"));
        var resource = lesson.getResources().stream().filter(resource1 -> resource1.getResourceId().equals(removeResourceFromLessonCommand.getResourceId()))
                .findFirst().orElseThrow(() -> new NotFoundException("Resource not found"));

        try {
            fileFunctions.deleteFile(resource.getResourceUrl());
        } catch (IOException e) {
            throw new BadRequestException("Unable to delete file. File not found!");
        }
        lesson.removeResource(resource.getResourceId());
        courseRepository.save(course);
        return null;
    }
}
