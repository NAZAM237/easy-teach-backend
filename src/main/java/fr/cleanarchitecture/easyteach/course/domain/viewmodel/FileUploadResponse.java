package fr.cleanarchitecture.easyteach.course.domain.viewmodel;

public class FileUploadResponse {
    private String fileName;
    private String fileType;
    private long fileSize;
    private String filePath;

    public FileUploadResponse() {}

    public FileUploadResponse(String fileName, String fileType, long fileSize, String filePath) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getFilePath() {
        return filePath;
    }
}
