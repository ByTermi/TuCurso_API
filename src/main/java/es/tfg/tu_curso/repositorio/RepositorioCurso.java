package es.tfg.tu_curso.repositorio;

import es.tfg.tu_curso.dto.CursoDTO;
import es.tfg.tu_curso.modelo.Curso;
import es.tfg.tu_curso.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Curso.
 * Proporciona métodos para realizar operaciones CRUD en la base de datos para los cursos
 * y métodos de consulta personalizados.
 */
@Repository
public interface RepositorioCurso extends JpaRepository<Curso, Long> {
    /**
     * Encuentra cursos por su nombre ignorando mayúsculas y minúsculas.
     *
     * @param nombre El nombre del curso a buscar
     * @return Lista de cursos que coinciden con el nombre especificado
     */
    List<Curso> findByNombreIgnoreCase(String nombre);

    /**
     * Encuentra cursos por su enlace.
     *
     * @param enlace El enlace del curso a buscar
     * @return Lista de cursos que coinciden con el enlace especificado
     */
    List<Curso> findByEnlace(String enlace);

    /**
     * Encuentra cursos que contengan una anotación específica.
     *
     * @param anotaciones La anotación a buscar dentro de los cursos
     * @return Lista de cursos que contienen la anotación especificada
     */
    List<Curso> findByAnotacionesContaining(String anotaciones);

    /**
     * Encuentra cursos que están marcados como finalizados.
     *
     * @return Lista de cursos finalizados
     */
    List<Curso> findByFinalizadoTrue();

    /**
     * Encuentra cursos que no están marcados como finalizados.
     *
     * @return Lista de cursos no finalizados
     */
    List<Curso> findByFinalizadoFalse();

    /**
     * Encuentra cursos con un precio menor que el valor especificado.
     *
     * @param precio El precio máximo para filtrar los cursos
     * @return Lista de cursos con precio menor que el especificado
     */
    List<Curso> findByPrecioLessThan(double precio);

    /**
     * Encuentra cursos con un precio mayor que el valor especificado.
     *
     * @param precio El precio mínimo para filtrar los cursos
     * @return Lista de cursos con precio mayor que el especificado
     */
    List<Curso> findByPrecioGreaterThan(double precio);

    /**
     * Encuentra cursos asociados a un usuario específico.
     *
     * @param usuario El usuario cuyo cursos se desean encontrar
     * @return Lista de cursos asociados al usuario especificado
     */
    List<Curso> findByUsuario(Usuario usuario);

    /**
     * Encuentra cursos asociados a un usuario por su ID.
     *
     * @param usuarioId El ID del usuario cuyos cursos se desean encontrar
     * @return Lista de cursos asociados al ID de usuario especificado
     */
    List<Curso> findByUsuarioId(Long usuarioId);

    /**
     * Cuenta el número de cursos asociados a un usuario específico.
     *
     * @param usuarioId El ID del usuario para el que se desea contar los cursos
     * @return El número de cursos asociados al usuario especificado
     */
    long countByUsuarioId(Long usuarioId);

    /**
     * Obtiene todos los cursos en formato DTO.
     *
     * @return Lista de todos los cursos en formato DTO
     */
    @Query("SELECT new es.tfg.tu_curso.dto.CursoDTO(c.id, c.nombre, c.enlace, c.precio, c.finalizado, c.anotaciones, c.usuario.id) FROM Curso c")
    List<CursoDTO> findAllCursosDTO();

    /**
     * Obtiene los cursos en formato DTO asociados a un usuario específico.
     *
     * @param usuarioId El ID del usuario cuyos cursos se desean obtener
     * @return Lista de cursos en formato DTO asociados al usuario especificado
     */
    @Query("SELECT new es.tfg.tu_curso.dto.CursoDTO(c.id, c.nombre, c.enlace, c.precio, c.finalizado, c.anotaciones, c.usuario.id) FROM Curso c WHERE c.usuario.id = :usuarioId")
    List<CursoDTO> findCursosDTOByUsuarioId(@Param("usuarioId") Long usuarioId);
}