package es.tfg.tu_curso.repositorio;

import es.tfg.tu_curso.dto.UsuarioDTO;
import es.tfg.tu_curso.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Repositorio para la entidad Usuario.
 * Proporciona métodos para realizar operaciones CRUD en la base de datos para los usuarios
 * y métodos de consulta personalizados para la gestión de usuarios en la aplicación.
 */
@Repository
public interface RepositorioUsuario extends JpaRepository<Usuario,Long> {
    /**
     * Encuentra un usuario por su dirección de email.
     *
     * @param email La dirección de email del usuario a buscar
     * @return Un Optional que puede contener el usuario si se encuentra
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Encuentra usuarios por su nombre ignorando mayúsculas y minúsculas.
     *
     * @param nombre El nombre a buscar
     * @return Lista de usuarios que coinciden con el nombre especificado
     */
    List<Usuario> findByNombreIgnoreCase(String nombre);

    /**
     * Encuentra usuarios cuya descripción contiene el texto especificado.
     *
     * @param descripcion El texto a buscar dentro de las descripciones
     * @return Lista de usuarios que contienen el texto especificado en su descripción
     */
    List<Usuario> findByDescripcionContaining(String descripcion);

    /**
     * Encuentra usuarios que tienen un icono específico.
     *
     * @param icono El icono a buscar
     * @return Lista de usuarios que tienen el icono especificado
     */
    List<Usuario> findByIcono(String icono);

    /**
     * Encuentra usuarios que no tienen descripción.
     *
     * @return Lista de usuarios sin descripción
     */
    List<Usuario> findByDescripcionIsNull();

    /**
     * Encuentra usuarios que tienen una descripción.
     *
     * @return Lista de usuarios con descripción
     */
    List<Usuario> findByDescripcionIsNotNull();

    /**
     * Encuentra usuarios por nombre y email.
     *
     * @param nombre El nombre del usuario
     * @param email El email del usuario
     * @return Lista de usuarios que coinciden con el nombre y email especificados
     */
    List<Usuario> findByNombreAndEmail(String nombre, String email);

    /**
     * Cuenta el número de usuarios con un email específico.
     *
     * @param email El email para el que se desea contar los usuarios
     * @return El número de usuarios con el email especificado
     */
    long countByEmail(String email);

    /**
     * Elimina usuarios por su email.
     *
     * @param email El email de los usuarios a eliminar
     */
    void deleteByEmail(String email);

    /**
     * Obtiene todos los usuarios en formato DTO.
     *
     * @return Lista de todos los usuarios en formato DTO
     */
    @Query("SELECT new es.tfg.tu_curso.dto.UsuarioDTO(u.id, u.nombre, u.descripcion, u.icono) FROM Usuario u")
    List<UsuarioDTO> findAllUsuariosDTO();

    // Métodos para gestión de amigos

    /**
     * Obtiene la lista de amigos de un usuario en formato DTO.
     *
     * @param usuarioId ID del usuario
     * @return Lista de amigos en formato DTO
     */
    @Query("SELECT new es.tfg.tu_curso.dto.UsuarioDTO(a.id, a.nombre, a.descripcion, a.icono) " +
            "FROM Usuario u JOIN u.amigos a WHERE u.id = :usuarioId")
    List<UsuarioDTO> findAmigosByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Busca usuarios que no son amigos del usuario actual y que coincidan con el nombre.
     *
     * @param usuarioId ID del usuario actual
     * @param nombre Nombre a buscar
     * @return Lista de usuarios que coinciden con la búsqueda y no son amigos
     */
    @Query("SELECT new es.tfg.tu_curso.dto.UsuarioDTO(u.id, u.nombre, u.descripcion, u.icono) " +
            "FROM Usuario u WHERE u.id != :usuarioId " +
            "AND u.nombre LIKE %:nombre% " +
            "AND u.id NOT IN (SELECT a.id FROM Usuario usr JOIN usr.amigos a WHERE usr.id = :usuarioId)")
    List<UsuarioDTO> findUsuariosNoAmigosByNombre(@Param("usuarioId") Long usuarioId, @Param("nombre") String nombre);

    /**
     * Verifica si dos usuarios son amigos.
     *
     * @param usuarioId ID del primer usuario
     * @param amigoId ID del segundo usuario
     * @return true si son amigos, false en caso contrario
     */
    @Query("SELECT COUNT(a) > 0 FROM Usuario u JOIN u.amigos a WHERE u.id = :usuarioId AND a.id = :amigoId")
    boolean sonAmigos(@Param("usuarioId") Long usuarioId, @Param("amigoId") Long amigoId);

    /**
     * Cuenta el número de amigos de un usuario.
     *
     * @param usuarioId ID del usuario
     * @return Número de amigos
     */
    @Query("SELECT COUNT(a) FROM Usuario u JOIN u.amigos a WHERE u.id = :usuarioId")
    long countAmigosByUsuarioId(@Param("usuarioId") Long usuarioId);
}