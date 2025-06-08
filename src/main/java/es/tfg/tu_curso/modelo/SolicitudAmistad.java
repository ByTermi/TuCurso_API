package es.tfg.tu_curso.modelo;

import jakarta.persistence.*;

@Entity
public class SolicitudAmistad {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emisor_id", nullable = false)
    private Usuario emisor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receptor_id", nullable = false)
    private Usuario receptor;

    // Constructores
    public SolicitudAmistad() {
    }

    public SolicitudAmistad(Usuario emisor, Usuario receptor) {
        this.emisor = emisor;
        this.receptor = receptor;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getEmisor() {
        return emisor;
    }

    public void setEmisor(Usuario emisor) {
        this.emisor = emisor;
    }

    public Usuario getReceptor() {
        return receptor;
    }

    public void setReceptor(Usuario receptor) {
        this.receptor = receptor;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SolicitudAmistad solicitud = (SolicitudAmistad) obj;
        return id != null && id.equals(solicitud.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}