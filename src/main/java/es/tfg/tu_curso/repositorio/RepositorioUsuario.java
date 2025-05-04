package es.tfg.tu_curso.repositorio;

import es.tfg.tu_curso.dto.UsuarioDTO;
import es.tfg.tu_curso.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioUsuario extends JpaRepository<Usuario,Long> {
    // Buscar un usuario por su email
    Optional<Usuario> findByEmail(String email);

    // Buscar usuarios por nombre (ignorando mayúsculas y minúsculas)
    List<Usuario> findByNombreIgnoreCase(String nombre);

    // Buscar usuarios que contengan una descripción específica
    List<Usuario> findByDescripcionContaining(String descripcion);

    // Buscar usuarios que tengan un icono específico
    List<Usuario> findByIcono(String icono);

    // Buscar usuarios que no tengan descripción
    List<Usuario> findByDescripcionIsNull();

    // Buscar usuarios que tengan una descripción
    List<Usuario> findByDescripcionIsNotNull();

    // Buscar usuarios por nombre y email
    List<Usuario> findByNombreAndEmail(String nombre, String email);

    // Contar el número de usuarios con un email específico
    long countByEmail(String email);

    // Eliminar usuarios por email
    void deleteByEmail(String email);

    @Query("SELECT new es.tfg.tu_curso.dto.UsuarioDTO(u.id, u.nombre, u.descripcion, u.icono) FROM Usuario u")
    List<UsuarioDTO> findAllUsuariosDTO();
}
