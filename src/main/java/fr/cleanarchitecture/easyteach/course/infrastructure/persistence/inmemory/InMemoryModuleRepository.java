package fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory;

import fr.cleanarchitecture.easyteach.course.application.ports.ModuleRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class InMemoryModuleRepository implements ModuleRepository {

    Set<Module> modules = new HashSet<>();

    @Override
    public Optional<Module> findByModuleId(String moduleId) {
        return this.modules.stream().filter(module -> module.getModuleId().equals(moduleId)).findFirst();
    }

    @Override
    public Optional<Module> findByTitle(String title) {
        return this.modules.stream().filter(module -> module.getModuleTitle().equals(title)).findFirst();
    }

    @Override
    public List<Module> findAll() {
        return this.modules.stream().toList();
    }

    @Override
    public void save(Module module) {
        this.modules.add(module);
    }

    @Override
    public void delete(Module module) {
        this.modules.remove(module);
    }

    @Override
    public void clear() {
        this.modules.clear();
    }
}
