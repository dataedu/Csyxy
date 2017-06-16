package com.dk.edu.csyxy.entity;

/**
 * 新闻
 * 作者：janabo on 2017/6/13 17:11
 */
public class News {
    private String id;
    private String content; //内容
    private String image;//图片
    private String name;//标题
    private String url;//详情页链接
    private String publishTime;
    private boolean load = false;

    private int mType;//0,滑动新闻 1，普通新闻

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public boolean isLoad() {
        return load;
    }

    public void setLoad(boolean load) {
        this.load = load;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public News(int mType) {
        this.mType = mType;
    }
}
