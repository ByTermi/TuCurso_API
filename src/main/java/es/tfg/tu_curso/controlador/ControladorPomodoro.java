package es.tfg.tu_curso.controlador;

import es.tfg.tu_curso.dto.PomodoroDTO;
import es.tfg.tu_curso.modelo.Pomodoro;
import es.tfg.tu_curso.servicio.interfaces.PomodoroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pomodoros")
public class ControladorPomodoro {

    @Autowired
    private PomodoroServicio pomodoroServicio;

    @PostMapping("/crear")
    public ResponseEntity<String> crearPomodoro(@RequestBody Pomodoro pomodoro, @RequestParam Long usuarioId) {
        if (pomodoroServicio.crear(pomodoro, usuarioId)) {
            return new ResponseEntity<>("Pomodoro creado exitosamente", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("No se pudo crear el pomodoro", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarPomodoro(@PathVariable Long id) {
        if (pomodoroServicio.borrar(id)) {
            return new ResponseEntity<>("Pomodoro eliminado correctamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo eliminar el pomodoro", HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> modificarPomodoro(@PathVariable Long id, @RequestBody Pomodoro pomodoro) {
        if (pomodoroServicio.modificar(id, pomodoro)) {
            return new ResponseEntity<>("Pomodoro modificado exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo modificar el pomodoro", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<PomodoroDTO>> obtenerPomodoros() {
        return new ResponseEntity<>(pomodoroServicio.obtenerPomodoros(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PomodoroDTO> obtenerPomodoroPorId(@PathVariable Long id) {
        Optional<PomodoroDTO> pomodoro = pomodoroServicio.obtenerPomodoroPorId(id);
        return pomodoro.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PomodoroDTO>> obtenerPomodorosPorUsuario(@PathVariable Long usuarioId) {
        List<PomodoroDTO> pomodoros = pomodoroServicio.obtenerPomodorosPorUsuario(usuarioId);
        return new ResponseEntity<>(pomodoros, HttpStatus.OK);
    }

    @GetMapping("/entre-fechas")
    public ResponseEntity<List<PomodoroDTO>> obtenerPomodorosEntreFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<PomodoroDTO> pomodoros = pomodoroServicio.obtenerPomodorosEntreFechas(fechaInicio, fechaFin);
        return new ResponseEntity<>(pomodoros, HttpStatus.OK);
    }

    @GetMapping("/contar/usuario/{usuarioId}")
    public ResponseEntity<Long> contarPomodorosPorUsuario(@PathVariable Long usuarioId) {
        long count = pomodoroServicio.contarPomodorosPorUsuario(usuarioId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}