package es.tfg.tu_curso.dto;

import es.tfg.tu_curso.modelo.Usuario;

public class UsuarioDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private String icono;

    public UsuarioDTO(Long id, String nombre, String descripcion, String icono) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.icono = icono;
    }
    public UsuarioDTO() {}

    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.descripcion = usuario.getDescripcion();
        this.icono = usuario.getIcono();
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }
}
