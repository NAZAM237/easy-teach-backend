package fr.cleanarchitecture.easyteach.course.infrastructure.spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseDto {
    private String title;
    private String description;
    private BigDecimal amount;
    private String currency;
}
