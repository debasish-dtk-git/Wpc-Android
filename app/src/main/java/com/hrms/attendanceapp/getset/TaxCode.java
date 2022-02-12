package com.hrms.attendanceapp.getset;

public class TaxCode {
    private String id = "";
    private String taxcode = "";
    private String persntg = "";
    private String taxref = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaxcode() {
        return taxcode;
    }

    public void setTaxcode(String taxcode) {
        this.taxcode = taxcode;
    }

    public String getPersntg() {
        return persntg;
    }

    public void setPersntg(String persntg) {
        this.persntg = persntg;
    }

    public String getTaxref() {
        return taxref;
    }

    public void setTaxref(String taxref) {
        this.taxref = taxref;
    }

    @Override
    public String toString() {
        return taxcode;
    }
}
