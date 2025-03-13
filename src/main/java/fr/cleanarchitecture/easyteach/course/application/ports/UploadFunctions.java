package fr.cleanarchitecture.easyteach.course.application.ports;

import fr.cleanarchitecture.easyteach.course.domain.viewmodel.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFunctions {
    FileUploadResponse uploadFile(MultipartFile file, String resourceType);
}
