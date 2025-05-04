package es.tfg.tu_curso.servicio.interfaces;

import es.tfg.tu_curso.dto.PuntoDeControlDTO;
import es.tfg.tu_curso.modelo.PuntoDeControl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PuntoDeControlServicio {
    boolean crear(String descripcion, Date fechaFinalizacionDeseada, boolean estaCompletado, Long cursoId);
    boolean crear(PuntoDeControl puntoDeControl, Long cursoId);
    boolean borrar(Long idPuntoDeControl);
    boolean modificar(Long idPuntoDeControl, PuntoDeControl puntoDeControl);
    boolean marcarCompletado(Long idPuntoDeControl, boolean completado);
    List<PuntoDeControlDTO> obtenerPuntosDeControl();
    List<PuntoDeControlDTO> obtenerPuntosDeControlPorCurso(Long cursoId);
    Optional<PuntoDeControlDTO> obtenerPuntoDeControlPorId(Long idPuntoDeControl);
    List<PuntoDeControlDTO> obtenerPuntosDeControlPendientes();
    long contarPuntosDeControlPorCurso(Long cursoId);
    long contarPuntosDeControlCompletadosPorCurso(Long cursoId);
}