package fr.cleanarchitecture.easyteach.course.e2eTests.modules;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.ModuleRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.ModuleViewModel;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.UpdateModuleDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@RunWith(SpringRunner.class)
public class UpdateModuleE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private ModuleRepository moduleRepository;

    @Test
    public void shouldUpdateModuleE2ETest() throws Exception {
        var module = new Module("Module title", "module description", 1);
        moduleRepository.save(module);

        var updateModuleDto = new UpdateModuleDto("title", "description");

        var result = mockMvc
                .perform(MockMvcRequestBuilders.patch("/modules/{moduleId}", module.getModuleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(updateModuleDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var moduleViewModel = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ModuleViewModel.class);

        var updatedModule = moduleRepository.findByModuleId(module.getModuleId());

        Assert.assertTrue(updatedModule.isPresent());
        Assert.assertEquals(moduleViewModel.getModuleTitle(), updatedModule.get().getModuleTitle());
        Assert.assertEquals(moduleViewModel.getModuleDescription(), updatedModule.get().getModuleDescription());
    }

    @Test
    public void updateNotExistingModuleE2ETest_shouldThrowException() throws Exception {
        var updateModuleDto = new UpdateModuleDto("title", "description");

        mockMvc
            .perform(MockMvcRequestBuilders.patch("/modules/{moduleId}", "garbage")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(updateModuleDto)))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
