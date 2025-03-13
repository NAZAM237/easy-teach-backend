package fr.cleanarchitecture.easyteach.course.domain.enums;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;

public enum ResourceType {
    VIDEOS, DOCUMENTS, AUDIOS, IMAGES;

    public static ResourceType getResourceType(String resourceType) {
        return switch (resourceType) {
            case "VIDEOS"-> ResourceType.VIDEOS;
            case "IMAGES" -> ResourceType.IMAGES;
            case "AUDIOS" -> ResourceType.AUDIOS;
            case "DOCUMENTS" -> ResourceType.DOCUMENTS;
            default -> throw new BadRequestException("Resource type not recognized");
        };
    }
}
