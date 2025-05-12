package org.irlab.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Paquete { 
    // Se Hace de manera independiente de la clase Plantilla
    // Muchos atributos puede que sean nulos? 
        // Ej: Un paquete que no tenga alojamiento, o que no tenga transporte?

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;
    
    @ElementCollection
    private List<String> destination;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private int requiredPeople = -1;

    @ElementCollection
    private List<String> accommodation;

    @ElementCollection
    private List<String> transportation;

    @ElementCollection
    private List<String> activities;

    @Column(nullable = false)
    private double price = -1;

    public Paquete() {}

    public Paquete(String name, String description, List<String> destination,
                         LocalDate startDate, LocalDate endDate, int requiredPeople,
                         List<String> accommodation, List<String> transportation, List<String> activities, double price) {
        this.name = name;
        this.description = description;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.requiredPeople = requiredPeople;
        this.accommodation = accommodation;
        this.transportation = transportation;
        this.activities = activities;
        this.price = price;
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
    
    public List<String> getDestination() {
        return destination;
    }
    
    public void setDestination(List<String> destination) {
        this.destination = destination;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public int getRequiredPeople() {
        return requiredPeople;
    }
    
    public void setRequiredPeople(int requiredPeople) {
        this.requiredPeople = requiredPeople;
    }
    
    public List<String> getAccommodation() {
        return accommodation;
    }
    
    public void setAccommodation(List<String> accommodation) {
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
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format(
            "\n========== PACKAGE DETAILS ==========\n" +
            " General Info\n" +
            "  ID              : %d\n" +
            "  Name            : %s\n" +
            "  Description     : %s\n" +
            "\n Dates & People\n" +
            "  Start Date      : %s\n" +
            "  End Date        : %s\n" +
            "  Required People : %d\n" +
            "\n Details\n" +
            "  Destination     : %s\n" +
            "  Accommodation   : %s\n" +
            "  Transportation  : %s\n" +
            "  Activities      : %s\n" +
            "  Price           : %.2f\n" +
            "=====================================\n",
            id,
            name,
            description,
            startDate,
            endDate,
            requiredPeople,
            String.join(", ", destination != null ? destination : List.of()),
            String.join(", ", accommodation != null ? accommodation : List.of()),
            String.join(", ", transportation != null ? transportation : List.of()),
            String.join(", ", activities != null ? activities : List.of()),
            price
        );
    }  
}
