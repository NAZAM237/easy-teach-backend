package fr.cleanarchitecture.easyteach.course.infrastructure.spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoveLessonFromModuleDto {
    private String lessonId;
}
