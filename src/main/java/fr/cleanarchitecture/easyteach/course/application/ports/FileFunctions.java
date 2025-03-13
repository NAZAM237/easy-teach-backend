package fr.cleanarchitecture.easyteach.course.application.ports;

import fr.cleanarchitecture.easyteach.course.domain.viewmodel.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileFunctions {
    FileUploadResponse uploadFile(MultipartFile file, String resourceType);

    void deleteFile(String resourceUrl) throws IOException;
}
