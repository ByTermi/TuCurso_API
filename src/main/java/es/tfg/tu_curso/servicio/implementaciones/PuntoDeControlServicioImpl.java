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

@Service
public class PuntoDeControlServicioImpl implements PuntoDeControlServicio {

    @Autowired
    private RepositorioPuntoDeControl puntoDeControlRepositorio;

    @Autowired
    private RepositorioCurso cursoRepositorio;

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

    @Override
    public List<PuntoDeControlDTO> obtenerPuntosDeControl() {
        return puntoDeControlRepositorio.findAllPuntosDeControlDTO();
    }

    @Override
    public List<PuntoDeControlDTO> obtenerPuntosDeControlPorCurso(Long cursoId) {
        return puntoDeControlRepositorio.findPuntoDeControlDTOByCursoId(cursoId);
    }

    @Override
    public Optional<PuntoDeControlDTO> obtenerPuntoDeControlPorId(Long idPuntoDeControl) {
        Optional<PuntoDeControl> puntoDeControl = puntoDeControlRepositorio.findById(idPuntoDeControl);
        return puntoDeControl.map(PuntoDeControlDTO::new);
    }

    @Override
    public List<PuntoDeControlDTO> obtenerPuntosDeControlPendientes() {
        return puntoDeControlRepositorio.findPendingPuntosDeControlDTOOrderByDate();
    }

    @Override
    public long contarPuntosDeControlPorCurso(Long cursoId) {
        return puntoDeControlRepositorio.countByCursoId(cursoId);
    }

    @Override
    public long contarPuntosDeControlCompletadosPorCurso(Long cursoId) {
        return puntoDeControlRepositorio.countByCursoIdAndEstaCompletadoTrue(cursoId);
    }
}