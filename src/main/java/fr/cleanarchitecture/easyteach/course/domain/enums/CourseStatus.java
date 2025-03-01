package fr.cleanarchitecture.easyteach.course.domain.enums;

public enum CourseStatus {
    DRAFT,
    PUBLISHED,
    ARCHIVED;

    public static CourseStatus fromStringToEnum(String status) {
        return switch (status) {
            case "DRAFT"-> CourseStatus.DRAFT;
            case "PUBLISHED" -> CourseStatus.PUBLISHED;
            case "ARCHIVED" -> CourseStatus.ARCHIVED;
            default -> throw new IllegalArgumentException("Status not recognized");
        };
    }

    public static String fromEnumToString(CourseStatus status) {
        return switch (status) {
            case DRAFT -> "DRAFT";
            case PUBLISHED -> "PUBLISHED";
            case ARCHIVED -> "ARCHIVED";
            default -> throw new IllegalArgumentException("Status not recognized");
        };
    }
}
