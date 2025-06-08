package es.tfg.tu_curso.servicio.implementaciones;

import es.tfg.tu_curso.dto.PomodoroDTO;
import es.tfg.tu_curso.modelo.Pomodoro;
import es.tfg.tu_curso.modelo.Usuario;
import es.tfg.tu_curso.repositorio.RepositorioPomodoro;
import es.tfg.tu_curso.repositorio.RepositorioUsuario;
import es.tfg.tu_curso.servicio.interfaces.PomodoroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de gestión de pomodoros.
 * Proporciona la lógica de negocio para operaciones con pomodoros.
 */
@Service
public class PomodoroServicioImpl implements PomodoroServicio {

    /**
     * Repositorio para acceder a los datos de pomodoros.
     */
    @Autowired
    private RepositorioPomodoro pomodoroRepositorio;

    /**
     * Repositorio para acceder a los datos de usuarios.
     */
    @Autowired
    private RepositorioUsuario usuarioRepositorio;

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación verifica la existencia del usuario antes de crear el pomodoro.
     * </p>
     */
    @Override
    public boolean crear(LocalDateTime fechaHoraInicial, LocalDateTime fechaHoraDestino, Long usuarioId) {
        // Verificar si el usuario existe
        Optional<Usuario> usuario = usuarioRepositorio.findById(usuarioId);
        if (!usuario.isPresent()) {
            return false; // El usuario no existe
        }

        // Crear nuevo pomodoro
        Pomodoro nuevoPomodoro = new Pomodoro();
        nuevoPomodoro.setFechaHoraInicial(fechaHoraInicial);
        nuevoPomodoro.setFechaHoraDestino(fechaHoraDestino);
        nuevoPomodoro.setUsuario(usuario.get());

        // Guardar el pomodoro
        pomodoroRepositorio.save(nuevoPomodoro);
        return true;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación verifica la existencia del usuario antes de crear el pomodoro.
     * </p>
     */
    @Override
    public boolean crear(Pomodoro pomodoro, Long usuarioId) {
        // Verificar si el usuario existe
        Optional<Usuario> usuario = usuarioRepositorio.findById(usuarioId);
        if (!usuario.isPresent()) {
            return false; // El usuario no existe
        }

        // Asignar el usuario al pomodoro
        pomodoro.setUsuario(usuario.get());

        // Guardar el pomodoro
        pomodoroRepositorio.save(pomodoro);
        return true;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación verifica la existencia del pomodoro antes de eliminarlo.
     * </p>
     */
    @Override
    public boolean borrar(Long idPomodoro) {
        // Verificar si el pomodoro existe
        Optional<Pomodoro> pomodoro = pomodoroRepositorio.findById(idPomodoro);
        if (pomodoro.isPresent()) {
            pomodoroRepositorio.delete(pomodoro.get());
            return true; // Pomodoro eliminado exitosamente
        }
        return false; // El pomodoro no existe
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación verifica la existencia del pomodoro y actualiza todos sus campos
     * con los valores proporcionados.
     * </p>
     */
    @Override
    public boolean modificar(Long idPomodoro, Pomodoro pomodoroActualizado) {
        // Verificar si el pomodoro existe
        Optional<Pomodoro> pomodoroExistente = pomodoroRepositorio.findById(idPomodoro);
        if (pomodoroExistente.isPresent()) {
            Pomodoro pomodoro = pomodoroExistente.get();

            // Actualizar los campos del pomodoro
            pomodoro.setFechaHoraInicial(pomodoroActualizado.getFechaHoraInicial());
            pomodoro.setFechaHoraDestino(pomodoroActualizado.getFechaHoraDestino());

            // Guardar el pomodoro actualizado
            pomodoroRepositorio.save(pomodoro);
            return true; // Pomodoro modificado exitosamente
        }
        return false; // El pomodoro no existe
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación utiliza un método específico del repositorio para
     * obtener directamente los DTO de pomodoro.
     * </p>
     */
    @Override
    public List<PomodoroDTO> obtenerPomodoros() {
        return pomodoroRepositorio.findAllPomodorosDTO();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación utiliza un método específico del repositorio para
     * obtener directamente los DTO de pomodoro filtrados por usuario.
     * </p>
     */
    @Override
    public List<PomodoroDTO> obtenerPomodorosPorUsuario(Long usuarioId) {
        return pomodoroRepositorio.findPomodoroDTOByUsuarioId(usuarioId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación convierte el modelo de pomodoro a DTO si se encuentra.
     * </p>
     */
    @Override
    public Optional<PomodoroDTO> obtenerPomodoroPorId(Long idPomodoro) {
        Optional<Pomodoro> pomodoro = pomodoroRepositorio.findById(idPomodoro);
        return pomodoro.map(PomodoroDTO::new);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación utiliza un método específico del repositorio para
     * obtener directamente los DTO de pomodoro filtrados por rango de fechas.
     * </p>
     */
    @Override
    public List<PomodoroDTO> obtenerPomodorosEntreFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return pomodoroRepositorio.findPomodoroDTOBetweenDates(fechaInicio, fechaFin);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esta implementación utiliza un método específico del repositorio para contar
     * el número de pomodoros asociados a un usuario.
     * </p>
     */
    @Override
    public long contarPomodorosPorUsuario(Long usuarioId) {
        return pomodoroRepositorio.countByUsuarioId(usuarioId);
    }
}