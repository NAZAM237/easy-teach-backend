package fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.course.application.ports.FileFunctions;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.FileUploadResponse;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.configuration.FileUploadPropertiesConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public class InMemoryResourceFile implements FileFunctions {

    private final FileUploadPropertiesConfiguration properties;

    public InMemoryResourceFile(FileUploadPropertiesConfiguration properties) {
        this.properties = properties;
    }

    @Override
    public FileUploadResponse uploadFile(MultipartFile file, String resourceType) {
        validateFile(file, ResourceType.getResourceType(resourceType));

        String fileName = generateFileName(file);
        Path uploadPath = Paths.get(properties.getDir(), resourceType.toLowerCase());

        try {
            Files.createDirectories(uploadPath);
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return new FileUploadResponse(
                    fileName,
                    file.getContentType(),
                    file.getSize(),
                    filePath.toAbsolutePath().toString()
            );
        } catch (IOException ex) {
            throw new BadRequestException("Failed to store file: " + ex.getMessage());
        }
    }

    @Override
    public void deleteFile(String resourceUrl) throws IOException {
        Path path = Paths.get(resourceUrl);
        if (!Files.deleteIfExists(path)) {
            throw new BadRequestException("Unable to delete file " + path);
        }
    }

    private void validateFile(MultipartFile file, ResourceType fileType) {
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename).toLowerCase();
        String allowedExtensions = properties.getAllowed().get(fileType.name().toLowerCase());

        if (allowedExtensions == null) {
            throw new BadRequestException("Invalid file type category");
        }

        if (!Arrays.asList(allowedExtensions.split(",")).contains(fileExtension)) {
            throw new BadRequestException("Invalid file extension for category: " + fileType);
        }
    }

    private String generateFileName(MultipartFile file) {
        return file.getName() + "." + getFileExtension(file.getOriginalFilename());
    }

    private String getFileExtension(String fileName) {
        return fileName != null ?
                fileName.substring(fileName.lastIndexOf(".") + 1) : "";
    }
}
