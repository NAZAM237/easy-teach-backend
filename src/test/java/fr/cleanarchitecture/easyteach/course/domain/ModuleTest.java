package fr.cleanarchitecture.easyteach.course.domain;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import org.junit.Assert;
import org.junit.Test;

public class ModuleTest {

    @Test
    public void linkModuleToCourseTest() {
        var module = new Module("title", "description", 1);
        module.linkToCourse();
        Assert.assertTrue(module.isLinkToCourse());
    }

    @Test
    public void linkAlwaysLinkedToCourseTest_shouldThrowException() {
        var module = new Module("title", "description", 1);
        module.linkToCourse();
        Assert.assertThrows(
                "Module is always linked in a course",
                BadRequestException.class,
                module::linkToCourse
        );
    }

    @Test
    public void unLinkModuleToCourseTest() {
        var module = new Module("title", "description", 1);
        module.linkToCourse();
        module.unLinkToCourse();
        Assert.assertFalse(module.isLinkToCourse());
    }

    @Test
    public void unLinkAlwaysUnLinkedModuleToCourseTest_shouldThrowException() {
        var module = new Module("title", "description", 1);
        Assert.assertThrows(
                "Module is not linked to any course",
                BadRequestException.class,
                module::unLinkToCourse
        );
    }
}
