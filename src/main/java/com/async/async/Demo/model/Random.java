package com.async.async.Demo.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Random {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String address;

    public Random(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
