package org.proy.monitorizerdesktop.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

@Data
@Entity

@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Uid;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;
}

