package fr.cleanarchitecture.easyteach.course.application.ports;

import fr.cleanarchitecture.easyteach.course.domain.viewmodel.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileFunctions {
    FileUploadResponse uploadResourceFile(MultipartFile file, String resourceType) throws IOException;
    FileUploadResponse uploadLessonContentFile(MultipartFile file, String lessonType) throws IOException;
    void deleteFile(String resourceUrl) throws IOException;
}
