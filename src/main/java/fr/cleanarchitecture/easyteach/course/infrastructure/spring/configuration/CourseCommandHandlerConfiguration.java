package fr.cleanarchitecture.easyteach.course.infrastructure.spring.configuration;

import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.ports.FileFunctions;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CourseCommandHandlerConfiguration {

    @Bean
    public CreateCourseCommandHandler createCourseCommandHandler(CourseRepository courseRepository) {
        return new CreateCourseCommandHandler(courseRepository);
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

    @Bean
    public AddModuleToCourseCommandHandler addModuleToCourseCommandHandler(CourseRepository courseRepository) {
        return new AddModuleToCourseCommandHandler(courseRepository);
    }

    @Bean
    public RemoveModuleFromCourseCommandHandler removeModuleFromCourseCommandHandler(CourseRepository courseRepository) {
        return new RemoveModuleFromCourseCommandHandler(courseRepository);
    }

    @Bean
    public GetCourseByIdCommandHandler getCourseByIdCommandHandler(CourseRepository courseRepository) {
        return new GetCourseByIdCommandHandler(courseRepository);
    }

    @Bean
    public GetAllCoursesCommandHandler getAllCoursesCommandHandler(CourseRepository courseRepository) {
        return new GetAllCoursesCommandHandler(courseRepository);
    }

    @Bean
    public GetAllModulesFromCourseCommandHandler getAllModulesFromCourseCommandHandler(CourseRepository courseRepository) {
        return new GetAllModulesFromCourseCommandHandler(courseRepository);
    }

    @Bean
    public ReorderLessonFromModuleCommandHandler reorderLessonCommandHandler(CourseRepository courseRepository) {
        return new ReorderLessonFromModuleCommandHandler(courseRepository);
    }

    @Bean
    public AddLessonToModuleCommandHandler addLessonToModuleCommandHandler(CourseRepository courseRepository) {
        return new AddLessonToModuleCommandHandler(courseRepository);
    }

    @Bean
    public RemoveLessonFromModuleCommandHandler removeLessonFromModuleCommandHandler(CourseRepository courseRepository) {
        return new RemoveLessonFromModuleCommandHandler(courseRepository);
    }

    @Bean
    public UpdateModuleFromCourseCommandHandler updateModuleCommandHandler(CourseRepository courseRepository) {
        return new UpdateModuleFromCourseCommandHandler(courseRepository);
    }

    @Bean
    public ReorderModuleInCourseCommandHandler reorderModuleInCourseCommandHandler(CourseRepository courseRepository) {
        return new ReorderModuleInCourseCommandHandler(courseRepository);
    }

    @Bean
    public UpdateLessonFromModuleCommandHandler updateLessonFromModuleCommandHandler(CourseRepository courseRepository) {
        return new UpdateLessonFromModuleCommandHandler(courseRepository);
    }

    @Bean
    public AddResourceToLessonCommandHandler addResourceToLessonCommandHandler(CourseRepository courseRepository, FileFunctions fileFunctions) {
        return new AddResourceToLessonCommandHandler(courseRepository, fileFunctions);
    }

    @Bean
    public AddContentFileToLessonCommandHandler addContentFileToLessonCommandHandler(CourseRepository courseRepository, FileFunctions fileFunctions) {
        return new AddContentFileToLessonCommandHandler(courseRepository, fileFunctions);
    }

    @Bean
    public RemoveResourceFromLessonCommandHandler removeResourceFromLessonCommandHandler(CourseRepository courseRepository, FileFunctions fileFunctions) {
        return new RemoveResourceFromLessonCommandHandler(courseRepository, fileFunctions);
    }

    @Bean
    public AttachQuizToLessonCommandHandler attachQuizToLessonCommandHandler(CourseRepository courseRepository) {
        return new AttachQuizToLessonCommandHandler(courseRepository);
    }

    @Bean
    public AddQuestionToQuizCommandHandler addQuestionToQuizCommandHandler(CourseRepository courseRepository) {
        return new AddQuestionToQuizCommandHandler(courseRepository);
    }

    @Bean
    public RemoveQuestionFromQuizCommandHandler removeQuestionFromQuizCommandHandler(CourseRepository courseRepository) {
        return new RemoveQuestionFromQuizCommandHandler(courseRepository);
    }

    @Bean
    public UpdateQuestionFromQuizCommandHandler updateQuestionFromQuizCommandHandler(CourseRepository courseRepository) {
        return new UpdateQuestionFromQuizCommandHandler(courseRepository);
    }

    @Bean
    public AddAnswerToQuestionCommandHandler addAnswerToQuestionCommandHandler(CourseRepository courseRepository) {
        return new AddAnswerToQuestionCommandHandler(courseRepository);
    }

    @Bean
    public UpdateAnswerFromQuestionCommandHandler updateAnswerFromQuestionCommandHandler(CourseRepository courseRepository) {
        return new UpdateAnswerFromQuestionCommandHandler(courseRepository);
    }
}
