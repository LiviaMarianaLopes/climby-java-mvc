package br.com.fiap.climby.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CY_USER")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String country;
    private String city;
    private String password;
}
