package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.model.Resource;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.IdsCourse;
import fr.cleanarchitecture.easyteach.shared.domain.viewmodel.BaseViewModel;
import org.springframework.web.multipart.MultipartFile;

public class AddResourceToLessonCommand implements Command<BaseViewModel<Resource>> {
    private IdsCourse idsCourse;
    private MultipartFile file;
    private String resourceType;

    public AddResourceToLessonCommand(IdsCourse idsCourse, MultipartFile file, String resourceType) {
        this.idsCourse = idsCourse;
        this.file = file;
        this.resourceType = resourceType;
    }

    public IdsCourse getIdsCourse() {
        return idsCourse;
    }

    public MultipartFile getFile() {
        return file;
    }

    public String getResourceType() {
        return resourceType;
    }
}
