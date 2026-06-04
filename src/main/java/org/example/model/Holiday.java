package org.example.model;

import java.util.Date;

public class Holiday {

    private String country;
    private String name;
    private Date date;

    public Holiday() {
    }

    public Holiday(String country, String name, Date date) {
        this.country = country;
        this.name = name;
        this.date = date;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
