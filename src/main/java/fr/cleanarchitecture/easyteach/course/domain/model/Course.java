package fr.cleanarchitecture.easyteach.course.domain.model;

import fr.cleanarchitecture.easyteach.course.domain.enums.StatusEnum;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import org.apache.coyote.BadRequestException;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Course {
    private String courseId;
    private String courseTitle;
    private String courseDescription;
    private Instructor instructor;
    private Price price;
    private StatusEnum status;
    private Set<Module> modules;

    public Course(String courseTitle, String courseDescription, Instructor instructor, Price price) {
        this.courseId = UUID.randomUUID().toString();
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.instructor = instructor;
        this.price = price;
        this.status = StatusEnum.DRAFT;
        this.modules = new HashSet<>();
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }
    
    public StatusEnum getStatus() {
        return status;
    }

    public Set<Module> getModules() {
        return modules;
    }

    public void publish() {
        this.status = StatusEnum.PUBLISHED;
    }

    public void archive() {
        if (!StatusEnum.PUBLISHED.equals(status)) {
            throw new IllegalStateException("The status of the course is not published");
        }
        this.status = StatusEnum.ARCHIVED;
    }

    public void restore() {
        if (!StatusEnum.ARCHIVED.equals(status)) {
            throw new IllegalStateException("The status of the course is not archived. You cannot restore it");
        }
        this.status = StatusEnum.DRAFT;
    }

    public void addModule(Module module) {
        var moduleWithSamePosition = this.modules.stream().anyMatch(m -> m.getPosition() == module.getPosition());
        if (StatusEnum.ARCHIVED.equals(this.status)) {
            throw new IllegalStateException("You cannot add module to archived course. Please restore course before!");
        }
        if (moduleWithSamePosition) {
            throw new IllegalArgumentException("The module position already in use");
        }
        this.modules.add(module);
    }

    public void removeModule(String moduleId) throws BadRequestException {
        if (this.modules.stream().noneMatch(module -> module.getId().equals(moduleId))) {
            throw new BadRequestException("The module does not exist");
        }
        this.modules.removeIf(module -> module.getId().equals(moduleId));
    }
}
