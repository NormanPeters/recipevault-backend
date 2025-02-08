package com.barriquebackend.bucksbuddy.journey.expenditure;

import com.barriquebackend.bucksbuddy.journey.Journey;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "expenditures")
public class Expenditure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenditureId;

    @ManyToOne
    @JoinColumn(name = "journey_id", nullable = false)
    @JsonBackReference
    private Journey journey;

    private String name;
    private double amount;

    @Temporal(TemporalType.DATE)
    private Date date;

    public Expenditure() {
    }

    public Expenditure(String name, double amount, Journey journey) {
        this.name = name;
        this.amount = amount;
        this.date = new Date();
        this.journey = journey;
    }

    public Long getExpenditureId() {
        return expenditureId;
    }

    public void setExpenditureId(Long expenditureId) {
        this.expenditureId = expenditureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Journey getJourney() {
        return journey;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }
}