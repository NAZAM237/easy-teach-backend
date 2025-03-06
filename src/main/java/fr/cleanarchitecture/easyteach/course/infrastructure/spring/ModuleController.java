package fr.cleanarchitecture.easyteach.course.infrastructure.spring;

import an.awesome.pipelinr.Pipeline;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.LinkModuleToCourseCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.ReorderLessonCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.UnLinkModuleToCourseCommand;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.ModuleViewModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("modules")
public class ModuleController {

    private final Pipeline pipeline;

    public ModuleController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PatchMapping("/{modulesId}/courses/{courseId}/link")
    public ResponseEntity<ModuleViewModel> linkModuleToCourse(
            @PathVariable("modulesId") String modulesId, @PathVariable("courseId") String courseId) {
        var result = this.pipeline.send(
                new LinkModuleToCourseCommand(courseId, modulesId)
        );
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{modulesId}/courses/{courseId}/unlink")
    public ResponseEntity<ModuleViewModel> unLinkModuleToCourse(
            @PathVariable("modulesId") String modulesId, @PathVariable("courseId") String courseId) {
        var result = this.pipeline.send(
                new UnLinkModuleToCourseCommand(courseId, modulesId)
        );
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{moduleId}/lessons")
    public ResponseEntity<ModuleViewModel> reorderLessons(@PathVariable String moduleId, @RequestBody ReorderLessonToModuleDto reOrderLessonToModuleDto) {
        var result = this.pipeline.send(
                new ReorderLessonCommand(moduleId, reOrderLessonToModuleDto.getLessons())
        );
        return ResponseEntity.ok(result);
    }
}
