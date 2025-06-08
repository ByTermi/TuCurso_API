package es.tfg.tu_curso.servicio.interfaces;

import es.tfg.tu_curso.dto.PuntoDeControlDTO;
import es.tfg.tu_curso.modelo.PuntoDeControl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones relacionadas con la gestión de puntos de control.
 * Proporciona métodos para crear, modificar, eliminar y consultar puntos de control
 * asociados a los cursos.
 */
public interface PuntoDeControlServicio {
    /**
     * Crea un nuevo punto de control asociado a un curso.
     *
     * @param descripcion            Descripción del punto de control
     * @param fechaFinalizacionDeseada Fecha límite deseada para completar el punto de control
     * @param estaCompletado         Indica si el punto de control ya está completado
     * @param cursoId                Identificador del curso al que pertenece el punto de control
     * @return {@code true} si la creación fue exitosa, {@code false} en caso contrario
     */
    boolean crear(String descripcion, Date fechaFinalizacionDeseada, boolean estaCompletado, Long cursoId);

    /**
     * Crea un nuevo punto de control utilizando un objeto PuntoDeControl ya construido.
     *
     * @param puntoDeControl Objeto PuntoDeControl con la información del punto de control a crear
     * @param cursoId        Identificador del curso al que pertenece el punto de control
     * @return {@code true} si la creación fue exitosa, {@code false} en caso contrario
     */
    boolean crear(PuntoDeControl puntoDeControl, Long cursoId);

    /**
     * Elimina un punto de control existente.
     *
     * @param idPuntoDeControl Identificador del punto de control a eliminar
     * @return {@code true} si la eliminación fue exitosa, {@code false} en caso contrario
     */
    boolean borrar(Long idPuntoDeControl);

    /**
     * Modifica la información de un punto de control existente.
     *
     * @param idPuntoDeControl Identificador del punto de control a modificar
     * @param puntoDeControl   Objeto PuntoDeControl con la nueva información
     * @return {@code true} si la modificación fue exitosa, {@code false} en caso contrario
     */
    boolean modificar(Long idPuntoDeControl, PuntoDeControl puntoDeControl);

    /**
     * Marca un punto de control como completado o pendiente.
     *
     * @param idPuntoDeControl Identificador del punto de control a actualizar
     * @param completado       {@code true} para marcar como completado, {@code false} para marcar como pendiente
     * @return {@code true} si la actualización fue exitosa, {@code false} en caso contrario
     */
    boolean marcarCompletado(Long idPuntoDeControl, boolean completado);

    /**
     * Obtiene la lista de todos los puntos de control registrados.
     *
     * @return Lista de DTOs con la información de los puntos de control
     */
    List<PuntoDeControlDTO> obtenerPuntosDeControl();

    /**
     * Obtiene la lista de puntos de control asociados a un curso específico.
     *
     * @param cursoId Identificador del curso
     * @return Lista de DTOs con la información de los puntos de control del curso
     */
    List<PuntoDeControlDTO> obtenerPuntosDeControlPorCurso(Long cursoId);

    /**
     * Busca un punto de control por su identificador.
     *
     * @param idPuntoDeControl Identificador del punto de control a buscar
     * @return Optional conteniendo el DTO del punto de control si existe, o vacío si no se encuentra
     */
    Optional<PuntoDeControlDTO> obtenerPuntoDeControlPorId(Long idPuntoDeControl);

    /**
     * Obtiene la lista de todos los puntos de control pendientes.
     *
     * @return Lista de DTOs con la información de los puntos de control pendientes
     */
    List<PuntoDeControlDTO> obtenerPuntosDeControlPendientes();

    /**
     * Cuenta el número total de puntos de control asociados a un curso.
     *
     * @param cursoId Identificador del curso
     * @return Número de puntos de control del curso
     */
    long contarPuntosDeControlPorCurso(Long cursoId);

    /**
     * Cuenta el número de puntos de control completados asociados a un curso.
     *
     * @param cursoId Identificador del curso
     * @return Número de puntos de control completados del curso
     */
    long contarPuntosDeControlCompletadosPorCurso(Long cursoId);
}