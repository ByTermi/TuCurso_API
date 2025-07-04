package es.tfg.tu_curso.modelo;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String nombre;
    private String descripcion;
    private String email;
    private String pass;
    private String icono;
    private String rol;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Curso> listaCursos;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pomodoro> listaPomodoros;

    // Relación Many-to-Many para lista de amigos
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuario_amigos",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "amigo_id")
    )
    private Set<Usuario> amigos = new HashSet<>();

    // Constructores existentes
    public Usuario() {
    }

    public Usuario(String nombre){
        this.nombre = nombre;
    }

    public Usuario(String nombre, String email, String pass) {
        this.nombre = nombre;
        this.email = email;
        this.pass = pass;
    }

    public Usuario(String nombre, String descripcion, String email, String pass, String icono) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.email = email;
        this.pass = pass;
        this.icono = icono;
    }

    public Usuario(Long id, String nombre, String descripcion, String email, String pass, String icono, List<Curso> listaCursos, List<Pomodoro> listaPomodoros) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.email = email;
        this.pass = pass;
        this.icono = icono;
        this.listaCursos = listaCursos;
        this.listaPomodoros = listaPomodoros;
    }

    // Métodos para gestionar amigos
    public void agregarAmigo(Usuario amigo) {
        if (amigo != null && !amigo.equals(this)) {
            this.amigos.add(amigo);
            amigo.getAmigos().add(this); // Relación bidireccional
        }
    }

    public void removerAmigo(Usuario amigo) {
        if (amigo != null) {
            this.amigos.remove(amigo);
            amigo.getAmigos().remove(this); // Relación bidireccional
        }
    }

    public boolean esAmigo(Usuario usuario) {
        return this.amigos.contains(usuario);
    }

    // Getters y Setters existentes
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public List<Curso> getListaCursos() {
        return listaCursos;
    }

    public void setListaCursos(List<Curso> listaCursos) {
        this.listaCursos = listaCursos;
    }

    public List<Pomodoro> getListaPomodoros() {
        return listaPomodoros;
    }

    public void setListaPomodoros(List<Pomodoro> listaPomodoros) {
        this.listaPomodoros = listaPomodoros;
    }

    // Getter y Setter para amigos
    public Set<Usuario> getAmigos() {
        return amigos;
    }

    public void setAmigos(Set<Usuario> amigos) {
        this.amigos = amigos;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return id != null && id.equals(usuario.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}