package com.example.carmanager.service.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "service_catalog")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    private String description;

    protected Service() {
    }

    public Service(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
}
