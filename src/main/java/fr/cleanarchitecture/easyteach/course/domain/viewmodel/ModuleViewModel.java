package fr.cleanarchitecture.easyteach.course.domain.viewmodel;

import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;

import java.util.Set;

public class ModuleViewModel {
    private String moduleId;
    private String moduleTitle;
    private String moduleDescription;
    private int moduleOrder;
    private Set<Lesson> lessons;

    public ModuleViewModel() {}

    public ModuleViewModel(Module module) {
        this.moduleId = module.getModuleId();
        this.moduleTitle = module.getModuleTitle();
        this.moduleDescription = module.getModuleDescription();
        this.moduleOrder = module.getOrder();
        this.lessons = module.getLessons();
    }

    public String getModuleId() {
        return moduleId;
    }

    public String getModuleTitle() {
        return moduleTitle;
    }

    public String getModuleDescription() {
        return moduleDescription;
    }

    public int getModuleOrder() {
        return moduleOrder;
    }

    public Set<Lesson> getLessons() {
        return lessons;
    }
}
