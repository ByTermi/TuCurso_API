package es.tfg.tu_curso.dto;

import es.tfg.tu_curso.modelo.Curso;

public class CursoDTO {

    private Long id;
    private String nombre;
    private String enlace;
    private double precio;
    private boolean finalizado;
    private String anotaciones;
    private Long usuarioId;

    public CursoDTO(Long id, String nombre, String enlace, double precio, boolean finalizado, String anotaciones, Long usuarioId) {
        this.id = id;
        this.nombre = nombre;
        this.enlace = enlace;
        this.precio = precio;
        this.finalizado = finalizado;
        this.anotaciones = anotaciones;
        this.usuarioId = usuarioId;
    }

    public CursoDTO() {}

    public CursoDTO(Curso curso) {
        this.id = curso.getId();
        this.nombre = curso.getNombre();
        this.enlace = curso.getEnlace();
        this.precio = curso.getPrecio();
        this.finalizado = curso.estaFinalizado();
        this.anotaciones = curso.getAnotaciones();
        if (curso.getUsuario() != null) {
            this.usuarioId = curso.getUsuario().getId();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public String getAnotaciones() {
        return anotaciones;
    }

    public void setAnotaciones(String anotaciones) {
        this.anotaciones = anotaciones;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}