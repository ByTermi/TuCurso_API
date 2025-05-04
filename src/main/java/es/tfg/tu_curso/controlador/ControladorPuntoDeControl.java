package es.tfg.tu_curso.controlador;

import es.tfg.tu_curso.dto.PuntoDeControlDTO;
import es.tfg.tu_curso.modelo.PuntoDeControl;
import es.tfg.tu_curso.servicio.interfaces.PuntoDeControlServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/puntos-de-control")
public class ControladorPuntoDeControl {

    @Autowired
    private PuntoDeControlServicio puntoDeControlServicio;

    @PostMapping("/crear")
    public ResponseEntity<String> crearPuntoDeControl(@RequestBody PuntoDeControl puntoDeControl, @RequestParam Long cursoId) {
        if (puntoDeControlServicio.crear(puntoDeControl, cursoId)) {
            return new ResponseEntity<>("Punto de control creado exitosamente", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("No se pudo crear el punto de control", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarPuntoDeControl(@PathVariable Long id) {
        if (puntoDeControlServicio.borrar(id)) {
            return new ResponseEntity<>("Punto de control eliminado correctamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo eliminar el punto de control", HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> modificarPuntoDeControl(@PathVariable Long id, @RequestBody PuntoDeControl puntoDeControl) {
        if (puntoDeControlServicio.modificar(id, puntoDeControl)) {
            return new ResponseEntity<>("Punto de control modificado exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo modificar el punto de control", HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}/completado")
    public ResponseEntity<String> marcarCompletado(@PathVariable Long id, @RequestParam boolean completado) {
        if (puntoDeControlServicio.marcarCompletado(id, completado)) {
            String estado = completado ? "completado" : "pendiente";
            return new ResponseEntity<>("Punto de control marcado como " + estado + " exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo actualizar el estado del punto de control", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<PuntoDeControlDTO>> obtenerPuntosDeControl() {
        return new ResponseEntity<>(puntoDeControlServicio.obtenerPuntosDeControl(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PuntoDeControlDTO> obtenerPuntoDeControlPorId(@PathVariable Long id) {
        Optional<PuntoDeControlDTO> puntoDeControl = puntoDeControlServicio.obtenerPuntoDeControlPorId(id);
        return puntoDeControl.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<PuntoDeControlDTO>> obtenerPuntosDeControlPorCurso(@PathVariable Long cursoId) {
        List<PuntoDeControlDTO> puntosDeControl = puntoDeControlServicio.obtenerPuntosDeControlPorCurso(cursoId);
        return new ResponseEntity<>(puntosDeControl, HttpStatus.OK);
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<PuntoDeControlDTO>> obtenerPuntosDeControlPendientes() {
        List<PuntoDeControlDTO> puntosDeControl = puntoDeControlServicio.obtenerPuntosDeControlPendientes();
        return new ResponseEntity<>(puntosDeControl, HttpStatus.OK);
    }

    @GetMapping("/contar/curso/{cursoId}")
    public ResponseEntity<Long> contarPuntosDeControlPorCurso(@PathVariable Long cursoId) {
        long count = puntoDeControlServicio.contarPuntosDeControlPorCurso(cursoId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/contar/completados/curso/{cursoId}")
    public ResponseEntity<Long> contarPuntosDeControlCompletadosPorCurso(@PathVariable Long cursoId) {
        long count = puntoDeControlServicio.contarPuntosDeControlCompletadosPorCurso(cursoId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}