package org.proy.monitorizerdesktop.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.*;

@Getter
@Setter
@Entity
@Table(name = "videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150, nullable = false)
    private String nombre;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] contenido;

    @Column(name = "longitud_bytes", nullable = false)
    private Long longitudBytes;

    @Column(name = "tipo_mime",length = 150, nullable = false)
    private String tipoMime;

    @ManyToOne
    @JoinColumn(name = "sesion_id")
    private Sesion sesion;

    public Video() {}

    public Video(String nombre, byte[] contenido, Long longitudBytes, String tipoMime, Sesion sesion) {
        this.nombre = nombre;
        this.contenido = contenido;
        this.longitudBytes = longitudBytes;
        this.tipoMime = tipoMime;
        this.sesion = sesion;
    }

    public static Video crearDesdeSesion(String nombreArchivo, byte[] contenido, String tipoMime, Sesion sesion) {
        Long longitud = (long) contenido.length;
        return new Video(nombreArchivo, contenido, longitud, tipoMime, sesion);
    }
}
