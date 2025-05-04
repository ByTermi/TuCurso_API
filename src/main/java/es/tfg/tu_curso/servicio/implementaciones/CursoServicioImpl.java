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

@Service
public class CursoServicioImpl implements CursoServicio {

    @Autowired
    private RepositorioCurso cursosRepositorio;

    @Autowired
    private RepositorioUsuario usuarioRepositorio;

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

    @Override
    public List<CursoDTO> obtenerCursos() {
        return cursosRepositorio.findAllCursosDTO();
    }

    @Override
    public List<CursoDTO> obtenerCursosPorUsuario(Long usuarioId) {
        return cursosRepositorio.findCursosDTOByUsuarioId(usuarioId);
    }

    @Override
    public Optional<CursoDTO> obtenerCursoPorId(Long idCurso) {
        Optional<Curso> curso = cursosRepositorio.findById(idCurso);
        return curso.map(CursoDTO::new);
    }

    @Override
    public long contarCursosPorUsuario(Long usuarioId) {
        return cursosRepositorio.countByUsuarioId(usuarioId);
    }
}