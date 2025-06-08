package es.tfg.tu_curso.repositorio;

import es.tfg.tu_curso.dto.PomodoroDTO;
import es.tfg.tu_curso.modelo.Pomodoro;
import es.tfg.tu_curso.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la entidad Pomodoro.
 * Proporciona métodos para realizar operaciones CRUD en la base de datos para los pomodoros
 * y métodos de consulta personalizados relacionados con la técnica Pomodoro.
 */
@Repository
public interface RepositorioPomodoro extends JpaRepository<Pomodoro, Long> {
    /**
     * Encuentra pomodoros asociados a un usuario específico.
     *
     * @param usuario El usuario cuyos pomodoros se desean encontrar
     * @return Lista de pomodoros asociados al usuario especificado
     */
    List<Pomodoro> findByUsuario(Usuario usuario);

    /**
     * Encuentra pomodoros asociados a un usuario por su ID.
     *
     * @param usuarioId El ID del usuario cuyos pomodoros se desean encontrar
     * @return Lista de pomodoros asociados al ID de usuario especificado
     */
    List<Pomodoro> findByUsuarioId(Long usuarioId);

    /**
     * Encuentra pomodoros con fecha de inicio posterior a la fecha especificada.
     *
     * @param fecha La fecha a partir de la cual buscar pomodoros
     * @return Lista de pomodoros iniciados después de la fecha especificada
     */
    List<Pomodoro> findByFechaHoraInicialAfter(LocalDateTime fecha);

    /**
     * Encuentra pomodoros con fecha de destino anterior a la fecha especificada.
     *
     * @param fecha La fecha hasta la cual buscar pomodoros
     * @return Lista de pomodoros con fecha de destino anterior a la fecha especificada
     */
    List<Pomodoro> findByFechaHoraDestinoLessThan(LocalDateTime fecha);

    /**
     * Encuentra pomodoros cuya fecha inicial esté dentro del rango especificado.
     *
     * @param fechaInicio La fecha de inicio del rango
     * @param fechaFin La fecha de fin del rango
     * @return Lista de pomodoros con fecha inicial dentro del rango especificado
     */
    List<Pomodoro> findByFechaHoraInicialBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Encuentra pomodoros cuya fecha de destino esté dentro del rango especificado.
     *
     * @param fechaInicio La fecha de inicio del rango
     * @param fechaFin La fecha de fin del rango
     * @return Lista de pomodoros con fecha de destino dentro del rango especificado
     */
    List<Pomodoro> findByFechaHoraDestinoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Cuenta el número de pomodoros asociados a un usuario específico.
     *
     * @param usuarioId El ID del usuario para el que se desea contar los pomodoros
     * @return El número de pomodoros asociados al usuario especificado
     */
    long countByUsuarioId(Long usuarioId);

    /**
     * Obtiene todos los pomodoros en formato DTO.
     *
     * @return Lista de todos los pomodoros en formato DTO
     */
    @Query("SELECT new es.tfg.tu_curso.dto.PomodoroDTO(p.id, p.fechaHoraInicial, p.fechaHoraDestino, p.usuario.id) FROM Pomodoro p")
    List<PomodoroDTO> findAllPomodorosDTO();

    /**
     * Obtiene los pomodoros en formato DTO asociados a un usuario específico.
     *
     * @param usuarioId El ID del usuario cuyos pomodoros se desean obtener
     * @return Lista de pomodoros en formato DTO asociados al usuario especificado
     */
    @Query("SELECT new es.tfg.tu_curso.dto.PomodoroDTO(p.id, p.fechaHoraInicial, p.fechaHoraDestino, p.usuario.id) FROM Pomodoro p WHERE p.usuario.id = :usuarioId")
    List<PomodoroDTO> findPomodoroDTOByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Obtiene los pomodoros en formato DTO cuya fecha inicial esté dentro del rango especificado.
     *
     * @param fechaInicio La fecha de inicio del rango
     * @param fechaFin La fecha de fin del rango
     * @return Lista de pomodoros en formato DTO dentro del rango de fechas especificado
     */
    @Query("SELECT new es.tfg.tu_curso.dto.PomodoroDTO(p.id, p.fechaHoraInicial, p.fechaHoraDestino, p.usuario.id) FROM Pomodoro p WHERE p.fechaHoraInicial BETWEEN :fechaInicio AND :fechaFin")
    List<PomodoroDTO> findPomodoroDTOBetweenDates(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
}