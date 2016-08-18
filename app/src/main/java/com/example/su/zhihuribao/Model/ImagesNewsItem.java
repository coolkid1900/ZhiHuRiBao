package com.example.su.zhihuribao.Model;

import java.util.List;

/**
 * Created by su on 16/5/6.
 */
public class ImagesNewsItem {
    private String title;
    private List<String> images;
    private String type;
    private String id;

    public ImagesNewsItem(String title, List<String> images, String type, String id) {
        this.title = title;
        this.images = images;
        this.type = type;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getImage() {
        return images;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getFirstImage(){
        if (images==null){
            return null;
        }
        return images.get(0);
    }
}


