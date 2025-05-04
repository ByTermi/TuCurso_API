package es.tfg.tu_curso.controlador;

import es.tfg.tu_curso.dto.CursoDTO;
import es.tfg.tu_curso.modelo.Curso;
import es.tfg.tu_curso.servicio.interfaces.CursoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cursos")
public class ControladorCurso {

    @Autowired
    private CursoServicio cursoServicio;

    @PostMapping("/crear")
    public ResponseEntity<String> crearCurso(@RequestBody Curso curso, @RequestParam Long usuarioId) {

        if (cursoServicio.crear(curso, usuarioId)) {
            return new ResponseEntity<>("Curso creado exitosamente", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("No se pudo crear el curso", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarCurso(@PathVariable Long id) {
        if (cursoServicio.borrar(id)) {
            return new ResponseEntity<>("Curso eliminado correctamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo eliminar el curso", HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> modificarCurso(@PathVariable Long id, @RequestBody Curso curso) {
        if (cursoServicio.modificar(id, curso)) {
            return new ResponseEntity<>("Curso modificado exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo modificar el curso", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<CursoDTO>> obtenerCursos() {
        return new ResponseEntity<>(cursoServicio.obtenerCursos(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO> obtenerCursoPorId(@PathVariable Long id) {
        Optional<CursoDTO> curso = cursoServicio.obtenerCursoPorId(id);
        return curso.map(c -> new ResponseEntity<>(c, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CursoDTO>> obtenerCursosPorUsuario(@PathVariable Long usuarioId) {
        List<CursoDTO> cursos = cursoServicio.obtenerCursosPorUsuario(usuarioId);
        return new ResponseEntity<>(cursos, HttpStatus.OK);
    }

    @GetMapping("/contar/usuario/{usuarioId}")
    public ResponseEntity<Long> contarCursosPorUsuario(@PathVariable Long usuarioId) {
        long count = cursoServicio.contarCursosPorUsuario(usuarioId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}