package com.wqz.allinone.entity;

public class Todo {
    private int id;
    private String title;
    private boolean completed;

    public Todo() {
    }

    public Todo(int id, String title, boolean completed) {
        this.id = id;
        this.title = title;
        this.completed = completed;
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
     * @return completed
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * 设置
     *
     * @param completed
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String toString() {
        return "Todo{id = " + id + ", title = " + title + ", completed = " + completed + "}";
    }

    public void toggleCompleted() {
        this.completed = !this.completed; // 切换 completed 的状态
    }
}