package es.tfg.tu_curso.servicio.implementaciones;

import es.tfg.tu_curso.dto.PuntoDeControlDTO;
import es.tfg.tu_curso.modelo.Curso;
import es.tfg.tu_curso.modelo.PuntoDeControl;
import es.tfg.tu_curso.repositorio.RepositorioCurso;
import es.tfg.tu_curso.repositorio.RepositorioPuntoDeControl;
import es.tfg.tu_curso.servicio.interfaces.PuntoDeControlServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de gestión de puntos de control.
 * Proporciona la lógica de negocio para operaciones con puntos de control.
 */
@Service
public class PuntoDeControlServicioImpl implements PuntoDeControlServicio {

    /**
     * Repositorio para acceder a los datos de puntos de control.
     */
    @Autowired
    private RepositorioPuntoDeControl puntoDeControlRepositorio;

    /**
     * Repositorio para acceder a los datos de cursos.
     */
    @Autowired
    private RepositorioCurso cursoRepositorio;

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación verifica la existencia del curso antes de crear el punto de control.
     * </p>
     */
    @Override
    public boolean crear(String descripcion, Date fechaFinalizacionDeseada, boolean estaCompletado, Long cursoId) {
        // Verificar si el curso existe
        Optional<Curso> curso = cursoRepositorio.findById(cursoId);
        if (!curso.isPresent()) {
            return false; // El curso no existe
        }

        // Crear nuevo punto de control
        PuntoDeControl nuevoPuntoDeControl = new PuntoDeControl();
        nuevoPuntoDeControl.setDescripcion(descripcion);
        nuevoPuntoDeControl.setFechaFinalizacionDeseada(fechaFinalizacionDeseada);
        nuevoPuntoDeControl.setEstaCompletado(estaCompletado);
        nuevoPuntoDeControl.setCurso(curso.get());

        // Guardar el punto de control
        puntoDeControlRepositorio.save(nuevoPuntoDeControl);
        return true;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación verifica la existencia del curso antes de crear el punto de control.
     * </p>
     */
    @Override
    public boolean crear(PuntoDeControl puntoDeControl, Long cursoId) {
        // Verificar si el curso existe
        Optional<Curso> curso = cursoRepositorio.findById(cursoId);
        if (!curso.isPresent()) {
            return false; // El curso no existe
        }

        // Asignar el curso al punto de control
        puntoDeControl.setCurso(curso.get());

        // Guardar el punto de control
        puntoDeControlRepositorio.save(puntoDeControl);
        return true;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación verifica la existencia del punto de control antes de eliminarlo.
     * </p>
     */
    @Override
    public boolean borrar(Long idPuntoDeControl) {
        // Verificar si el punto de control existe
        Optional<PuntoDeControl> puntoDeControl = puntoDeControlRepositorio.findById(idPuntoDeControl);
        if (puntoDeControl.isPresent()) {
            puntoDeControlRepositorio.delete(puntoDeControl.get());
            return true; // Punto de control eliminado exitosamente
        }
        return false; // El punto de control no existe
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación verifica la existencia del punto de control y actualiza todos sus campos
     * con los valores proporcionados.
     * </p>
     */
    @Override
    public boolean modificar(Long idPuntoDeControl, PuntoDeControl puntoDeControlActualizado) {
        // Verificar si el punto de control existe
        Optional<PuntoDeControl> puntoDeControlExistente = puntoDeControlRepositorio.findById(idPuntoDeControl);
        if (puntoDeControlExistente.isPresent()) {
            PuntoDeControl puntoDeControl = puntoDeControlExistente.get();

            // Actualizar los campos del punto de control
            puntoDeControl.setDescripcion(puntoDeControlActualizado.getDescripcion());
            puntoDeControl.setFechaFinalizacionDeseada(puntoDeControlActualizado.getFechaFinalizacionDeseada());
            puntoDeControl.setEstaCompletado(puntoDeControlActualizado.isEstaCompletado());

            // Guardar el punto de control actualizado
            puntoDeControlRepositorio.save(puntoDeControl);
            return true; // Punto de control modificado exitosamente
        }
        return false; // El punto de control no existe
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación verifica la existencia del punto de control y actualiza
     * solo su estado de completado.
     * </p>
     */
    @Override
    public boolean marcarCompletado(Long idPuntoDeControl, boolean completado) {
        // Verificar si el punto de control existe
        Optional<PuntoDeControl> puntoDeControlExistente = puntoDeControlRepositorio.findById(idPuntoDeControl);
        if (puntoDeControlExistente.isPresent()) {
            PuntoDeControl puntoDeControl = puntoDeControlExistente.get();
            puntoDeControl.setEstaCompletado(completado);
            puntoDeControlRepositorio.save(puntoDeControl);
            return true; // Estado de completado actualizado correctamente
        }
        return false; // El punto de control no existe
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación utiliza un método específico del repositorio para
     * obtener directamente los DTO de puntos de control.
     * </p>
     */
    @Override
    public List<PuntoDeControlDTO> obtenerPuntosDeControl() {
        return puntoDeControlRepositorio.findAllPuntosDeControlDTO();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación utiliza un método específico del repositorio para
     * obtener directamente los DTO de puntos de control filtrados por curso.
     * </p>
     */
    @Override
    public List<PuntoDeControlDTO> obtenerPuntosDeControlPorCurso(Long cursoId) {
        return puntoDeControlRepositorio.findPuntoDeControlDTOByCursoId(cursoId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación convierte el modelo de punto de control a DTO si se encuentra.
     * </p>
     */
    @Override
    public Optional<PuntoDeControlDTO> obtenerPuntoDeControlPorId(Long idPuntoDeControl) {
        Optional<PuntoDeControl> puntoDeControl = puntoDeControlRepositorio.findById(idPuntoDeControl);
        return puntoDeControl.map(PuntoDeControlDTO::new);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación utiliza un método específico del repositorio para
     * obtener directamente los DTO de puntos de control pendientes, ordenados por fecha.
     * </p>
     */
    @Override
    public List<PuntoDeControlDTO> obtenerPuntosDeControlPendientes() {
        return puntoDeControlRepositorio.findPendingPuntosDeControlDTOOrderByDate();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación utiliza un método específico del repositorio para contar
     * el número total de puntos de control asociados a un curso.
     * </p>
     */
    @Override
    public long contarPuntosDeControlPorCurso(Long cursoId) {
        return puntoDeControlRepositorio.countByCursoId(cursoId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación utiliza un método específico del repositorio para contar
     * el número de puntos de control completados asociados a un curso.
     * </p>
     */
    @Override
    public long contarPuntosDeControlCompletadosPorCurso(Long cursoId) {
        return puntoDeControlRepositorio.countByCursoIdAndEstaCompletadoTrue(cursoId);
    }
}