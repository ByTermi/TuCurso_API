package es.tfg.tu_curso.servicio.implementaciones;

import es.tfg.tu_curso.dto.UsuarioDTO;
import es.tfg.tu_curso.modelo.Usuario;
import es.tfg.tu_curso.repositorio.RepositorioUsuario;
import es.tfg.tu_curso.servicio.interfaces.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServicioImpl implements UsuarioServicio {


    @Autowired
    private RepositorioUsuario usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean crear(String nombre, String mail, String pass, String descripcion, String icono) {

        if (usuarioRepositorio.findByEmail(mail).isPresent()) {
            return false; // El usuario ya existe
        }
        Usuario nuevoUsuario = new Usuario(nombre, descripcion, mail, passwordEncoder.encode(pass), icono); // <-- Encriptar la contraseña
        nuevoUsuario.setRol("USER");
        usuarioRepositorio.save(nuevoUsuario);
        return true;
    }

    @Override
    public boolean crear(Usuario usuario) {

        if (usuarioRepositorio.findByEmail(usuario.getEmail()).isPresent()) {
            return false; // El usuario ya existe
        }

        usuario.setPass(passwordEncoder.encode(usuario.getPass()));


        usuario.setRol("USER");
        usuarioRepositorio.save(usuario);
        return true;
    }

    @Override
    public boolean borrar(Long idUsuario) {
        // Verificar si el usuario existe
        Optional<Usuario> usuario = usuarioRepositorio.findById(idUsuario);
        if (usuario.isPresent()) {
            usuarioRepositorio.delete(usuario.get());
            return true; // Usuario eliminado exitosamente
        }
        return false; // El usuario no existe
    }

    @Override
    public boolean modificar(Long idUsuario, Usuario u) {
        // Verificar si el usuario existe
        Optional<Usuario> usuarioExistente = usuarioRepositorio.findById(idUsuario);
        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();

            // Actualizar los campos del usuario
            usuario.setNombre(u.getNombre());
            usuario.setEmail(u.getEmail());

            // Encriptar la contraseña antes de guardarla
            usuario.setPass(passwordEncoder.encode(u.getPass()));

            usuario.setDescripcion(u.getDescripcion());
            usuario.setIcono(u.getIcono());

            // Guardar el usuario actualizado
            usuarioRepositorio.save(usuario);
            return true; // Usuario modificado exitosamente
        }
        return false; // El usuario no existe
    }

    @Override
    public List<UsuarioDTO> obtenerUsuarios() {
        return usuarioRepositorio.findAllUsuariosDTO();
    }

    @Override
    public Usuario obtenerPorEmail(String email) {
        return usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con el email: " + email));
    }



}