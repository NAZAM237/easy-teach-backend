package fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory;

import fr.cleanarchitecture.easyteach.course.application.ports.ModuleRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryModuleRepository implements ModuleRepository {

    List<Module> modules = new ArrayList<>();

    @Override
    public Optional<Module> findByModuleId(String moduleId) {
        return modules.stream().filter(module -> module.getModuleId().equals(moduleId)).findFirst();
    }

    @Override
    public Optional<Module> findByTitle(String title) {
        return modules.stream().filter(module -> module.getModuleTitle().equals(title)).findFirst();
    }

    @Override
    public List<Module> findAll() {
        return modules;
    }

    @Override
    public void save(Module module) {
        modules.add(module);
    }

    @Override
    public void delete(Module module) {
        modules.remove(module);
    }
}
