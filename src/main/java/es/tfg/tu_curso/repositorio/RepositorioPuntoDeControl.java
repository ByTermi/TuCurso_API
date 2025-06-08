package es.tfg.tu_curso.repositorio;

import es.tfg.tu_curso.dto.PuntoDeControlDTO;
import es.tfg.tu_curso.modelo.Curso;
import es.tfg.tu_curso.modelo.PuntoDeControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Repositorio para la entidad PuntoDeControl.
 * Proporciona métodos para realizar operaciones CRUD en la base de datos para los puntos de control
 * y métodos de consulta personalizados para gestionar los hitos o checkpoints de los cursos.
 */
@Repository
public interface RepositorioPuntoDeControl extends JpaRepository<PuntoDeControl, Long> {
    /**
     * Encuentra puntos de control cuya descripción contiene el texto especificado.
     *
     * @param descripcion El texto a buscar dentro de las descripciones
     * @return Lista de puntos de control que contienen el texto especificado en su descripción
     */
    List<PuntoDeControl> findByDescripcionContaining(String descripcion);

    /**
     * Encuentra puntos de control que están marcados como completados.
     *
     * @return Lista de puntos de control completados
     */
    List<PuntoDeControl> findByEstaCompletadoTrue();

    /**
     * Encuentra puntos de control que no están marcados como completados.
     *
     * @return Lista de puntos de control no completados
     */
    List<PuntoDeControl> findByEstaCompletadoFalse();

    /**
     * Encuentra puntos de control con fecha de finalización deseada anterior a la fecha especificada.
     *
     * @param fecha La fecha límite para buscar puntos de control
     * @return Lista de puntos de control con fecha de finalización deseada anterior a la fecha especificada
     */
    List<PuntoDeControl> findByFechaFinalizacionDeseadaBefore(Date fecha);

    /**
     * Encuentra puntos de control con fecha de finalización deseada posterior a la fecha especificada.
     *
     * @param fecha La fecha a partir de la cual buscar puntos de control
     * @return Lista de puntos de control con fecha de finalización deseada posterior a la fecha especificada
     */
    List<PuntoDeControl> findByFechaFinalizacionDeseadaAfter(Date fecha);

    /**
     * Encuentra puntos de control cuya fecha de finalización deseada esté dentro del rango especificado.
     *
     * @param fechaInicio La fecha de inicio del rango
     * @param fechaFin La fecha de fin del rango
     * @return Lista de puntos de control con fecha de finalización deseada dentro del rango especificado
     */
    List<PuntoDeControl> findByFechaFinalizacionDeseadaBetween(Date fechaInicio, Date fechaFin);

    /**
     * Encuentra puntos de control asociados a un curso específico.
     *
     * @param curso El curso cuyos puntos de control se desean encontrar
     * @return Lista de puntos de control asociados al curso especificado
     */
    List<PuntoDeControl> findByCurso(Curso curso);

    /**
     * Encuentra puntos de control asociados a un curso por su ID.
     *
     * @param cursoId El ID del curso cuyos puntos de control se desean encontrar
     * @return Lista de puntos de control asociados al ID de curso especificado
     */
    List<PuntoDeControl> findByCursoId(Long cursoId);

    /**
     * Cuenta el número de puntos de control asociados a un curso específico.
     *
     * @param cursoId El ID del curso para el que se desea contar los puntos de control
     * @return El número de puntos de control asociados al curso especificado
     */
    long countByCursoId(Long cursoId);

    /**
     * Cuenta el número de puntos de control completados asociados a un curso específico.
     *
     * @param cursoId El ID del curso para el que se desea contar los puntos de control completados
     * @return El número de puntos de control completados asociados al curso especificado
     */
    long countByCursoIdAndEstaCompletadoTrue(Long cursoId);

    /**
     * Obtiene todos los puntos de control en formato DTO.
     *
     * @return Lista de todos los puntos de control en formato DTO
     */
    @Query("SELECT new es.tfg.tu_curso.dto.PuntoDeControlDTO(p.id, p.descripcion, p.fechaFinalizacionDeseada, p.estaCompletado, p.curso.id) FROM PuntoDeControl p")
    List<PuntoDeControlDTO> findAllPuntosDeControlDTO();

    /**
     * Obtiene los puntos de control en formato DTO asociados a un curso específico.
     *
     * @param cursoId El ID del curso cuyos puntos de control se desean obtener
     * @return Lista de puntos de control en formato DTO asociados al curso especificado
     */
    @Query("SELECT new es.tfg.tu_curso.dto.PuntoDeControlDTO(p.id, p.descripcion, p.fechaFinalizacionDeseada, p.estaCompletado, p.curso.id) FROM PuntoDeControl p WHERE p.curso.id = :cursoId")
    List<PuntoDeControlDTO> findPuntoDeControlDTOByCursoId(@Param("cursoId") Long cursoId);

    /**
     * Obtiene los puntos de control pendientes (no completados) ordenados por fecha de finalización deseada.
     *
     * @return Lista de puntos de control pendientes en formato DTO ordenados por fecha
     */
    @Query("SELECT new es.tfg.tu_curso.dto.PuntoDeControlDTO(p.id, p.descripcion, p.fechaFinalizacionDeseada, p.estaCompletado, p.curso.id) FROM PuntoDeControl p WHERE p.estaCompletado = false ORDER BY p.fechaFinalizacionDeseada")
    List<PuntoDeControlDTO> findPendingPuntosDeControlDTOOrderByDate();
}