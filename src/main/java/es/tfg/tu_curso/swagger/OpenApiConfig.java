package es.tfg.tu_curso.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI para la documentación de la API
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configuración personalizada de OpenAPI
     * @return Objeto OpenAPI configurado
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Tu Curso")
                        .description("API REST para la gestión de cursos, pomodoros y puntos de control")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Jaime")
                                .email("jaimenovillobenito.trabajo.com")
                                .url("https://tucurso.com"))
                        .license(new License()
                                .name("Licencia")
                                .url("https://tucurso.com/licencia")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor local")
                ));
    }
}