package com.hrms.attendanceapp.getset;

public class ViewLeaveList {

    private String id = "";
    private String leavetype = "";
    private String dateOfApply = "";
    private String noOfLeave = "";
    private String fromdate = "";
    private String todate = "";
    private String status = "";
    private String remarks = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public String getLeavetype() {
        return leavetype;
    }

    public void setLeavetype(String leavetype) {
        this.leavetype = leavetype;
    }

    public String getDateOfApply() {
        return dateOfApply;
    }

    public void setDateOfApply(String dateOfApply) {
        this.dateOfApply = dateOfApply;
    }

    public String getNoOfLeave() {
        return noOfLeave;
    }

    public void setNoOfLeave(String noOfLeave) {
        this.noOfLeave = noOfLeave;
    }


}
