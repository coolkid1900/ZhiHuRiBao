package com.example.su.zhihuribao.Model;

/**
 * Created by su on 16/5/7.
 */
public class TopicsItem {

    private String id;
    private String name;
    private String thumbnail;
    private String description;


    public TopicsItem(String id, String name, String thumbnail, String description) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.description = description;
    }


    public String getThumbnail() {
        return thumbnail;
    }


    public String getDescription() {
        return description;
    }


    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }
}
