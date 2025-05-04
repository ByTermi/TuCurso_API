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

@Repository
public interface RepositorioPuntoDeControl extends JpaRepository<PuntoDeControl, Long> {
    // Buscar puntos de control por descripción que contiene
    List<PuntoDeControl> findByDescripcionContaining(String descripcion);

    // Buscar puntos de control completados
    List<PuntoDeControl> findByEstaCompletadoTrue();

    // Buscar puntos de control no completados
    List<PuntoDeControl> findByEstaCompletadoFalse();

    // Buscar puntos de control con fecha de finalización deseada antes de una fecha
    List<PuntoDeControl> findByFechaFinalizacionDeseadaBefore(Date fecha);

    // Buscar puntos de control con fecha de finalización deseada después de una fecha
    List<PuntoDeControl> findByFechaFinalizacionDeseadaAfter(Date fecha);

    // Buscar puntos de control entre dos fechas
    List<PuntoDeControl> findByFechaFinalizacionDeseadaBetween(Date fechaInicio, Date fechaFin);

    // Buscar puntos de control por curso
    List<PuntoDeControl> findByCurso(Curso curso);

    // Buscar puntos de control por ID de curso
    List<PuntoDeControl> findByCursoId(Long cursoId);

    // Contar puntos de control por curso
    long countByCursoId(Long cursoId);

    // Contar puntos de control completados por curso
    long countByCursoIdAndEstaCompletadoTrue(Long cursoId);

    // Query personalizada para obtener todos los PuntoDeControlDTO
    @Query("SELECT new es.tfg.tu_curso.dto.PuntoDeControlDTO(p.id, p.descripcion, p.fechaFinalizacionDeseada, p.estaCompletado, p.curso.id) FROM PuntoDeControl p")
    List<PuntoDeControlDTO> findAllPuntosDeControlDTO();

    // Query para obtener los PuntoDeControlDTO de un curso específico
    @Query("SELECT new es.tfg.tu_curso.dto.PuntoDeControlDTO(p.id, p.descripcion, p.fechaFinalizacionDeseada, p.estaCompletado, p.curso.id) FROM PuntoDeControl p WHERE p.curso.id = :cursoId")
    List<PuntoDeControlDTO> findPuntoDeControlDTOByCursoId(@Param("cursoId") Long cursoId);

    // Query para obtener puntos de control no completados ordenados por fecha
    @Query("SELECT new es.tfg.tu_curso.dto.PuntoDeControlDTO(p.id, p.descripcion, p.fechaFinalizacionDeseada, p.estaCompletado, p.curso.id) FROM PuntoDeControl p WHERE p.estaCompletado = false ORDER BY p.fechaFinalizacionDeseada")
    List<PuntoDeControlDTO> findPendingPuntosDeControlDTOOrderByDate();
}