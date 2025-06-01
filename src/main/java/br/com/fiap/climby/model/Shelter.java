package br.com.fiap.climby.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CY_SHELTER")
@Data
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private Long phone;
    private String country;
    private String city;
    private String adress;
    @Column(name = "ADRESS_NUMBER")
    private int adressNumber;
    private String zip;
    private String district;
    @Column(name = "IS_FULL")
    private boolean isFull;
}
