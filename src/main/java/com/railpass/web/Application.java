package com.railpass.web;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Application {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private User student;

    @Enumerated
    private Station start;

    @Enumerated
    private Station end;

    @Enumerated
    private Tier tier;

    @Enumerated
    private Period period;

    @Enumerated
    private Status status;

    protected Application() {}

    public Application(User student, Station start, Station end, Tier tier, Period period, Status status) {
        this.student = student;
        this.start = start;
        this.end = end;
        this.tier = tier;
        this.period = period;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public User getStudent() {
        return student;
    }

    public Station getStart() {
        return start;
    }

    public Station getEnd() {
        return end;
    }

    public Tier getTier() {
        return tier;
    }

    public Period getPeriod() {
        return period;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
