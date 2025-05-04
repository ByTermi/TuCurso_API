package es.tfg.tu_curso.dto;

import es.tfg.tu_curso.modelo.PuntoDeControl;

import java.util.Date;

public class PuntoDeControlDTO {

    private Long id;
    private String descripcion;
    private Date fechaFinalizacionDeseada;
    private boolean estaCompletado;
    private Long cursoId;

    public PuntoDeControlDTO(Long id, String descripcion, Date fechaFinalizacionDeseada, boolean estaCompletado, Long cursoId) {
        this.id = id;
        this.descripcion = descripcion;
        this.fechaFinalizacionDeseada = fechaFinalizacionDeseada;
        this.estaCompletado = estaCompletado;
        this.cursoId = cursoId;
    }

    public PuntoDeControlDTO() {}

    public PuntoDeControlDTO(PuntoDeControl puntoDeControl) {
        this.id = puntoDeControl.getId();
        this.descripcion = puntoDeControl.getDescripcion();
        this.fechaFinalizacionDeseada = puntoDeControl.getFechaFinalizacionDeseada();
        this.estaCompletado = puntoDeControl.isEstaCompletado();
        if (puntoDeControl.getCurso() != null) {
            this.cursoId = puntoDeControl.getCurso().getId();
        }
    }

    // Getters and Setters
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

    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
    }
}