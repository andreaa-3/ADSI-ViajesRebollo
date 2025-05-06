package org.irlab.model.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Plantilla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Es la clave primaria (es autogenerado).

     @Column(unique = true, nullable = false)
    public String name;

    @Column(nullable = false)
    public String description;

    @Column(nullable = false)
    public String destination;

    @Column(nullable = false)
    public String accommodation;

    @ElementCollection
    public List<String> transportation;

    @ElementCollection
    public List<String> activities;


    public Plantilla() {
    }

    public Plantilla(String name, String description, String destination, 
            String accommodation, List<String> transportation, List<String> activities) {
        this.name = name;
        this.description = description;
        this.destination = destination;
        this.accommodation = accommodation;
        this.transportation = transportation;
        this.activities = activities;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) { // Creo que no es necesario
        this.id = id; // No se puede modificar el id, es autogenerado.
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
