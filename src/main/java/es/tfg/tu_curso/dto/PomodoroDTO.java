package es.tfg.tu_curso.dto;

import es.tfg.tu_curso.modelo.Pomodoro;

import java.time.LocalDateTime;

public class PomodoroDTO {

    private Long id;
    private LocalDateTime fechaHoraInicial;
    private LocalDateTime fechaHoraDestino;
    private Long usuarioId;

    public PomodoroDTO(Long id, LocalDateTime fechaHoraInicial, LocalDateTime fechaHoraDestino, Long usuarioId) {
        this.id = id;
        this.fechaHoraInicial = fechaHoraInicial;
        this.fechaHoraDestino = fechaHoraDestino;
        this.usuarioId = usuarioId;
    }

    public PomodoroDTO() {}

    public PomodoroDTO(Pomodoro pomodoro) {
        this.id = pomodoro.getId();
        this.fechaHoraInicial = pomodoro.getFechaHoraInicial();
        this.fechaHoraDestino = pomodoro.getFechaHoraDestino();
        if (pomodoro.getUsuario() != null) {
            this.usuarioId = pomodoro.getUsuario().getId();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaHoraInicial() {
        return fechaHoraInicial;
    }

    public void setFechaHoraInicial(LocalDateTime fechaHoraInicial) {
        this.fechaHoraInicial = fechaHoraInicial;
    }

    public LocalDateTime getFechaHoraDestino() {
        return fechaHoraDestino;
    }

    public void setFechaHoraDestino(LocalDateTime fechaHoraDestino) {
        this.fechaHoraDestino = fechaHoraDestino;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}