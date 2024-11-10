package com.wqz.allinone.entity;

public class Link {
    private int id;
    private String title;
    private String url;
    private int folder;

    public Link() {
    }

    public Link(int id, String title, String url, int folder) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.folder = folder;
    }

    /**
     * 获取
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * 设置
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 获取
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取
     *
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取
     *
     * @return folder
     */
    public int getFolder() {
        return folder;
    }

    /**
     * 设置
     *
     * @param folder
     */
    public void setFolder(int folder) {
        this.folder = folder;
    }

    public String toString() {
        return "Link{id = " + id + ", title = " + title + ", url = " + url + ", folder = " + folder + "}";
    }
}