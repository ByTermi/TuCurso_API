package es.tfg.tu_curso.modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Pomodoro {

    @Id
    @SequenceGenerator(name = "pomodoro_seq", sequenceName = "pomodoro_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pomodoro_seq")
    private Long id;
    private LocalDateTime fechaHoraInicial;
    private LocalDateTime fechaHoraDestino;

    @ManyToOne
    private Usuario usuario;

    public Pomodoro() {
    }

    public Pomodoro(Long id, LocalDateTime fechaHoraInicial, LocalDateTime fechaHoraDestino) {
        this.id = id;
        this.fechaHoraInicial = fechaHoraInicial;
        this.fechaHoraDestino = fechaHoraDestino;
    }

    public Pomodoro(Long id, LocalDateTime fechaHoraInicial, LocalDateTime fechaHoraDestino, Usuario usuario) {
        this.id = id;
        this.fechaHoraInicial = fechaHoraInicial;
        this.fechaHoraDestino = fechaHoraDestino;
        this.usuario = usuario;
    }

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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


}
