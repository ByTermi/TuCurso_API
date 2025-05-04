package es.tfg.tu_curso.servicio.interfaces;

import es.tfg.tu_curso.dto.PomodoroDTO;
import es.tfg.tu_curso.modelo.Pomodoro;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PomodoroServicio {
    boolean crear(LocalDateTime fechaHoraInicial, LocalDateTime fechaHoraDestino, Long usuarioId);
    boolean crear(Pomodoro pomodoro, Long usuarioId);
    boolean borrar(Long idPomodoro);
    boolean modificar(Long idPomodoro, Pomodoro pomodoro);
    List<PomodoroDTO> obtenerPomodoros();
    List<PomodoroDTO> obtenerPomodorosPorUsuario(Long usuarioId);
    Optional<PomodoroDTO> obtenerPomodoroPorId(Long idPomodoro);
    List<PomodoroDTO> obtenerPomodorosEntreFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    long contarPomodorosPorUsuario(Long usuarioId);
}