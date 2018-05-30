package com.vimalsagarji.vimalsagarjiapp.model;

public class EventCategoryItem {

    private String id;
    private String name;
    private String categoryicon;


    public EventCategoryItem(String id, String name, String categoryicon) {
        this.id = id;
        this.name = name;
        this.categoryicon = categoryicon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryicon() {
        return categoryicon;
    }

    public void setCategoryicon(String categoryicon) {
        this.categoryicon = categoryicon;
    }
}
