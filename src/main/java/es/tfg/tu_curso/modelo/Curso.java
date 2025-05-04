package es.tfg.tu_curso.modelo;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Curso {

    @Id
    @SequenceGenerator(name = "curso_seq", sequenceName = "curso_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "curso_seq")
    private Long id;
    private String nombre;
    private String enlace;
    private double precio;
    private boolean finalizado;
    private String anotaciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PuntoDeControl> listaPuntosDeControl;
    
    public Curso() {
    }

    public Curso(Long id, String nombre, String enlace, double precio, boolean finalizado, String anotaciones) {
        this.id = id;
        this.nombre = nombre;
        this.enlace = enlace;
        this.precio = precio;
        this.finalizado = finalizado;
        this.anotaciones = anotaciones;
    }

    public Curso(Long id, String nombre, String enlace, double precio, boolean finalizado, String anotaciones, Usuario usuario, List<PuntoDeControl> listaPuntosDeControl) {
        this.id = id;
        this.nombre = nombre;
        this.enlace = enlace;
        this.precio = precio;
        this.finalizado = finalizado;
        this.anotaciones = anotaciones;
        this.usuario = usuario;
        this.listaPuntosDeControl = listaPuntosDeControl;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public boolean estaFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean estaFinalizado) {
        this.finalizado = estaFinalizado;
    }

    public String getAnotaciones() {
        return anotaciones;
    }

    public void setAnotaciones(String anotaciones) {
        this.anotaciones = anotaciones;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<PuntoDeControl> getListaPuntosDeControl() {
        return listaPuntosDeControl;
    }

    public void setListaPuntosDeControl(List<PuntoDeControl> listaPuntosDeControl) {
        this.listaPuntosDeControl = listaPuntosDeControl;
    }
}
