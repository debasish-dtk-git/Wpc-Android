package com.hrms.attendanceapp.getset;

public class NotiList {

    private String id = "";
    private String date = "";
    private String title = "";
    private String desc = "";
    private String readfu = "";
    private String img = "";



    public NotiList(String id, String date, String title, String desc, String readfu, String img) {

        setId(id);
        setDate(date);
        setTitle(title);
        setDesc(desc);
        setReadfu(readfu);
        setImg(img);


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getReadfu() {
        return readfu;
    }

    public void setReadfu(String readfu) {
        this.readfu = readfu;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
