package org.irlab.model.entities;

import java.util.Date;
import java.util.List;

public class Paquete extends Plantilla {
    private String name;
    private String description;
    private String destination;
    private String accommodation;
    private List<String> transportation;
    private List<String> activities;
    private Date begin;
    private Date end;
    private Long people;
    private Long prize;

    public Paquete (String name, String description, String destination, String accommodation, List<String> transportation, List<String> activities, Date begin, Date end, Long people, Long prize) {
        super(name, description, destination, accommodation, transportation, activities);
        this.begin = begin;
        this.end = end;
        this.people = people;
        this.prize = prize;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Long getPeople() {
        return people;
    }

    public void setPeople(Long people) {
        this.people = people;
    }

    public Long getPrize() {
        return prize;
    }

    public void setPrize(Long prize) {
        this.prize = prize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<String> getTransportation() {
        return transportation;
    }

    public void setTransportation(List<String> transportation) {
        this.transportation = transportation;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }
}
