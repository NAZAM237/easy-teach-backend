package fr.cleanarchitecture.easyteach.course.e2eTests.modules;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.ModuleRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
public class DeleteModuleE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private ModuleRepository moduleRepository;

    @Test
    public void shouldDeleteModuleE2ETest() throws Exception {
        var module = new Module("module title", "module description", 1);
        moduleRepository.save(module);

        mockMvc
                .perform(MockMvcRequestBuilders.delete("/modules/{moduleId}", module.getModuleId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteNotExistingModuleE2ETest_shouldThrowException() throws Exception {
        var module = new Module("module title", "module description", 1);

        mockMvc
                .perform(MockMvcRequestBuilders.delete("/modules/{moduleId}", module.getModuleId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
