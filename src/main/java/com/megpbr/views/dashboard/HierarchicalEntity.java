package com.megpbr.views.dashboard;

public class HierarchicalEntity {
    private String name;
    private Object entity;

    public HierarchicalEntity(String name, Object entity) {
        this.name = name;
        this.entity = entity;
    }

    public String getName() {
        return name;
    }

    public Object getEntity() {
        return entity;
    }

    @Override
    public String toString() {
        return name;
    }
}