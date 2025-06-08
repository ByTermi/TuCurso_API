package es.tfg.tu_curso.servicio.implementaciones;

import es.tfg.tu_curso.dto.SolicitudAmistadDTO;
import es.tfg.tu_curso.modelo.SolicitudAmistad;
import es.tfg.tu_curso.modelo.Usuario;
import es.tfg.tu_curso.repositorio.RepositorioSolicitudAmistad;
import es.tfg.tu_curso.repositorio.RepositorioUsuario;
import es.tfg.tu_curso.servicio.interfaces.SolicitudAmistadServicio;
import es.tfg.tu_curso.servicio.interfaces.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de gestión de solicitudes de amistad.
 * Proporciona la lógica de negocio para operaciones con solicitudes de amistad,
 * incluyendo envío, aceptación, rechazo y consulta de solicitudes.
 */
@Service
public class SolicitudAmistadServicioImpl implements SolicitudAmistadServicio {

    @Autowired
    private RepositorioSolicitudAmistad solicitudRepositorio;

    @Autowired
    private RepositorioUsuario usuarioRepositorio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación verifica que ambos usuarios existan, que no sean el mismo usuario,
     * que no sean ya amigos, y que no exista una solicitud pendiente antes de crear la nueva solicitud.
     * </p>
     */
    @Override
    @Transactional
    public boolean enviarSolicitud(Long emisorId, Long receptorId) {
        // Verificar que no sea el mismo usuario
        if (emisorId.equals(receptorId)) {
            return false;
        }

        // Verificar que ambos usuarios existan
        Optional<Usuario> emisorOpt = usuarioRepositorio.findById(emisorId);
        Optional<Usuario> receptorOpt = usuarioRepositorio.findById(receptorId);

        if (emisorOpt.isEmpty() || receptorOpt.isEmpty()) {
            return false;
        }

        Usuario emisor = emisorOpt.get();
        Usuario receptor = receptorOpt.get();

        // Verificar que no sean ya amigos
        if (usuarioServicio.sonAmigos(emisorId, receptorId)) {
            return false;
        }

        // Verificar que no exista una solicitud pendiente
        if (solicitudRepositorio.existeSolicitudPendiente(emisorId, receptorId)) {
            return false;
        }

        // Crear y guardar la nueva solicitud
        SolicitudAmistad nuevaSolicitud = new SolicitudAmistad(emisor, receptor);
        solicitudRepositorio.save(nuevaSolicitud);
        return true;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación busca la solicitud, verifica que exista, establece la relación
     * de amistad entre los usuarios y elimina la solicitud.
     * </p>
     */
    @Override
    @Transactional
    public boolean aceptarSolicitud(Long solicitudId) {
        Optional<SolicitudAmistad> solicitudOpt = solicitudRepositorio.findById(solicitudId);

        if (solicitudOpt.isEmpty()) {
            return false;
        }

        SolicitudAmistad solicitud = solicitudOpt.get();
        Long emisorId = solicitud.getEmisor().getId();
        Long receptorId = solicitud.getReceptor().getId();

        // Agregar como amigos usando el servicio de usuario
        boolean amistadEstablecida = usuarioServicio.agregarAmigo(emisorId, receptorId);

        if (amistadEstablecida) {
            // Eliminar la solicitud después de establecer la amistad
            solicitudRepositorio.delete(solicitud);
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación simplemente elimina la solicitud del sistema.
     * </p>
     */
    @Override
    @Transactional
    public boolean rechazarSolicitud(Long solicitudId) {
        Optional<SolicitudAmistad> solicitudOpt = solicitudRepositorio.findById(solicitudId);

        if (solicitudOpt.isEmpty()) {
            return false;
        }

        solicitudRepositorio.delete(solicitudOpt.get());
        return true;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación obtiene todas las solicitudes donde el usuario es el receptor.
     * </p>
     */
    @Override
    public List<SolicitudAmistad> obtenerSolicitudesRecibidas(Long receptorId) {
        return solicitudRepositorio.findByReceptorId(receptorId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación obtiene todas las solicitudes donde el usuario es el emisor.
     * </p>
     */
    @Override
    public List<SolicitudAmistad> obtenerSolicitudesEnviadas(Long emisorId) {
        return solicitudRepositorio.findByEmisorId(emisorId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación obtiene todas las solicitudes recibidas y las convierte a DTO
     * para evitar exponer información sensible como contraseñas.
     * </p>
     */
    @Override
    public List<SolicitudAmistadDTO> obtenerSolicitudesRecibidasDTO(Long receptorId) {
        List<SolicitudAmistad> solicitudes = solicitudRepositorio.findByReceptorId(receptorId);
        return solicitudes.stream()
                .map(SolicitudAmistadDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación obtiene todas las solicitudes enviadas y las convierte a DTO
     * para evitar exponer información sensible como contraseñas.
     * </p>
     */
    @Override
    public List<SolicitudAmistadDTO> obtenerSolicitudesEnviadasDTO(Long emisorId) {
        List<SolicitudAmistad> solicitudes = solicitudRepositorio.findByEmisorId(emisorId);
        return solicitudes.stream()
                .map(SolicitudAmistadDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación verifica si existe una solicitud pendiente entre dos usuarios
     * en cualquier dirección.
     * </p>
     */
    @Override
    public boolean existeSolicitudPendiente(Long usuario1Id, Long usuario2Id) {
        return solicitudRepositorio.existeSolicitudPendiente(usuario1Id, usuario2Id);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación cuenta el número de solicitudes recibidas por un usuario.
     * </p>
     */
    @Override
    public long contarSolicitudesRecibidas(Long receptorId) {
        return solicitudRepositorio.countByReceptorId(receptorId);
    }
}