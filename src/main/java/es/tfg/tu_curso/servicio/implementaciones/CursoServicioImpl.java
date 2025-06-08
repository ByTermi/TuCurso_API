package es.tfg.tu_curso.servicio.implementaciones;

import es.tfg.tu_curso.dto.CursoDTO;
import es.tfg.tu_curso.modelo.Curso;
import es.tfg.tu_curso.modelo.Usuario;

import es.tfg.tu_curso.repositorio.RepositorioCurso;
import es.tfg.tu_curso.repositorio.RepositorioUsuario;
import es.tfg.tu_curso.servicio.interfaces.CursoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de gestión de cursos.
 * Proporciona la lógica de negocio para operaciones con cursos.
 */
@Service
public class CursoServicioImpl implements CursoServicio {

    /**
     * Repositorio para acceder a los datos de cursos.
     */
    @Autowired
    private RepositorioCurso cursosRepositorio;

    /**
     * Repositorio para acceder a los datos de usuarios.
     */
    @Autowired
    private RepositorioUsuario usuarioRepositorio;

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación verifica la existencia del usuario antes de crear el curso.
     * </p>
     */
    @Override
    public boolean crear(String nombre, String enlace, double precio, boolean finalizado, String anotaciones, Long usuarioId) {
        // Verificar si el usuario existe
        Optional<Usuario> usuario = usuarioRepositorio.findById(usuarioId);
        if (!usuario.isPresent()) {
            return false; // El usuario no existe
        }

        // Crear nuevo curso
        Curso nuevoCurso = new Curso();
        nuevoCurso.setNombre(nombre);
        nuevoCurso.setEnlace(enlace);
        nuevoCurso.setPrecio(precio);
        nuevoCurso.setFinalizado(finalizado);
        nuevoCurso.setAnotaciones(anotaciones);
        nuevoCurso.setUsuario(usuario.get());

        // Guardar el curso
        cursosRepositorio.save(nuevoCurso);
        return true;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación verifica la existencia del usuario antes de crear el curso
     * y establece el ID como nulo para asegurar la creación de un nuevo registro.
     * </p>
     */
    @Override
    public boolean crear(Curso curso, Long usuarioId) {
        // Verificar si el usuario existe
        Optional<Usuario> usuario = usuarioRepositorio.findById(usuarioId);
        if (!usuario.isPresent()) {
            return false; // El usuario no existe
        }

        // Asignar el usuario al curso
        curso.setUsuario(usuario.get());

        // Guardar el curso
        curso.setId(null);
        cursosRepositorio.save(curso);
        return true;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación verifica la existencia del curso antes de eliminarlo.
     * </p>
     */
    @Override
    public boolean borrar(Long idCurso) {
        // Verificar si el curso existe
        Optional<Curso> curso = cursosRepositorio.findById(idCurso);
        if (curso.isPresent()) {
            cursosRepositorio.delete(curso.get());
            return true; // Curso eliminado exitosamente
        }
        return false; // El curso no existe
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación verifica la existencia del curso y actualiza todos sus campos
     * con los valores proporcionados.
     * </p>
     */
    @Override
    public boolean modificar(Long idCurso, Curso cursoActualizado) {
        // Verificar si el curso existe
        Optional<Curso> cursoExistente = cursosRepositorio.findById(idCurso);
        if (cursoExistente.isPresent()) {
            Curso curso = cursoExistente.get();

            // Actualizar los campos del curso
            curso.setNombre(cursoActualizado.getNombre());
            curso.setEnlace(cursoActualizado.getEnlace());
            curso.setPrecio(cursoActualizado.getPrecio());
            curso.setFinalizado(cursoActualizado.estaFinalizado());
            curso.setAnotaciones(cursoActualizado.getAnotaciones());

            // Guardar el curso actualizado
            cursosRepositorio.save(curso);
            return true; // Curso modificado exitosamente
        }
        return false; // El curso no existe
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación utiliza un método específico del repositorio para
     * obtener directamente los DTO de curso.
     * </p>
     */
    @Override
    public List<CursoDTO> obtenerCursos() {
        return cursosRepositorio.findAllCursosDTO();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación utiliza un método específico del repositorio para
     * obtener directamente los DTO de curso filtrados por usuario.
     * </p>
     */
    @Override
    public List<CursoDTO> obtenerCursosPorUsuario(Long usuarioId) {
        return cursosRepositorio.findCursosDTOByUsuarioId(usuarioId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación convierte el modelo de curso a DTO si se encuentra.
     * </p>
     */
    @Override
    public Optional<CursoDTO> obtenerCursoPorId(Long idCurso) {
        Optional<Curso> curso = cursosRepositorio.findById(idCurso);
        return curso.map(CursoDTO::new);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación utiliza un método específico del repositorio para contar
     * el número de cursos asociados a un usuario.
     * </p>
     */
    @Override
    public long contarCursosPorUsuario(Long usuarioId) {
        return cursosRepositorio.countByUsuarioId(usuarioId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación utiliza el método count() del repositorio
     * para obtener el número total de cursos en el sistema.
     * </p>
     */
    @Override
    public long contarCursos() {
        return cursosRepositorio.count();
    }
}