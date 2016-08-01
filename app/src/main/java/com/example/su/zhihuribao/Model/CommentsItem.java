package com.example.su.zhihuribao.Model;

/**
 * Created by su on 16/5/11.
 */
public class CommentsItem {

    private String avatar;
    private String author;
    private String comment;

    public CommentsItem(String avatar, String author, String comment) {
        this.avatar = avatar;
        this.author = author;
        this.comment = comment;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAuthor() {
        return author;
    }

    public String getComment() {
        return comment;
    }
}
