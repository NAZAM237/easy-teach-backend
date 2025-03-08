package fr.cleanarchitecture.easyteach.course.domain.model;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.domain.enums.CourseStatus;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Course {
    private String courseId;
    private String courseTitle;
    private String courseDescription;
    private Teacher teacher;
    private Price price;
    private CourseStatus status;
    private Set<Module> modules = new HashSet<>();

    public Course() {}

    public Course(String courseTitle, String courseDescription, Teacher teacher, Price price) {
        this.courseId = UUID.randomUUID().toString();
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.teacher = teacher;
        this.price = price;
        this.status = CourseStatus.DRAFT;
    }

    public Course(String courseId, String courseTitle, String courseDescription,
                  Teacher teacher, Price price, String status, Set<Module> modules) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.teacher = teacher;
        this.price = price;
        this.status = CourseStatus.fromStringToEnum(status);
        this.modules = modules;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Price getPrice() {
        return price;
    }
    
    public CourseStatus getStatus() {
        return status;
    }

    public Set<Module> getModules() {
        return modules;
    }

    public Course publish() {
        if (this.modules.isEmpty()) {
            throw new BadRequestException("You must provide at least one module");
        }

        var aLeastOnLesson = this.modules.stream().noneMatch(module -> module.getLessons().isEmpty());

        if (!aLeastOnLesson) {
            throw new BadRequestException("You must provide at least one lesson");
        }

        this.status = CourseStatus.PUBLISHED;
        return this;
    }

    public Course archive() {
        if (!CourseStatus.PUBLISHED.equals(status)) {
            throw new BadRequestException("The status of the course is not published");
        }
        this.status = CourseStatus.ARCHIVED;
        return this;
    }

    public Course restore() {
        if (!CourseStatus.ARCHIVED.equals(status)) {
            throw new BadRequestException("The status of the course is not archived. You cannot restore it");
        }
        this.status = CourseStatus.DRAFT;
        return this;
    }

    public void addModule(Module module) {
        var moduleWithSamePosition = this.modules.stream().anyMatch(m -> m.getOrder() == module.getOrder());
        if (CourseStatus.ARCHIVED.equals(this.status)) {
            throw new BadRequestException("You cannot add module to archived course. Please restore course before!");
        }
        if (moduleWithSamePosition) {
            throw new BadRequestException("The module position already in use");
        }
        this.modules.add(module);
    }

    public void removeModule(String moduleId) throws BadRequestException {
        var module = this.modules.stream()
                .filter(module1 -> module1.getModuleId().equals(moduleId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Module not found"));
        this.modules.remove(module);
    }

    public void updateCourseData(String newTitle, String newDescription, Price newPrice) {
        this.courseTitle = newTitle;
        this.courseDescription = newDescription;
        this.price = newPrice;
    }

    public void addLessonToModule(String moduleId, Lesson lesson) {
        var module = this.modules.stream()
                .filter(module1 -> module1.getModuleId().equals(moduleId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Module not found"));
        module.addLesson(lesson);
    }

    public void removeLessonToModule(String moduleId, String lessonId) {
        var module = this.modules.stream()
                .filter(module1 -> module1.getModuleId().equals(moduleId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Module not found"));
        module.removeLesson(lessonId);
    }

    public void reorderLessonsFromModule(String moduleId, List<Lesson> lessons) {
        var module = this.modules.stream()
                .filter(module1 -> module1.getModuleId().equals(moduleId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Module not found"));
        module.reorderLessons(lessons);
    }

    public void updateModuleData(String moduleId, String moduleTitle, String moduleDescription) {
        var module = this.modules.stream()
                .filter(module1 -> module1.getModuleId().equals(moduleId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Module not found"));
        module.updateData(moduleTitle, moduleDescription);
    }
}
