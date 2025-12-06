package com.batch_processing.batch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coffee")
public class Coffee {

    @Column(name = "brand" , length = 25)
    private String brand;

    @Column(name = "origin" , length = 30)
    private String origin;

    @Column(name = "characterstics" , length = 255)
    private String characterstics;
    
}
