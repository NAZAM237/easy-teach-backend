package fr.cleanarchitecture.easyteach.course.infrastructure.spring.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoveLessonFromModuleDto {
    private String lessonId;
}
