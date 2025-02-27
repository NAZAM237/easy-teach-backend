package fr.cleanarchitecture.easyteach.course.infrastructure.spring;

import fr.cleanarchitecture.easyteach.authentication.application.ports.UserRepository;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.CreateCourseCommandHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CourseCommandHandlerConfiguration {

    @Bean
    public CreateCourseCommandHandler createCourseCommandHandler(CourseRepository courseRepository, UserRepository userRepository) {
        return new CreateCourseCommandHandler(courseRepository, userRepository);
    }
}
