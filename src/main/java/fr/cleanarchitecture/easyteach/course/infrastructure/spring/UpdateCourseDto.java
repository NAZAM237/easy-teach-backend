package fr.cleanarchitecture.easyteach.course.infrastructure.spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCourseDto {
    private String CourseTitle;
    private String CourseDescription;
    private BigDecimal CoursePrice;
    private String currency;
}
