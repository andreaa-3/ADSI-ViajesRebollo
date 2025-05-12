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
    private List<String> accommodation = new ArrayList<>();

    @ElementCollection
    private List<String> transportation = new ArrayList<>();

    @ElementCollection
    private List<String> activities = new ArrayList<>();

    @Column(nullable = false)
    private double price = -1; 

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

    // Functions to add elements to an array.
    
    public List<String> addList (String item, List<String> list, String fieldName) {
        if (item == null || item.isEmpty()) {
            throw new IllegalArgumentException(fieldName  + " cannot be null or empty.");
        }
        if (list == null) list = new ArrayList<>();
        if (!list.contains(item)) list.add(item);
        return list;
    }

    public void addDestination(String destination) {
        List<String> l = addList(destination, this.destination, "Destionation");
        if (l != null) setDestination(l);
    }

    public void addAccommodation(String accommodation) {
        List<String> l = addList(accommodation, this.accommodation, "Accommodation");
        if (l != null) setAccommodation(l);
    }

    public void addTransportation(String transport) {
        List<String> l = addList(transport, this.transportation, "Transportation");
        if (l != null) setTransportation(l);
    }

    public void addActivity(String activity) {
        List<String> l = addList(activity, this.activities, "Activity");
        if (l != null) setActivities(l);
    }
    
    // Functions to delete elements from an array.

    public List<String> deleteList(String item, List<String> list, String fieldName) {
        if (item == null || item.isEmpty()) throw new IllegalArgumentException(fieldName + " cannot be null or empty.");

        if (list == null) return null;
        
        String target = item.trim().toLowerCase();
        list.removeIf(a -> a.trim().toLowerCase().equals(target));

        if (list.isEmpty()) return null;
        return list;
    }


    public void deleteDestination(String dest) {
        setDestination(deleteList(dest, this.destination, "Destination"));
    }

    public void deleteAccommodation(String accommodation) {
        setAccommodation(deleteList(accommodation, this.accommodation, "Accommodation"));
    }

    public void deleteTransportation(String transport) {
        setTransportation(deleteList(transport, this.transportation, "Transportation"));
    }

    public void deleteActivity(String activity) {
        setActivities(deleteList(activity, this.activities, "Activity"));
    }

    @Override
    public String toString() {
        return String.format(
            "\n=========== PLAN DETAILS =============\n" +
            " General Info\n" +
            "  ID             : %d\n" +
            "  Name           : %s\n" +
            "  Description    : %s\n" +
            "\n Dates\n" +
            "  Start Date     : %s\n" +
            "  End Date       : %s\n" +
            "\n Details\n" +
            "  Destination    : %s\n" +
            "  Accommodation  : %s\n" +
            "  Transportation : %s\n" +
            "  Activities     : %s\n" +
            "  Price          : %.2f\n" +
            "\n Based On\n" +
            "  Source         : %s\n" +
            "======================================\n",
            id,
            name,
            description,
            startDate,
            endDate,
            String.join(", ", destination != null ? destination : List.of()),
            String.join(", ", accommodation != null ? accommodation : List.of()),
            String.join(", ", transportation != null ? transportation : List.of()),
            String.join(", ", activities != null ? activities : List.of()),
            price,
            plantillaBase != null ? "Plantilla: " + plantillaBase.getName() :
                paqueteBase != null ? "Paquete: " + paqueteBase.getName() : "None"
        );
    }
 
}