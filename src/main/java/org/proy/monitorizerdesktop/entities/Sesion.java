package org.proy.monitorizerdesktop.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.*;

@Getter
@Setter
@Entity
@Table(name = "sesiones")
public class Sesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha_transmision", nullable = false)
    private LocalDateTime fechaTransmision;

    @Column(name = "estado_sesion", length = 20)
    private String estadoSesion;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "servidor_id")
    private Usuario servidor;

    public Sesion(LocalDateTime fecha, String estadoSesion, Usuario cliente, Usuario servidor) {
        this.fechaTransmision = fecha;
        this.estadoSesion = estadoSesion;
        this.cliente = cliente;
        this.servidor = servidor;
    }

    public Sesion() {}

    public static Sesion crearFinalizada(LocalDateTime fecha, String estadoSesion ,Usuario cliente, Usuario servidor) {
        return new Sesion(fecha, estadoSesion, cliente, servidor);
    }
}
