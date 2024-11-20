package com.ums.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "property")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "property_name", nullable = false)
    private String propertyName;

    @Column(name = "guests", nullable = false)
    private String guests;

    @Column(name = "beds", nullable = false)
    private String beds;

    @Column(name = "bathrooms")
    private String bathrooms;

    @Column(name = "bedrooms", nullable = false)
    private String bedrooms;

    @Column(name = "nightly_price", nullable = false)
    private int nightlyPrice;



}
