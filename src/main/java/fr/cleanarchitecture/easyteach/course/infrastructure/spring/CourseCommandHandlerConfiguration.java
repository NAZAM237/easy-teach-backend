package fr.cleanarchitecture.easyteach.course.infrastructure.spring;

import fr.cleanarchitecture.easyteach.authentication.application.ports.UserRepository;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CourseCommandHandlerConfiguration {

    @Bean
    public CreateCourseCommandHandler createCourseCommandHandler(CourseRepository courseRepository, UserRepository userRepository) {
        return new CreateCourseCommandHandler(courseRepository, userRepository);
    }

    @Bean
    public UpdateCourseCommandHandler updateCourseCommandHandler(CourseRepository courseRepository) {
        return new UpdateCourseCommandHandler(courseRepository);
    }

    @Bean
    public DeleteCourseCommandHandler deleteCourseCommandHandler(CourseRepository courseRepository) {
        return new DeleteCourseCommandHandler(courseRepository);
    }

    @Bean
    public PublishCourseCommandHandler publishCourseCommandHandler(CourseRepository courseRepository) {
        return new PublishCourseCommandHandler(courseRepository);
    }

    @Bean
    public ArchiveCourseCommandHandler archiveCourseCommandHandler(CourseRepository courseRepository) {
        return new ArchiveCourseCommandHandler(courseRepository);
    }

    @Bean
    public RestoreCourseCommandHandler restoreCourseCommandHandler(CourseRepository courseRepository) {
        return new RestoreCourseCommandHandler(courseRepository);
    }
}
