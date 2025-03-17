package fr.cleanarchitecture.easyteach.course.infrastructure.spring.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@Data
public class FileUploadPropertiesConfiguration {
    @NotBlank
    @Value("${app.upload.dir}")
    private String dir;
    @NotNull
    private Map<String, String> allowed = Map.ofEntries(
            Map.entry("documents", "pdf,doc,docx,txt"),
            Map.entry("audios", "mp3,wav,ogg"),
            Map.entry("images", "jpg,jpeg,png,gif"),
            Map.entry("videos", "mp4,mov,avi")
    );
}
