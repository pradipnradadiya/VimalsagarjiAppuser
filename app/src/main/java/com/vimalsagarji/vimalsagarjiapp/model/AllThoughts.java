package com.vimalsagarji.vimalsagarjiapp.model;

/**
 * Created by BHARAT on 09/02/2016.
 */
@SuppressWarnings("ALL")
public class AllThoughts {
    private String id;
    private String title;
    private String description;
    private String date;
    private String view;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }
}
