package es.tfg.tu_curso.servicio.interfaces;

import es.tfg.tu_curso.dto.PomodoroDTO;
import es.tfg.tu_curso.modelo.Pomodoro;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones relacionadas con la gestión de pomodoros.
 * Proporciona métodos para crear, modificar, eliminar y consultar pomodoros.
 */
public interface PomodoroServicio {
    /**
     * Crea un nuevo pomodoro asociado a un usuario.
     *
     * @param fechaHoraInicial  Fecha y hora de inicio del pomodoro
     * @param fechaHoraDestino  Fecha y hora de finalización del pomodoro
     * @param usuarioId         Identificador del usuario asociado al pomodoro
     * @return {@code true} si la creación fue exitosa, {@code false} en caso contrario
     */
    boolean crear(LocalDateTime fechaHoraInicial, LocalDateTime fechaHoraDestino, Long usuarioId);

    /**
     * Crea un nuevo pomodoro utilizando un objeto Pomodoro ya construido.
     *
     * @param pomodoro  Objeto Pomodoro con la información del pomodoro a crear
     * @param usuarioId Identificador del usuario asociado al pomodoro
     * @return {@code true} si la creación fue exitosa, {@code false} en caso contrario
     */
    boolean crear(Pomodoro pomodoro, Long usuarioId);

    /**
     * Elimina un pomodoro existente.
     *
     * @param idPomodoro Identificador del pomodoro a eliminar
     * @return {@code true} si la eliminación fue exitosa, {@code false} en caso contrario
     */
    boolean borrar(Long idPomodoro);

    /**
     * Modifica la información de un pomodoro existente.
     *
     * @param idPomodoro Identificador del pomodoro a modificar
     * @param pomodoro   Objeto Pomodoro con la nueva información
     * @return {@code true} si la modificación fue exitosa, {@code false} en caso contrario
     */
    boolean modificar(Long idPomodoro, Pomodoro pomodoro);

    /**
     * Obtiene la lista de todos los pomodoros registrados.
     *
     * @return Lista de DTOs con la información de los pomodoros
     */
    List<PomodoroDTO> obtenerPomodoros();

    /**
     * Obtiene la lista de pomodoros asociados a un usuario específico.
     *
     * @param usuarioId Identificador del usuario
     * @return Lista de DTOs con la información de los pomodoros del usuario
     */
    List<PomodoroDTO> obtenerPomodorosPorUsuario(Long usuarioId);

    /**
     * Busca un pomodoro por su identificador.
     *
     * @param idPomodoro Identificador del pomodoro a buscar
     * @return Optional conteniendo el DTO del pomodoro si existe, o vacío si no se encuentra
     */
    Optional<PomodoroDTO> obtenerPomodoroPorId(Long idPomodoro);

    /**
     * Obtiene la lista de pomodoros registrados en un rango de fechas específico.
     *
     * @param fechaInicio Fecha y hora de inicio del rango de búsqueda
     * @param fechaFin    Fecha y hora de fin del rango de búsqueda
     * @return Lista de DTOs con la información de los pomodoros en el rango especificado
     */
    List<PomodoroDTO> obtenerPomodorosEntreFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Cuenta el número total de pomodoros asociados a un usuario.
     *
     * @param usuarioId Identificador del usuario
     * @return Número de pomodoros del usuario
     */
    long contarPomodorosPorUsuario(Long usuarioId);
}