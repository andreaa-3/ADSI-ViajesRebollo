package org.irlab.model.entities;

import java.util.List;

public class Plantilla {
    public String name;
    public String description;
    public String destination;
    public String accommodation;
    public List<String> transportation;
    public List<String> activities;

    public Plantilla(String name, String description, String destination, String accommodation, List<String> transportation, List<String> activities) {
        this.name = name;
        this.description = description;
        this.destination = destination;
        this.accommodation = accommodation;
        this.transportation = transportation;
        this.activities = activities;
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
