package org.proy.monitorizerdesktop.entities;

import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name = "videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150)
    private String nombre;

    @Column(name = "ruta_archivo", length = 500)
    private String rutaArchivo;

    @Column(name = "tamano_en_bytes")
    private Long tamanoEnBytes;

    @Column(name = "fecha_transferencia")
    private LocalDateTime fechaTransferencia;

    @ManyToOne
    @JoinColumn(name = "sesion_id")
    private Sesion sesion;

    public Video() {

    }

}
