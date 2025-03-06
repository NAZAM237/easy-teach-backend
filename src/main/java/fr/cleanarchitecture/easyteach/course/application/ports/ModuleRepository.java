package fr.cleanarchitecture.easyteach.course.application.ports;

import fr.cleanarchitecture.easyteach.course.domain.model.Module;

import java.util.List;
import java.util.Optional;

public interface ModuleRepository {
    Optional<Module> findByModuleId(String moduleId);
    Optional<Module> findByTitle(String title);
    List<Module> findAll();
    void save(Module module);
    void delete(Module module);
    void clear();
}
