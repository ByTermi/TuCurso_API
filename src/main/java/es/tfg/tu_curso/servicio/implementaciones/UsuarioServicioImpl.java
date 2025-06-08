package es.tfg.tu_curso.servicio.implementaciones;

import es.tfg.tu_curso.dto.UsuarioDTO;
import es.tfg.tu_curso.modelo.Usuario;
import es.tfg.tu_curso.repositorio.RepositorioUsuario;
import es.tfg.tu_curso.servicio.interfaces.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de gestión de usuarios.
 * Proporciona la lógica de negocio para operaciones con usuarios,
 * incluyendo creación, modificación, eliminación y consulta,
 * así como gestión de relaciones de amistad.
 */
@Service
public class UsuarioServicioImpl implements UsuarioServicio {

    /**
     * Repositorio para acceder a los datos de usuarios.
     */
    @Autowired
    private RepositorioUsuario usuarioRepositorio;

    /**
     * Codificador de contraseñas para el manejo seguro de credenciales.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación verifica que no exista un usuario con el mismo email
     * antes de crear uno nuevo, y encripta la contraseña antes de almacenarla.
     * </p>
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación verifica que no exista un usuario con el mismo email
     * antes de crear uno nuevo, y encripta la contraseña del objeto usuario
     * antes de almacenarlo.
     * </p>
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación verifica que no exista un usuario con el mismo email
     * antes de crear uno nuevo administrador, y encripta la contraseña antes de almacenarla.
     * </p>
     */
    @Override
    public boolean crearAdministrador(String nombre, String email, String pass, String descripcion, String icono) {
        // Verificar si ya existe un usuario con ese email
        if (usuarioRepositorio.findByEmail(email).isPresent()) {
            return false; // El usuario ya existe
        }

        try {
            // Crear nuevo usuario administrador
            Usuario nuevoAdmin = new Usuario(nombre, descripcion, email, passwordEncoder.encode(pass), icono);
            nuevoAdmin.setRol("ADMIN"); // Establecer rol como administrador
            usuarioRepositorio.save(nuevoAdmin);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación verifica la existencia del usuario antes de eliminarlo.
     * </p>
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación verifica la existencia del usuario y actualiza todos sus campos
     * con los valores proporcionados, encriptando la nueva contraseña.
     * </p>
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación utiliza un método específico del repositorio para
     * obtener directamente los DTO de usuario.
     * </p>
     */
    @Override
    public List<UsuarioDTO> obtenerUsuarios() {
        return usuarioRepositorio.findAllUsuariosDTO();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación busca un usuario por su email y lanza una excepción
     * si no lo encuentra.
     * </p>
     */
    @Override
    public Usuario obtenerPorEmail(String email) {
        return usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con el email: " + email));
    }

    // Implementación de métodos para gestión de amigos

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación verifica que ambos usuarios existan y que no sean el mismo usuario
     * antes de establecer la relación de amistad bidireccional.
     * </p>
     */
    @Override
    @Transactional
    public boolean agregarAmigo(Long usuarioId, Long amigoId) {
        if (usuarioId.equals(amigoId)) {
            return false; // Un usuario no puede ser amigo de sí mismo
        }

        Optional<Usuario> usuarioOpt = usuarioRepositorio.findById(usuarioId);
        Optional<Usuario> amigoOpt = usuarioRepositorio.findById(amigoId);

        if (usuarioOpt.isPresent() && amigoOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            Usuario amigo = amigoOpt.get();

            if (!usuario.esAmigo(amigo)) {
                usuario.agregarAmigo(amigo);
                usuarioRepositorio.save(usuario);
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación verifica que ambos usuarios existan antes de
     * remover la relación de amistad bidireccional.
     * </p>
     */
    @Override
    @Transactional
    public boolean removerAmigo(Long usuarioId, Long amigoId) {
        Optional<Usuario> usuarioOpt = usuarioRepositorio.findById(usuarioId);
        Optional<Usuario> amigoOpt = usuarioRepositorio.findById(amigoId);

        if (usuarioOpt.isPresent() && amigoOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            Usuario amigo = amigoOpt.get();

            if (usuario.esAmigo(amigo)) {
                usuario.removerAmigo(amigo);
                usuarioRepositorio.save(usuario);
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación utiliza un método específico del repositorio para
     * obtener directamente los DTO de los amigos de un usuario.
     * </p>
     */
    @Override
    public List<UsuarioDTO> obtenerAmigos(Long usuarioId) {
        return usuarioRepositorio.findAmigosByUsuarioId(usuarioId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación busca usuarios que coincidan con el nombre proporcionado
     * y que no sean amigos del usuario actual.
     * </p>
     */
    @Override
    public List<UsuarioDTO> buscarUsuariosParaAgregar(Long usuarioId, String nombre) {
        return usuarioRepositorio.findUsuariosNoAmigosByNombre(usuarioId, nombre);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación verifica si existe una relación de amistad entre dos usuarios.
     * </p>
     */
    @Override
    public boolean sonAmigos(Long usuarioId, Long amigoId) {
        return usuarioRepositorio.sonAmigos(usuarioId, amigoId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación cuenta el número de amigos de un usuario específico.
     * </p>
     */
    @Override
    public long contarAmigos(Long usuarioId) {
        return usuarioRepositorio.countAmigosByUsuarioId(usuarioId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación utiliza el método count() del repositorio
     * para obtener el número total de usuarios registrados en el sistema.
     * </p>
     */
    @Override
    public long contarUsuarios() {
        return usuarioRepositorio.count();
    }

    @Override
    public boolean cambiarContrasena(Long id, String nuevaContrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepositorio.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setPass(passwordEncoder.encode(nuevaContrasena));
            usuarioRepositorio.save(usuario);
            return true;
        }
        throw new RuntimeException("Usuario no encontrado con ID: " + id);
    }

}