package com.example.jobsuche_app;

public class Job {
    private String imgUrl;
    private String description;
    private String bundesland;

    public Job(String imgUrl, String description, String bundesland) {
        this.imgUrl = imgUrl;
        this.description = description;
        this.bundesland = bundesland;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBundesland() {
        return bundesland;
    }

    public void setBundesland(String bundesland) {
        this.bundesland = bundesland;
    }
}
