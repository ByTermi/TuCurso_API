package es.tfg.tu_curso.modelo;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class PuntoDeControl {
    @Id
    @SequenceGenerator(name = "punto_de_control_seq", sequenceName = "punto_de_control_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "punto_de_control_seq")
    private Long id;
    private String descripcion;
    private Date fechaFinalizacionDeseada;
    private boolean estaCompletado;

    @ManyToOne
    private Curso curso;

    public PuntoDeControl() {
    }

    public PuntoDeControl(Long id, String descripcion, Date fechaFinalizacionDeseada, boolean estaCompletado) {
        this.id = id;
        this.descripcion = descripcion;
        this.fechaFinalizacionDeseada = fechaFinalizacionDeseada;
        this.estaCompletado = estaCompletado;
    }

    public PuntoDeControl(Long id, String descripcion, Date fechaFinalizacionDeseada, boolean estaCompletado, Curso curso) {
        this.id = id;
        this.descripcion = descripcion;
        this.fechaFinalizacionDeseada = fechaFinalizacionDeseada;
        this.estaCompletado = estaCompletado;
        this.curso = curso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaFinalizacionDeseada() {
        return fechaFinalizacionDeseada;
    }

    public void setFechaFinalizacionDeseada(Date fechaFinalizacionDeseada) {
        this.fechaFinalizacionDeseada = fechaFinalizacionDeseada;
    }

    public boolean isEstaCompletado() {
        return estaCompletado;
    }

    public void setEstaCompletado(boolean estaCompletado) {
        this.estaCompletado = estaCompletado;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}
