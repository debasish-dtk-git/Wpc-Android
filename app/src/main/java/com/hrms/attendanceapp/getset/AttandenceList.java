package com.hrms.attendanceapp.getset;

import java.util.ArrayList;

/**
 * Created by user on 1/18/2019.
 */

public class AttandenceList {


    private String id = "";
    private String date = "";
    private String month = "";
    private ArrayList<AttandenceSubList> attandenceSubLists;


    public void setId(String id)
    {
        this.id = id;
    }
    public String getId()
    {
        return this.id;
    }

    public void setDate(String date)
    {
        this.date = date;
    }
    public String getDate()
    {
        return this.date;
    }


    public void setMonth(String month)
    {
        this.month = month;
    }
    public String getMonth()
    {
        return this.month;
    }

    public void setAttandenceSubLists(ArrayList<AttandenceSubList> attandenceSubLists) {
        this.attandenceSubLists = attandenceSubLists;
    }

    public ArrayList<AttandenceSubList> getAttandenceSubLists() {
        return attandenceSubLists;
    }

}
