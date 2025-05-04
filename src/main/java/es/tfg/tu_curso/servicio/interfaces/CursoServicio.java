package es.tfg.tu_curso.servicio.interfaces;

import es.tfg.tu_curso.dto.CursoDTO;
import es.tfg.tu_curso.modelo.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoServicio {
    boolean crear(String nombre, String enlace, double precio, boolean finalizado, String anotaciones, Long usuarioId);
    boolean crear(Curso curso, Long usuarioId);
    boolean borrar(Long idCurso);
    boolean modificar(Long idCurso, Curso curso);
    List<CursoDTO> obtenerCursos();
    List<CursoDTO> obtenerCursosPorUsuario(Long usuarioId);
    Optional<CursoDTO> obtenerCursoPorId(Long idCurso);
    long contarCursosPorUsuario(Long usuarioId);
}