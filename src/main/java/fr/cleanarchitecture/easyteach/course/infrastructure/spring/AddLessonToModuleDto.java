package fr.cleanarchitecture.easyteach.course.infrastructure.spring;

import fr.cleanarchitecture.easyteach.course.domain.enums.LessonType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddLessonToModuleDto {
    private String title;
    private LessonType lessonType;
    private String videoUrl;
    private String textContent;
    private int order;
}
