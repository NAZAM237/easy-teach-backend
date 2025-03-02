package fr.cleanarchitecture.easyteach.course.infrastructure.spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddModuleToCourseDto {
    private String moduleTitle;
    private String moduleDescription;
    private int moduleOrder;
}
