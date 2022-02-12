package com.hrms.attendanceapp.getset;

public class EmpList {
    private String id;
    private String name;
    private boolean selected;

   /* public EmpList() {

    }

    EmpList(String id, String name) {
        id = id;
        name = name;
    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return name;
    }
}
