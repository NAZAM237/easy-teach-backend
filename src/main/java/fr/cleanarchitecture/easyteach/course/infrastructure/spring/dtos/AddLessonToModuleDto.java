package fr.cleanarchitecture.easyteach.course.infrastructure.spring.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddLessonToModuleDto {
    private String title;
    private String lessonType;
    private String contentFileUrl;
    private String textContent;
    private int order;
}
