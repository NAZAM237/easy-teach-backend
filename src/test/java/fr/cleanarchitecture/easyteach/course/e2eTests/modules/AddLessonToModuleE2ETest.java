package fr.cleanarchitecture.easyteach.course.e2eTests.modules;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.ModuleRepository;
import fr.cleanarchitecture.easyteach.course.domain.enums.LessonType;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.ModuleViewModel;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.AddLessonToModuleDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
public class AddLessonToModuleE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private ModuleRepository moduleRepository;

    @Test
    public void shouldAddLessonToModuleE2ETest() throws Exception {
        var module = new Module("title", "description", 1);
        moduleRepository.save(module);
        var dto = new AddLessonToModuleDto("title", LessonType.TEXT, "videoUrl", "textContent", 1);

        var result = mockMvc
                .perform(MockMvcRequestBuilders.patch("/modules/{moduleId}/add-lesson-to-module", module.getModuleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var moduleViewModel = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ModuleViewModel.class
        );

        Assert.assertNotNull(moduleViewModel);
        Assert.assertEquals(1, moduleViewModel.getLessons().size());
    }
}
