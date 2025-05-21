package org.proy.monitorizerdesktop.entities;

import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name = "sesiones")
public class Sesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private LocalDate fecha;

    @Column
    private LocalTime duracion;

    @Column(name = "estado_sesion", length = 20)
    private String estadoSesion;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "servidor_id")
    private Usuario servidor;

    public Sesion() {

    }
}
