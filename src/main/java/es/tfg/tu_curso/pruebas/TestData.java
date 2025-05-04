package es.tfg.tu_curso.pruebas;
import es.tfg.tu_curso.modelo.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class TestData {

    public static List<Usuario> crearUsuariosDePrueba() {
        // =============================================
        // Usuario 1: Juan Pérez (con 2 cursos y 2 pomodoros)
        // =============================================
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNombre("Juan Pérez");
        usuario1.setEmail("juan@example.com");
        usuario1.setPass("pass123");
        usuario1.setRol("USER");

        // Curso 1 de Juan
        Curso curso1 = new Curso(
                1L,
                "Curso Spring Boot Avanzado",
                "https://ejemplo.com/spring",
                199.99,
                false,
                "Curso sobre Spring Boot 3 y Microservicios",
                usuario1,
                List.of(
                        new PuntoDeControl(1L, "Configuración inicial", new Date(), false, null),
                        new PuntoDeControl(2L, "Despliegue en AWS", new Date(), true, null)
                )
        );

        // Curso 2 de Juan
        Curso curso2 = new Curso(
                2L,
                "React desde Cero",
                "https://ejemplo.com/react",
                149.99,
                true,
                "Curso completo de React 18",
                usuario1,
                List.of(
                        new PuntoDeControl(3L, "Componentes básicos", new Date(), true, null)
                )
        );

        // Pomodoros de Juan
        List<Pomodoro> pomodorosJuan = List.of(
                new Pomodoro(1L,
                        LocalDateTime.of(2024, 5, 1, 10, 0),
                        LocalDateTime.of(2024, 5, 1, 10, 25),
                        usuario1
                ),
                new Pomodoro(2L,
                        LocalDateTime.of(2024, 5, 2, 15, 30),
                        LocalDateTime.of(2024, 5, 2, 15, 55),
                        usuario1
                )
        );

        usuario1.setListaCursos(List.of(curso1, curso2));
        usuario1.setListaPomodoros(pomodorosJuan);

        /*// =============================================
        // Usuario 2: Ana Gómez (con 1 curso y 1 pomodoro)
        // =============================================
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNombre("Ana Gómez");
        usuario2.setEmail("ana@example.com");
        usuario2.setPass("pass456");
        usuario2.setRol("ADMIN");

        // Curso de Ana
        Curso curso3 = new Curso(
                3L,
                "Machine Learning Básico",
                "https://ejemplo.com/ml",
                299.99,
                false,
                "Introducción a ML con Python",
                usuario2,
                List.of(
                        new PuntoDeControl(4L, "Instalación de herramientas", new Date(), true, null),
                        new PuntoDeControl(5L, "Regresión lineal", new Date(), false, null),
                        new PuntoDeControl(6L, "Clasificación con SVM", new Date(), false, null)
                )
        );

        // Pomodoro de Ana
        usuario2.setListaCursos(List.of(curso3));
        usuario2.setListaPomodoros(List.of(
                new Pomodoro(3L,
                        LocalDateTime.of(2024, 5, 3, 9, 0),
                        LocalDateTime.of(2024, 5, 3, 9, 25),
                        usuario2
                )
        ));

        // =============================================
        // Usuario 3: Carlos Ruiz (con 1 curso y 1 pomodoro)
        // =============================================
        Usuario usuario3 = new Usuario();
        usuario3.setId(3L);
        usuario3.setNombre("Carlos Ruiz");
        usuario3.setEmail("carlos@example.com");
        usuario3.setPass("pass789");
        usuario3.setRol("USER");

        // Curso de Carlos
        Curso curso4 = new Curso(
                4L,
                "DevOps Essentials",
                "https://ejemplo.com/devops",
                249.99,
                false,
                "Fundamentos de CI/CD y Docker",
                usuario3,
                List.of(
                        new PuntoDeControl(7L, "Configuración de pipeline", new Date(), false, null)
                )
        );

        // Pomodoro de Carlos
        usuario3.setListaCursos(List.of(curso4));
        usuario3.setListaPomodoros(List.of(
                new Pomodoro(4L,
                        LocalDateTime.of(2024, 5, 4, 14, 0),
                        LocalDateTime.of(2024, 5, 4, 14, 25),
                        usuario3
                )
        ));
*/
        return List.of(usuario1/*, usuario2, usuario3*/);
    }
}