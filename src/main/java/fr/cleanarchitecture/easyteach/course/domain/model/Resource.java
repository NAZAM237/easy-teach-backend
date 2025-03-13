package fr.cleanarchitecture.easyteach.course.domain.model;

import java.util.UUID;

public class Resource {
    private String resourceId;
    private String resourceName;
    private String resourceUrl;

    public Resource(String resourceName, String resourceUrl) {
        this.resourceId = UUID.randomUUID().toString();
        this.resourceName = resourceName;
        this.resourceUrl = resourceUrl;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }
}
