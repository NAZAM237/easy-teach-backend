package fr.cleanarchitecture.easyteach.course.domain.model;

import java.util.UUID;

public class Module {
    private String id;
    private int position;

    public Module() {
        this.id = UUID.randomUUID().toString();
    }

    public Module(int position) {
        this.id = UUID.randomUUID().toString();
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }
}
