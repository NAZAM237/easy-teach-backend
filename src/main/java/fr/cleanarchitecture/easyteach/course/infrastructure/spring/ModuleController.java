package fr.cleanarchitecture.easyteach.course.infrastructure.spring;

import an.awesome.pipelinr.Pipeline;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.*;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.InputLesson;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.ModuleViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("modules")
public class ModuleController {

    private final Pipeline pipeline;

    public ModuleController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PatchMapping("/{moduleId}/lessons")
    public ResponseEntity<ModuleViewModel> reorderLessons(@PathVariable String moduleId, @RequestBody ReorderLessonToModuleDto reOrderLessonToModuleDto) {
        var result = this.pipeline.send(
                new ReorderLessonCommand(moduleId, reOrderLessonToModuleDto.getLessons())
        );
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{moduleId}/add-lesson-to-module")
    public ResponseEntity<ModuleViewModel> addLessonToModule(@PathVariable String moduleId, @RequestBody AddLessonToModuleDto addLessonToModuleDto) {
        var result = this.pipeline.send(
                new AddLessonToModuleCommand(
                        moduleId,
                        new InputLesson(
                                addLessonToModuleDto.getTitle(),
                                addLessonToModuleDto.getLessonType(),
                                addLessonToModuleDto.getVideoUrl(),
                                addLessonToModuleDto.getTextContent(),
                                addLessonToModuleDto.getOrder()))
        );
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{moduleId}/remove-lesson-from-module")
    public ResponseEntity<Void> removeLessonFromModule(@PathVariable String moduleId, @RequestBody RemoveLessonFromModuleDto removeLessonFromModuleDto) {
        this.pipeline.send(new RemoveLessonFromModuleCommand(moduleId, removeLessonFromModuleDto.getLessonId()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{moduleId}")
    public ResponseEntity<ModuleViewModel> updateModule(@PathVariable String moduleId, @RequestBody UpdateModuleDto updateModuleDto) {
        var result = this.pipeline.send(new UpdateModuleCommand(moduleId, updateModuleDto.getModuleTitle(), updateModuleDto.getModuleDescription()));
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{moduleId}")
    public ResponseEntity<Void> deleteModule(@PathVariable String moduleId) {
        this.pipeline.send(new DeleteModuleCommand(moduleId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
