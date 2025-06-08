package es.tfg.tu_curso.repositorio;

import es.tfg.tu_curso.modelo.SolicitudAmistad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad SolicitudAmistad.
 * Proporciona métodos para realizar operaciones CRUD en la base de datos
 * para las solicitudes de amistad entre usuarios.
 */
@Repository
public interface RepositorioSolicitudAmistad extends JpaRepository<SolicitudAmistad, Long> {

    /**
     * Busca una solicitud específica entre dos usuarios.
     *
     * @param emisorId ID del usuario emisor
     * @param receptorId ID del usuario receptor
     * @return Optional con la solicitud si existe
     */
    @Query("SELECT s FROM SolicitudAmistad s WHERE s.emisor.id = :emisorId AND s.receptor.id = :receptorId")
    Optional<SolicitudAmistad> findByEmisorIdAndReceptorId(@Param("emisorId") Long emisorId, @Param("receptorId") Long receptorId);

    /**
     * Obtiene todas las solicitudes recibidas por un usuario.
     *
     * @param receptorId ID del usuario receptor
     * @return Lista de solicitudes recibidas
     */
    @Query("SELECT s FROM SolicitudAmistad s WHERE s.receptor.id = :receptorId")
    List<SolicitudAmistad> findByReceptorId(@Param("receptorId") Long receptorId);

    /**
     * Obtiene todas las solicitudes enviadas por un usuario.
     *
     * @param emisorId ID del usuario emisor
     * @return Lista de solicitudes enviadas
     */
    @Query("SELECT s FROM SolicitudAmistad s WHERE s.emisor.id = :emisorId")
    List<SolicitudAmistad> findByEmisorId(@Param("emisorId") Long emisorId);

    /**
     * Verifica si existe una solicitud entre dos usuarios (en cualquier dirección).
     *
     * @param usuario1Id ID del primer usuario
     * @param usuario2Id ID del segundo usuario
     * @return true si existe una solicitud pendiente, false en caso contrario
     */
    @Query("SELECT COUNT(s) > 0 FROM SolicitudAmistad s WHERE " +
            "(s.emisor.id = :usuario1Id AND s.receptor.id = :usuario2Id) OR " +
            "(s.emisor.id = :usuario2Id AND s.receptor.id = :usuario1Id)")
    boolean existeSolicitudPendiente(@Param("usuario1Id") Long usuario1Id, @Param("usuario2Id") Long usuario2Id);

    /**
     * Cuenta el número de solicitudes recibidas por un usuario.
     *
     * @param receptorId ID del usuario receptor
     * @return Número de solicitudes recibidas
     */
    @Query("SELECT COUNT(s) FROM SolicitudAmistad s WHERE s.receptor.id = :receptorId")
    long countByReceptorId(@Param("receptorId") Long receptorId);

    /**
     * Elimina todas las solicitudes entre dos usuarios específicos.
     *
     * @param usuario1Id ID del primer usuario
     * @param usuario2Id ID del segundo usuario
     */
    @Modifying
    @Query("DELETE FROM SolicitudAmistad s WHERE " +
            "(s.emisor.id = :usuario1Id AND s.receptor.id = :usuario2Id) OR " +
            "(s.emisor.id = :usuario2Id AND s.receptor.id = :usuario1Id)")
    void   deleteByUsuarios(@Param("usuario1Id") Long usuario1Id, @Param("usuario2Id") Long usuario2Id);
}