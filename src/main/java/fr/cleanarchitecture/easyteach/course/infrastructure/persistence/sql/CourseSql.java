package fr.cleanarchitecture.easyteach.course.infrastructure.persistence.sql;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "courses")
@Entity
public class CourseSql {
    @Id
    private String id;
    private String title;
    private String description;
    private String teacherId;
}
