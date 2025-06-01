package org.proy.monitorizerdesktop.clientserver.services;

import org.proy.monitorizerdesktop.auth.repos.SesionRepository;
import org.proy.monitorizerdesktop.auth.repos.UsuarioRepository;
import org.proy.monitorizerdesktop.auth.repos.VideoRepository;
import org.proy.monitorizerdesktop.clientserver.dtos.SesionDTO;
import org.proy.monitorizerdesktop.clientserver.utils.EstatusConexion;
import org.proy.monitorizerdesktop.entities.Sesion;
import org.proy.monitorizerdesktop.entities.Usuario;
import org.proy.monitorizerdesktop.entities.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;

@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SesionRepository sesionRepository;

    @Autowired
    public VideoService(VideoRepository videoRepository, UsuarioRepository usuarioRepository,
                        SesionRepository sesionRepository) {
        this.videoRepository = videoRepository;
        this.sesionRepository = sesionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void persistirVideoGenerado(File videoGuardadoLocal, SesionDTO datosSesion) {
        try {
            byte[] contenidoVideo = Files.readAllBytes(videoGuardadoLocal.toPath());
            String nombre = videoGuardadoLocal.getName();
            String tipoMime = determinarTipoMime(nombre);
            LocalDateTime fecha = LocalDateTime.now();

            Usuario cliente = usuarioRepository.findById(datosSesion.getClienteId())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
            Usuario servidor = usuarioRepository.findById(datosSesion.getServidorId())
                    .orElseThrow(() -> new IllegalArgumentException("Servidor no encontrado"));

            Sesion sesion = Sesion.crearFinalizada(fecha, EstatusConexion.FINALIZADA.toString(), cliente, servidor);
            sesionRepository.save(sesion);
            Video video = Video.crearDesdeSesion(nombre, contenidoVideo, tipoMime, sesion);

            videoRepository.save(video);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de video: " + videoGuardadoLocal.getAbsolutePath(), e);
        }
    }

    private String determinarTipoMime(String nombreArchivo) {
        String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf('.') + 1).toLowerCase();

        return switch (extension) {
            case "avi" -> "video/avi";
            case "mov" -> "video/quicktime";
            case "wmv" -> "video/x-ms-wmv";
            case "flv" -> "video/x-flv";
            case "webm" -> "video/webm";
            default -> "video/mp4";
        };
    }
}
