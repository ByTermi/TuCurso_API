package es.tfg.tu_curso.seguridad;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



/**
 * Configuración de seguridad de la aplicación.
 * Define la configuración de Spring Security para proteger los endpoints de la API,
 * incluyendo reglas de acceso, gestión de autenticación y filtros de seguridad.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Filtro para procesar y validar tokens JWT en las solicitudes entrantes.
     */
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    /**
     * Servicio para cargar los detalles del usuario desde la base de datos.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Configura la cadena de filtros de seguridad para la aplicación.
     * Define qué endpoints están protegidos, qué tipo de autenticación se requiere,
     * y cómo se gestionan las sesiones.
     *
     * @param http Objeto HttpSecurity para configurar las reglas de seguridad
     * @return La cadena de filtros de seguridad configurada
     * @throws Exception Si ocurre un error durante la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api-docs/**", "/swagger-docs/**",
                                "/swagger-ui/**", "/swagger-ui.html",
                                "/v3/api-docs/**", "/webjars/**").permitAll()
                        .requestMatchers("/usuarios/crear", "/usuarios/login", "/usuarios/crear-admin-dev").permitAll()
                        .requestMatchers("/admin/login", "/admin/registro-admin-dev").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint((request, response, authException) -> {
                            System.out.println("=== Security Debug ===");
                            System.out.println("Request URL: " + request.getRequestURL());
                            System.out.println("Method: " + request.getMethod());
                            System.out.println("Auth Header: " + request.getHeader("Authorization"));
                            System.out.println("Error: " + authException.getMessage());
                            System.out.println("===================");

                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"error\": \"" + authException.getMessage() + "\"}");
                        })
                );

        return http.build();
    }

    /**
     * Define el gestor de autenticación global de la aplicación.
     * Establece el servicio de detalles de usuario y el codificador de contraseñas.
     *
     * @param auth Objeto AuthenticationManagerBuilder para configurar la autenticación
     * @throws Exception Si ocurre un error durante la configuración
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * Define el codificador de contraseñas a utilizar en la aplicación.
     * Se utiliza BCrypt para el hash seguro de contraseñas.
     *
     * @return Un codificador de contraseñas BCrypt
     */
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura el gestor de autenticación global de la aplicación.
     * Establece el servicio de detalles de usuario y el codificador de contraseñas.
     *
     * @param http Objeto HttpSecurity para configurar las reglas de seguridad
     * @return El gestor de autenticación configurado
     * @throws Exception Si ocurre un error durante la configuración
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
}