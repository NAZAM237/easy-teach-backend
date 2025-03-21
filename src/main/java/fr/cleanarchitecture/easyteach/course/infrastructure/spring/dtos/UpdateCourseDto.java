package fr.cleanarchitecture.easyteach.course.infrastructure.spring.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCourseDto {
    private String courseTitle;
    private String courseDescription;
    private BigDecimal coursePrice;
    private String currency;
}
