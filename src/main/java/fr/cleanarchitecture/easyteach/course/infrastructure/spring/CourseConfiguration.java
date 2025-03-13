package fr.cleanarchitecture.easyteach.course.infrastructure.spring;

import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.ports.UploadFunctions;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryResourceUpload;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CourseConfiguration {

    @Bean
    public CourseRepository courseRepository() {
        return new InMemoryCourseRepository();
    }

    @Bean
    public UploadFunctions uploadFunctions(FileUploadProperties fileUploadProperties) { return new InMemoryResourceUpload(fileUploadProperties);}
}
