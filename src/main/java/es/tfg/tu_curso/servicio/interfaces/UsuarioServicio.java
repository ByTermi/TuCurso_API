package es.tfg.tu_curso.servicio.interfaces;

import es.tfg.tu_curso.dto.UsuarioDTO;
import es.tfg.tu_curso.modelo.Usuario;

import java.util.List;

public interface UsuarioServicio {
    boolean crear(String nombre, String mail, String pass, String descripcion, String icono);
    boolean crear(Usuario usuario);
    boolean borrar(Long idUsuario);
    boolean modificar(Long idUsuario, Usuario u);
    List<UsuarioDTO> obtenerUsuarios();
    Usuario obtenerPorEmail(String email);
}
