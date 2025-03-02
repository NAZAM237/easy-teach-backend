package fr.cleanarchitecture.easyteach.course.domain.viewmodel;

import fr.cleanarchitecture.easyteach.course.domain.model.Module;

import java.util.Set;

public class ModuleFromCourseViewModel {
    private String courseId;
    private Set<Module> modules;

    public ModuleFromCourseViewModel() {}

    public ModuleFromCourseViewModel(String courseId, Set<Module> modules) {
        this.courseId = courseId;
        this.modules = modules;
    }

    public String getCourseId() {
        return courseId;
    }

    public Set<Module> getModules() {
        return modules;
    }
}
