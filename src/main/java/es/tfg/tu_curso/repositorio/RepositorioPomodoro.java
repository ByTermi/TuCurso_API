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

@Repository
public interface RepositorioPomodoro extends JpaRepository<Pomodoro, Long> {
    // Buscar pomodoros por usuario
    List<Pomodoro> findByUsuario(Usuario usuario);

    // Buscar pomodoros por ID de usuario
    List<Pomodoro> findByUsuarioId(Long usuarioId);

    // Buscar pomodoros con fecha de inicio posterior a una fecha específica
    List<Pomodoro> findByFechaHoraInicialAfter(LocalDateTime fecha);

    // Buscar pomodoros con fecha de destino anterior a una fecha específica
    List<Pomodoro> findByFechaHoraDestinoLessThan(LocalDateTime fecha);

    // Buscar pomodoros entre dos fechas iniciales
    List<Pomodoro> findByFechaHoraInicialBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Buscar pomodoros entre dos fechas de destino
    List<Pomodoro> findByFechaHoraDestinoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Contar pomodoros por usuario
    long countByUsuarioId(Long usuarioId);

    // Query personalizada para obtener todos los PomodoroDTO
    @Query("SELECT new es.tfg.tu_curso.dto.PomodoroDTO(p.id, p.fechaHoraInicial, p.fechaHoraDestino, p.usuario.id) FROM Pomodoro p")
    List<PomodoroDTO> findAllPomodorosDTO();

    // Query para obtener los PomodoroDTO de un usuario específico
    @Query("SELECT new es.tfg.tu_curso.dto.PomodoroDTO(p.id, p.fechaHoraInicial, p.fechaHoraDestino, p.usuario.id) FROM Pomodoro p WHERE p.usuario.id = :usuarioId")
    List<PomodoroDTO> findPomodoroDTOByUsuarioId(@Param("usuarioId") Long usuarioId);

    // Query para obtener los PomodoroDTO entre dos fechas
    @Query("SELECT new es.tfg.tu_curso.dto.PomodoroDTO(p.id, p.fechaHoraInicial, p.fechaHoraDestino, p.usuario.id) FROM Pomodoro p WHERE p.fechaHoraInicial BETWEEN :fechaInicio AND :fechaFin")
    List<PomodoroDTO> findPomodoroDTOBetweenDates(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
}