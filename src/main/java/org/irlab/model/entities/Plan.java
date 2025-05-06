package org.irlab.model.entities;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Plan { //Extiende de Plantilla?

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Es la clave primaria (es autogenerado).

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
    private String accommodation; // En la EERR aparece como "hotel"

    @ElementCollection
    private String transportation;

    //@ElementCollection
    //private String activities; // Los planes específicos no tienen actividades?

    //@Column(nullable = false)
    //private double price; // Los planes específicos no tienen precio?

    public Plan() {
    }

    public Plan(String name, String description, String destination, LocalDate startDate, LocalDate endDate,
            String accommodation, String transportation, String activities, double price) {
        this.name = name;
        this.description = description;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.accommodation = accommodation;
        this.transportation = transportation;
        //this.activities = activities;
        //this.price = price;
    }


    public Long getId() {
        return id;
    }
    
    public void setId(Long id) { // Creo que no es necesario
        //this.id = id; // No se puede modificar el id, es autogenerado.
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

}