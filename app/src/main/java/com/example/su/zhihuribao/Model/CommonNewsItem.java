package com.example.su.zhihuribao.Model;

/**
 * Created by su on 16/5/10.
 */
public class CommonNewsItem {

    private String id;
    private String url;
    private String thumbnail;
    private String title;

    public CommonNewsItem(String id, String url, String thumbnail, String title) {
        this.id = id;
        this.url = url;
        this.thumbnail = thumbnail;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }
}
