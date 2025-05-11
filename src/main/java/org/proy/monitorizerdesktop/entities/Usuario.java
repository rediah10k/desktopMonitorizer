package org.proy.monitorizerdesktop.entities;

import jakarta.persistence.*;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombres;
    private String apellidos;
    private String email;
    private String password;


}
