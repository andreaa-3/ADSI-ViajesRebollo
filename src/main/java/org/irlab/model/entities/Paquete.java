package org.irlab.model.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Paquete extends Plantilla { // Extiende de Plantilla? 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private int requiredPeople;

    @Column(nullable = false) // Puede haber un viaje sin alojamiento?
    private String accommodation;

    @ElementCollection
    private List<String> transportation;

    @ElementCollection
    private List<String> activities;

    @Column(nullable = false)
    private double price;

    public Paquete() {}

    public Paquete(String name, String description, String destination,
                         LocalDate startDate, LocalDate endDate, int requiredPeople,
                         String accomodation, List<String> transportation, List<String> activities, double price) {
        this.name = name;
        this.description = description;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.requiredPeople = requiredPeople;
        this.accommodation = accomodation;
        this.transportation = transportation;
        this.activities = activities;
        this.price = price;
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) { // Creo que no es necesario
        // No se puede modificar el id, es autogenerado.
        this.id = id;
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
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }

   
}
