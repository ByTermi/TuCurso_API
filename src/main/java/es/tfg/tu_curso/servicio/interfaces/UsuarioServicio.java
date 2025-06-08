package es.tfg.tu_curso.servicio.interfaces;

import es.tfg.tu_curso.dto.UsuarioDTO;
import es.tfg.tu_curso.modelo.Usuario;

import java.util.List;

/**
 * Interfaz que define las operaciones relacionadas con la gestión de usuarios.
 * Proporciona métodos para crear, modificar, eliminar y consultar usuarios del sistema,
 * así como para gestionar las relaciones de amistad entre usuarios.
 */
public interface UsuarioServicio {
    /**
     * Crea un nuevo usuario en el sistema.
     *
     * @param nombre      Nombre del usuario
     * @param mail        Correo electrónico del usuario, que servirá como identificador único
     * @param pass        Contraseña del usuario
     * @param descripcion Descripción o biografía del usuario
     * @param icono       Ruta o identificador del icono de perfil del usuario
     * @return {@code true} si la creación fue exitosa, {@code false} en caso contrario
     */
    boolean crear(String nombre, String mail, String pass, String descripcion, String icono);

    /**
     * Crea un nuevo usuario utilizando un objeto Usuario ya construido.
     *
     * @param usuario Objeto Usuario con la información del usuario a crear
     * @return {@code true} si la creación fue exitosa, {@code false} en caso contrario
     */
    boolean crear(Usuario usuario);

    /**
     * Crea un nuevo usuario con rol de administrador.
     *
     * @param nombre Nombre del administrador
     * @param email Email del administrador
     * @param pass Contraseña del administrador
     * @param descripcion Descripción del administrador
     * @param icono URL o ruta del icono del administrador
     * @return true si la creación fue exitosa, false en caso contrario
     */
    boolean crearAdministrador(String nombre, String email, String pass,
                               String descripcion, String icono);

    /**
     * Elimina un usuario existente.
     *
     * @param idUsuario Identificador del usuario a eliminar
     * @return {@code true} si la eliminación fue exitosa, {@code false} en caso contrario
     */
    boolean borrar(Long idUsuario);

    /**
     * Modifica la información de un usuario existente.
     *
     * @param idUsuario Identificador del usuario a modificar
     * @param u         Objeto Usuario con la nueva información
     * @return {@code true} si la modificación fue exitosa, {@code false} en caso contrario
     */
    boolean modificar(Long idUsuario, Usuario u);

    /**
     * Obtiene la lista de todos los usuarios registrados en el sistema.
     *
     * @return Lista de DTOs con la información de los usuarios
     */
    List<UsuarioDTO> obtenerUsuarios();

    /**
     * Busca un usuario por su dirección de correo electrónico.
     *
     * @param email Dirección de correo electrónico del usuario a buscar
     * @return Objeto Usuario correspondiente al email proporcionado o {@code null} si no se encuentra
     */
    Usuario obtenerPorEmail(String email);

    // Métodos para gestión de amigos

    /**
     * Agrega un amigo a la lista de amigos de un usuario.
     *
     * @param usuarioId ID del usuario que quiere agregar un amigo
     * @param amigoId   ID del usuario que será agregado como amigo
     * @return {@code true} si se agregó exitosamente, {@code false} en caso contrario
     */
    boolean agregarAmigo(Long usuarioId, Long amigoId);

    /**
     * Remueve un amigo de la lista de amigos de un usuario.
     *
     * @param usuarioId ID del usuario que quiere remover un amigo
     * @param amigoId   ID del usuario que será removido de la lista de amigos
     * @return {@code true} si se removió exitosamente, {@code false} en caso contrario
     */
    boolean removerAmigo(Long usuarioId, Long amigoId);

    /**
     * Obtiene la lista de amigos de un usuario.
     *
     * @param usuarioId ID del usuario
     * @return Lista de amigos en formato DTO
     */
    List<UsuarioDTO> obtenerAmigos(Long usuarioId);

    /**
     * Busca usuarios que no son amigos del usuario actual y que coincidan con el nombre.
     *
     * @param usuarioId ID del usuario actual
     * @param nombre    Nombre a buscar
     * @return Lista de usuarios potenciales para agregar como amigos
     */
    List<UsuarioDTO> buscarUsuariosParaAgregar(Long usuarioId, String nombre);

    /**
     * Verifica si dos usuarios son amigos.
     *
     * @param usuarioId ID del primer usuario
     * @param amigoId   ID del segundo usuario
     * @return {@code true} si son amigos, {@code false} en caso contrario
     */
    boolean sonAmigos(Long usuarioId, Long amigoId);

    /**
     * Obtiene el número de amigos de un usuario.
     *
     * @param usuarioId ID del usuario
     * @return Número de amigos del usuario
     */
    long contarAmigos(Long usuarioId);

    /**
     * Cuenta el número total de usuarios registrados en el sistema.
     *
     * @return número total de usuarios
     */
    long contarUsuarios();

    /**
     * Cambia la contraseña de un usuario específico
     *
     * @param id ID del usuario
     * @param nuevaContrasena Nueva contraseña del usuario
     * @return true si el cambio fue exitoso, false en caso contrario
     * @throws RuntimeException si el usuario no existe
     */
    boolean cambiarContrasena(Long id, String nuevaContrasena);

}