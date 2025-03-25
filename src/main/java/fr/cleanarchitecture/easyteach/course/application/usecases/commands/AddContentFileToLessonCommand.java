package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.FileUploadResponse;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.IdsCourse;
import fr.cleanarchitecture.easyteach.shared.domain.viewmodel.BaseViewModel;
import org.springframework.web.multipart.MultipartFile;

public class AddContentFileToLessonCommand implements Command<BaseViewModel<FileUploadResponse>> {
    private final IdsCourse idsCourse;
    private final MultipartFile file;
    private final String lessonContentType;

    public AddContentFileToLessonCommand(IdsCourse idsCourse, MultipartFile file, String lessonContentType) {
        this.idsCourse = idsCourse;
        this.file = file;
        this.lessonContentType = lessonContentType;
    }

    public IdsCourse getIdsCourse() {
        return idsCourse;
    }

    public MultipartFile getFile() {
        return file;
    }

    public String getLessonContentType() {
        return lessonContentType;
    }
}
