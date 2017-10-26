package com.vimalsagarji.vimalsagarjiapp.model;

/**
 * Created by Grapes-Pradip on 04-Oct-17.
 */

public class NotificationItem {
    private String table, id, title, description, date;

    public NotificationItem(String table, String id, String title, String description, String date) {
        this.table = table;
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

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
}
