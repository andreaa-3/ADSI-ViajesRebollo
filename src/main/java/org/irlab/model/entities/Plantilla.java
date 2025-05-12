package org.irlab.model.entities;

import java.util.ArrayList;
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
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String destination;// A diferencia del paquete, por defecto cada plantilla solo tienen un destino.

    @Column(nullable = false)
    private String accommodation; // A diferencia del paquete, por defecto cada plantilla solo tienen un alojamiento.

    @ElementCollection
    private List<String> transportation;

    @ElementCollection
    private List<String> activities;


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

    @Override
    public String toString() {
        return String.format(
            "\n========== TEMPLATE DETAILS ==========\n" +
            "  General Info\n" +
            "  ID             : %d\n" +
            "  Name           : %s\n" +
            "  Description    : %s\n" +
            "\n Details\n" +
            "  Destination    : %s\n" +
            "  Accommodation  : %s\n" +
            "  Transportation : %s\n" +
            "  Activities     : %s\n" +
            "======================================\n",
            id,
            name,
            description,
            destination,
            accommodation,
            transportation != null ? String.join(", ", transportation) : "None",
            activities != null ? String.join(", ", activities) : "None"
        );
    }



 
}
