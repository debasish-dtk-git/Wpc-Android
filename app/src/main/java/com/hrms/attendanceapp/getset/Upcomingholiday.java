package com.hrms.attendanceapp.getset;

public class Upcomingholiday {

    private String id;
    private String year;
    private String holidaytype;
    private String fromdate;




    public void setId(String id)
    {
        this.id = id;
    }
    public String getId()
    {
        return id;
    }

    public void setYear(String year)
    {
        this.year = year;
    }
    public String getYear()
    {
        return year;
    }


    public void setHolidaytype(String holidaytype)
    {
        this.holidaytype = holidaytype;
    }
    public String getHolidaytype()
    {
        return holidaytype;
    }


    public void setFromdate(String fromdate)
    {
        this.fromdate = fromdate;
    }
    public String getFromdate()
    {
        return fromdate;
    }

}
