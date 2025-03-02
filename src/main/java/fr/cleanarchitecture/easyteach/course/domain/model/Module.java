package fr.cleanarchitecture.easyteach.course.domain.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Module {
    private String moduleId;
    private String moduleTitle;
    private String moduleDescription;
    private int order;
    private Set<Lesson> lessons = new HashSet<>();
    private boolean linkToCourse;

    public Module(String moduleTitle, String moduleDescription, int order) {
        this.moduleId = UUID.randomUUID().toString();
        this.moduleTitle = moduleTitle;
        this.moduleDescription = moduleDescription;
        this.order = order;
    }

    public Module() {}

    public Module(int order) {
        this.moduleId = UUID.randomUUID().toString();
        this.order = order;
    }

    public Module(String moduleId, int order) {
        this.moduleId = moduleId;
        this.order = order;
    }

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

    public boolean isLinkToCourse() {
        return linkToCourse;
    }
}
