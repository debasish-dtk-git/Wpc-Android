package com.hrms.attendanceapp.getset;

/**
 * Created by Hirak on 12/28/2018.
 */

public class HolidayList {




    private String id = "";
    private String year = "";
    private String type = "";
    private String fromdate = "";
    private String todate = "";
    private String nodays = "";

    public void setId(String id)
    {
        this.id=id;
    }
    public String getId()
    {
        return this.id;
    }

    public void setYear(String year)
    {
        this.year=year;
    }
    public String getYear()
    {
        return this.year;
    }

    public void setType(String type)
    {
        this.type=type;
    }
    public String getType()
    {
        return this.type;
    }

    public void setFromdate(String fromdate)
    {
        this.fromdate=fromdate;
    }
    public String getFromdate()
    {
        return this.fromdate;
    }

    public void setTodate(String todate)
    {
        this.todate=todate;
    }
    public String getTodate()
    {
        return this.todate;
    }

    public void setNodays(String nodays)
    {
        this.nodays=nodays;
    }
    public String getNodays()
    {
        return this.nodays;
    }
}
