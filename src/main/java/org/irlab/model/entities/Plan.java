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
import jakarta.persistence.ManyToOne;

@Entity
public class Plan { //Extiende de Plantilla y de Paquete? 
    // Creo que no es necesario que extienda, se puede manejar la lógica en el servicio.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Es la clave primaria (es autogenerado).

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ElementCollection
    private List<String> destination = new ArrayList<>();

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @ElementCollection
    private List<String> accommodation = new ArrayList<>(); // En la EERR aparece como "hotel"

    @ElementCollection
    private List<String> transportation = new ArrayList<>();

    @ElementCollection
    private List<String> activities = new ArrayList<>();

    @Column(nullable = false)
    private double price; 

     // Relación con plantilla (si se creó desde una)
    @ManyToOne
    private Plantilla plantillaBase;

    // Relación con paquete (si se creó desde un paquete base)
    @ManyToOne
    private Paquete paqueteBase;

    /*
    REGLAS DE NEGOCIO A APLICAR
    Si se usa plantillaBase, se permite modificar cualquier campo.

    Si se usa paqueteBase, se pueden añadir extras, pero no modificar los campos del paquete original.

    Los campos plantillaBase y paqueteBase no deben estar ambos a la vez (puedes validarlo a nivel de lógica o servicio).
    */

    public Plan() {
    }

    public Plan(String name, String description, List<String> destination, LocalDate startDate, LocalDate endDate,
            List<String> accommodation, List<String> transportation, List<String> activities, double price) {
        this.name = name;
        this.description = description;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public void setDescription(String description) {
            this.description = description;
    }

    
    public String getDescription() {
        return description;
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

    public Plantilla getPlantillaBase() {
        return plantillaBase;
    }

    public void setPlantillaBase(Plantilla plantillaBase) {
        this.plantillaBase = plantillaBase;
    }

    public Paquete getPaqueteBase() {
        return paqueteBase;
    }

    public void setPaqueteBase(Paquete paqueteBase) {
        this.paqueteBase = paqueteBase;
    }

    // Funciones para añadir elementos "extras" a los paquetes y plantillas creados.

    public void addDestination(String destination) {
        if (destination == null || destination.isEmpty()) {
            throw new IllegalArgumentException("Destination cannot be null or empty.");
        }
        this.destination.add(destination);
    }

    public void addAccommodation(String accommodation) {
        if (accommodation == null || accommodation.isEmpty()) {
            throw new IllegalArgumentException("Accommodation cannot be null or empty.");
        }
        this.accommodation.add(accommodation);
    }

    public void addTransportation(String transport) {
        if (transport == null || transport.isEmpty()) {
            throw new IllegalArgumentException("Transportation cannot be null or empty.");
        }
        this.transportation.add(transport);
    }

    public void addActivity(String activity) {
        if (activity == null || activity.isEmpty()) {
            throw new IllegalArgumentException("Activity cannot be null or empty.");
        }
        this.activities.add(activity);
    }

}