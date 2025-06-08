package es.tfg.tu_curso.servicio.interfaces;

import es.tfg.tu_curso.dto.SolicitudAmistadDTO;
import es.tfg.tu_curso.modelo.SolicitudAmistad;

import java.util.List;

/**
 * Interfaz que define las operaciones relacionadas con la gestión de solicitudes de amistad.
 * Proporciona métodos para enviar, aceptar, rechazar y consultar solicitudes de amistad.
 */
public interface SolicitudAmistadServicio {

    /**
     * Envía una solicitud de amistad desde un usuario a otro.
     *
     * @param emisorId ID del usuario que envía la solicitud
     * @param receptorId ID del usuario que recibe la solicitud
     * @return {@code true} si la solicitud se envió exitosamente, {@code false} en caso contrario
     */
    boolean enviarSolicitud(Long emisorId, Long receptorId);

    /**
     * Acepta una solicitud de amistad. Al aceptar, se eliminará la solicitud
     * y se establecerá la relación de amistad entre ambos usuarios.
     *
     * @param solicitudId ID de la solicitud a aceptar
     * @return {@code true} si se aceptó exitosamente, {@code false} en caso contrario
     */
    boolean aceptarSolicitud(Long solicitudId);

    /**
     * Rechaza una solicitud de amistad. La solicitud será eliminada del sistema.
     *
     * @param solicitudId ID de la solicitud a rechazar
     * @return {@code true} si se rechazó exitosamente, {@code false} en caso contrario
     */
    boolean rechazarSolicitud(Long solicitudId);

    /**
     * Obtiene todas las solicitudes de amistad recibidas por un usuario.
     *
     * @param receptorId ID del usuario receptor
     * @return Lista de solicitudes recibidas
     */
    List<SolicitudAmistad> obtenerSolicitudesRecibidas(Long receptorId);

    /**
     * Obtiene todas las solicitudes de amistad enviadas por un usuario.
     *
     * @param emisorId ID del usuario emisor
     * @return Lista de solicitudes enviadas
     */
    List<SolicitudAmistad> obtenerSolicitudesEnviadas(Long emisorId);

    /**
     * Obtiene todas las solicitudes de amistad recibidas por un usuario en formato DTO.
     *
     * @param receptorId ID del usuario receptor
     * @return Lista de solicitudes recibidas como DTO
     */
    List<SolicitudAmistadDTO> obtenerSolicitudesRecibidasDTO(Long receptorId);

    /**
     * Obtiene todas las solicitudes de amistad enviadas por un usuario en formato DTO.
     *
     * @param emisorId ID del usuario emisor
     * @return Lista de solicitudes enviadas como DTO
     */
    List<SolicitudAmistadDTO> obtenerSolicitudesEnviadasDTO(Long emisorId);

    /**
     * Verifica si existe una solicitud de amistad pendiente entre dos usuarios.
     *
     * @param usuario1Id ID del primer usuario
     * @param usuario2Id ID del segundo usuario
     * @return {@code true} si existe una solicitud pendiente, {@code false} en caso contrario
     */
    boolean existeSolicitudPendiente(Long usuario1Id, Long usuario2Id);

    /**
     * Obtiene el número de solicitudes de amistad recibidas por un usuario.
     *
     * @param receptorId ID del usuario receptor
     * @return Número de solicitudes recibidas
     */
    long contarSolicitudesRecibidas(Long receptorId);
}