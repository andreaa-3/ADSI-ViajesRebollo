package org.irlab.model.entities;

import java.util.Date;

public class Plan {
    private String name;
    private String description;
    private String destination;
    private String accommodation;
    private String transportation;
    private Date begin;

    public Plan(String name, String description, String destination, String accommodation, String transportation, Date begin, Date end) {
        this.name = name;
        this.description = description;
        this.destination = destination;
        this.accommodation = accommodation;
        this.transportation = transportation;
        this.begin = begin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(String accommodation) {
        this.accommodation = accommodation;
    }

    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }
}
