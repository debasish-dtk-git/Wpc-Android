package com.hrms.attendanceapp.getset;

/**
 * Created by user on 4/4/2019.
 */

public class AttandenceSubList {


    private String timein = "";
    private String timeout = "";
    private String timein_location = "";
    private String timeout_location = "";
    private String dutyhours = "";


    public void setTimein(String timein)
    {
        this.timein = timein;
    }
    public String getTimein()
    {
        return this.timein;
    }
    public void setTimeout(String timeout)
    {
        this.timeout = timeout;
    }
    public String getTimeout()
    {
        return this.timeout;
    }
    public void setTimein_location(String timein_location)
    {
        this.timein_location = timein_location;
    }
    public String getTimein_location()
    {
        return this.timein_location;
    }

    public void setTimeout_location(String timeout_location)
    {
        this.timeout_location = timeout_location;
    }
    public String getTimeout_location()
    {
        return this.timeout_location;
    }

    public String getDutyhours() {
        return dutyhours;
    }

    public void setDutyhours(String dutyhours) {
        this.dutyhours = dutyhours;
    }
}
