package es.tfg.tu_curso.repositorio;

import es.tfg.tu_curso.dto.CursoDTO;
import es.tfg.tu_curso.modelo.Curso;
import es.tfg.tu_curso.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioCurso extends JpaRepository<Curso, Long> {
    // Buscar cursos por nombre (ignorando mayúsculas y minúsculas)
    List<Curso> findByNombreIgnoreCase(String nombre);

    // Buscar cursos por enlace
    List<Curso> findByEnlace(String enlace);

    // Buscar cursos que contengan una anotación específica
    List<Curso> findByAnotacionesContaining(String anotaciones);

    // Buscar cursos finalizados
    List<Curso> findByFinalizadoTrue();

    // Buscar cursos no finalizados
    List<Curso> findByFinalizadoFalse();

    // Buscar cursos por precio menor que un valor
    List<Curso> findByPrecioLessThan(double precio);

    // Buscar cursos por precio mayor que un valor
    List<Curso> findByPrecioGreaterThan(double precio);

    // Buscar cursos por usuario
    List<Curso> findByUsuario(Usuario usuario);

    // Buscar cursos por ID de usuario
    List<Curso> findByUsuarioId(Long usuarioId);

    // Contar cursos por usuario
    long countByUsuarioId(Long usuarioId);

    // Query personalizada para obtener todos los CursoDTO
    @Query("SELECT new es.tfg.tu_curso.dto.CursoDTO(c.id, c.nombre, c.enlace, c.precio, c.finalizado, c.anotaciones, c.usuario.id) FROM Curso c")
    List<CursoDTO> findAllCursosDTO();

    // Query para obtener los CursoDTO de un usuario específico
    @Query("SELECT new es.tfg.tu_curso.dto.CursoDTO(c.id, c.nombre, c.enlace, c.precio, c.finalizado, c.anotaciones, c.usuario.id) FROM Curso c WHERE c.usuario.id = :usuarioId")
    List<CursoDTO> findCursosDTOByUsuarioId(@Param("usuarioId") Long usuarioId);
}