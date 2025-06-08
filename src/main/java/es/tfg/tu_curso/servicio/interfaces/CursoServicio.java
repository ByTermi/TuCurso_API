package es.tfg.tu_curso.servicio.interfaces;

import es.tfg.tu_curso.dto.CursoDTO;
import es.tfg.tu_curso.modelo.Curso;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones relacionadas con la gestión de cursos.
 * Proporciona métodos para crear, modificar, eliminar y consultar cursos.
 */
public interface CursoServicio {
    /**
     * Crea un nuevo curso asociado a un usuario.
     *
     * @param nombre      Nombre del curso
     * @param enlace      Enlace al recurso del curso
     * @param precio      Precio del curso
     * @param finalizado  Indica si el curso está finalizado
     * @param anotaciones Notas adicionales sobre el curso
     * @param usuarioId   Identificador del usuario propietario del curso
     * @return {@code true} si la creación fue exitosa, {@code false} en caso contrario
     */
    boolean crear(String nombre, String enlace, double precio, boolean finalizado, String anotaciones, Long usuarioId);

    /**
     * Crea un nuevo curso utilizando un objeto Curso ya construido.
     *
     * @param curso     Objeto Curso con la información del curso a crear
     * @param usuarioId Identificador del usuario propietario del curso
     * @return {@code true} si la creación fue exitosa, {@code false} en caso contrario
     */
    boolean crear(Curso curso, Long usuarioId);

    /**
     * Elimina un curso existente.
     *
     * @param idCurso Identificador del curso a eliminar
     * @return {@code true} si la eliminación fue exitosa, {@code false} en caso contrario
     */
    boolean borrar(Long idCurso);

    /**
     * Modifica la información de un curso existente.
     *
     * @param idCurso Identificador del curso a modificar
     * @param curso   Objeto Curso con la nueva información
     * @return {@code true} si la modificación fue exitosa, {@code false} en caso contrario
     */
    boolean modificar(Long idCurso, Curso curso);

    /**
     * Obtiene la lista de todos los cursos disponibles.
     *
     * @return Lista de DTOs con la información de los cursos
     */
    List<CursoDTO> obtenerCursos();

    /**
     * Obtiene la lista de cursos asociados a un usuario específico.
     *
     * @param usuarioId Identificador del usuario
     * @return Lista de DTOs con la información de los cursos del usuario
     */
    List<CursoDTO> obtenerCursosPorUsuario(Long usuarioId);

    /**
     * Busca un curso por su identificador.
     *
     * @param idCurso Identificador del curso a buscar
     * @return Optional conteniendo el DTO del curso si existe, o vacío si no se encuentra
     */
    Optional<CursoDTO> obtenerCursoPorId(Long idCurso);

    /**
     * Cuenta el número total de cursos asociados a un usuario.
     *
     * @param usuarioId Identificador del usuario
     * @return Número de cursos del usuario
     */
    long contarCursosPorUsuario(Long usuarioId);

    /**
     * Cuenta el número total de cursos registrados en el sistema.
     *
     * @return número total de cursos
     */
    long contarCursos();
}