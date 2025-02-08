package com.barriquebackend.bucksbuddy.journey;

import com.barriquebackend.bucksbuddy.journey.expenditure.Expenditure;
import com.barriquebackend.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "journeys")
public class Journey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long journeyId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String homeCurr;

    @Column(nullable = false)
    private String vacCurr;

    @Column(nullable = false)
    private int budget;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date endDate;

    @OneToMany(mappedBy = "journey", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Expenditure> expenditures;

    // Getters and setters
    public Long getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(Long journeyId) {
        this.journeyId = journeyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getHomeCurr() {
        return homeCurr;
    }

    public void setHomeCurr(String homeCurr) {
        this.homeCurr = homeCurr;
    }

    public String getVacCurr() {
        return vacCurr;
    }

    public void setVacCurr(String vacCurr) {
        this.vacCurr = vacCurr;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    // Updated getter to return Date instead of String
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    // Updated getter to return Date instead of String
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Set<Expenditure> getExpenditures() {
        return expenditures;
    }

    public void setExpenditures(Set<Expenditure> expenditures) {
        this.expenditures = expenditures;
    }
}