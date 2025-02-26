package fr.cleanarchitecture.easyteach.course.application.ports;

import fr.cleanarchitecture.easyteach.course.domain.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {

    Optional<Course> findById(String id);
    List<Course> findAll();
    void save(Course course);
    void delete(Course course);
}
