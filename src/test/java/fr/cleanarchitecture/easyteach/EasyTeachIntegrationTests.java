package fr.cleanarchitecture.easyteach;

import fr.cleanarchitecture.easyteach.course.infrastructure.spring.configuration.CourseConfiguration;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.configuration.FileUploadPropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@Import({PostgresSQLTestConfiguration.class,
        FileUploadPropertiesConfiguration.class,
        CourseConfiguration.class})
@SpringBootTest
@AutoConfigureMockMvc
public class EasyTeachIntegrationTests {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
}
