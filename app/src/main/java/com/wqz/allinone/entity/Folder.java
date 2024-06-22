package com.wqz.allinone.entity;

public class Folder {
    private int id;
    private String name;

    public Folder() {
    }

    public Folder(int id, String name) {
        this.id = id;
        this.name = name;
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
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "Folder{id = " + id + ", name = " + name + "}";
    }
}