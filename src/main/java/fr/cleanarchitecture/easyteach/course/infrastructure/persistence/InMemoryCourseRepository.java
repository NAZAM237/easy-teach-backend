package fr.cleanarchitecture.easyteach.course.infrastructure.persistence;

import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class InMemoryCourseRepository implements CourseRepository {

    private Set<Course> courses = new HashSet<>();

    @Override
    public Optional<Course> findById(String id) {
        return courses.stream().filter(course -> course.getCourseId().equals(id)).findFirst();
    }

    @Override
    public List<Course> findAll() {
        return this.courses.stream().toList();
    }

    @Override
    public void save(Course course) {
        this.courses.add(course);
    }

    @Override
    public void delete(Course course) {
        this.courses.remove(course);
    }
}
