package es.tfg.tu_curso.dto;

import es.tfg.tu_curso.modelo.SolicitudAmistad;

public class SolicitudAmistadDTO {

    private Long id;
    private UsuarioDTO emisor;
    private UsuarioDTO receptor;

    // Constructores
    public SolicitudAmistadDTO() {
    }

    public SolicitudAmistadDTO(Long id, UsuarioDTO emisor, UsuarioDTO receptor) {
        this.id = id;
        this.emisor = emisor;
        this.receptor = receptor;
    }

    public SolicitudAmistadDTO(SolicitudAmistad solicitud) {
        this.id = solicitud.getId();
        this.emisor = new UsuarioDTO(solicitud.getEmisor());
        this.receptor = new UsuarioDTO(solicitud.getReceptor());
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsuarioDTO getEmisor() {
        return emisor;
    }

    public void setEmisor(UsuarioDTO emisor) {
        this.emisor = emisor;
    }

    public UsuarioDTO getReceptor() {
        return receptor;
    }

    public void setReceptor(UsuarioDTO receptor) {
        this.receptor = receptor;
    }
}