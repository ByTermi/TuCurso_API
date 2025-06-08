package es.tfg.tu_curso;

import es.tfg.tu_curso.modelo.Curso;
import es.tfg.tu_curso.modelo.Usuario;
import es.tfg.tu_curso.servicio.interfaces.UsuarioServicio;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication(scanBasePackages = "es.tfg.tu_curso")
public class ProyectoGrupalApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(ProyectoGrupalApplication.class, args);

        /*UsuarioServicio usuarioServicio = context.getBean(UsuarioServicio.class);

        // Crear un usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre("Nuevo Usuario");
        nuevoUsuario.setEmail("nuevo.usuario@example.com");
        nuevoUsuario.setPass("password123");
        nuevoUsuario.setRol("USER");

        // Crear un curso y asociarlo al usuario
        Curso nuevoCurso = new Curso();
        nuevoCurso.setNombre("Curso de Java");
        nuevoCurso.setAnotaciones("Curso básico de Java");
        nuevoCurso.setPrecio(99.99);
        nuevoCurso.setUsuario(nuevoUsuario);

        // Asociar el curso al usuario
        nuevoUsuario.setListaCursos(List.of(nuevoCurso));

        // Guardar el usuario (y el curso asociado) en la base de datos
        usuarioServicio.crear(nuevoUsuario);*/
    }
}