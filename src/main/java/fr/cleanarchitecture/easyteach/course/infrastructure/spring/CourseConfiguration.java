package fr.cleanarchitecture.easyteach.course.infrastructure.spring;

import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.ports.FileFunctions;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryResourceFile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CourseConfiguration {

    @Bean
    public CourseRepository courseRepository() {
        return new InMemoryCourseRepository();
    }

    @Bean
    public FileFunctions uploadFunctions(FileUploadProperties fileUploadProperties) { return new InMemoryResourceFile(fileUploadProperties);}
}
