package fr.cleanarchitecture.easyteach.course.domain.model;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Module {
    private String moduleId;
    private String moduleTitle;
    private String moduleDescription;
    private int order;
    private Set<Lesson> lessons = new HashSet<>();

    public Module(String moduleTitle, String moduleDescription, int order) {
        this.moduleId = UUID.randomUUID().toString();
        this.moduleTitle = moduleTitle;
        this.moduleDescription = moduleDescription;
        this.order = order;
    }

    public Module() {}

    public String getModuleId() {
        return moduleId;
    }

    public int getOrder() {
        return order;
    }

    public String getModuleTitle() {
        return moduleTitle;
    }

    public String getModuleDescription() {
        return moduleDescription;
    }

    public Set<Lesson> getLessons() {
        return lessons;
    }

    public void updateData(String moduleTitle, String moduleDescription) {
        this.moduleTitle = moduleTitle;
        this.moduleDescription = moduleDescription;
    }

    public void addLesson(Lesson lesson) {
        var lessonWithSamePosition = this.lessons.stream().anyMatch(l -> l.getOrder() == lesson.getOrder());

        if (lessonWithSamePosition) {
            throw new BadRequestException("The lesson position already in use");
        }
        this.lessons.add(lesson);
    }

    public void removeLesson(String lessonId) throws BadRequestException {
        var lesson = this.lessons.stream().filter(lesson1 -> lesson1.getLessonId().equals(lessonId)).findFirst();
        if (lesson.isEmpty()) {
            throw new NotFoundException("The lesson not found");
        }
        this.lessons.remove(lesson.get());
    }

    public void reorderLessons(List<Lesson> lessons) {
        if (!this.lessons.containsAll(lessons) || lessons.size() != this.lessons.size()) {
            throw new BadRequestException("Invalid lessons list");
        }
        for (int i = 0; i < lessons.size(); i++) {
            lessons.get(i).changeOrder(i + 1);
        }

        this.lessons = new HashSet<>(lessons);
    }

    public void changeOrder(int newOrder) {
        this.order = newOrder;
    }
}
