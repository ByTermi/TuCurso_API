# TuCurso - Plataforma de Gestión de Cursos

## Configuración del Proyecto

### Requisitos Previos
- Java 17 o superior
- Maven

### Base de Datos H2
El proyecto utiliza H2 como base de datos con las siguientes configuraciones:
- URL: `jdbc:h2:./data/testdb`
- Usuario: `sa`
- Contraseña: vacía
- Consola H2: `http://localhost:8080/h2-console`
- Modo de actualización JPA: `update`

### Seguridad
- JWT con tiempo de expiración de 1 hora
- Clave secreta configurada en properties
- Usuario de prueba:
    - Usuario: `test`
    - Contraseña: `test`

### API Documentation
- OpenAPI docs: `http://localhost:8080/api-docs`
- Swagger UI: `http://localhost:8080/swagger-docs`

## Estructura de Seguridad

### Endpoints Públicos
- `/api-docs/**` - Documentación OpenAPI
- `/swagger-docs/**` - Documentación Swagger
- `/swagger-ui/**` - UI de Swagger
- `/swagger-ui.html` - Página principal Swagger
- `/v3/api-docs/**` - Documentación OpenAPI v3
- `/webjars/**` - Recursos web
- `/usuarios/crear` - Crear usuario
- `/usuarios/login` - Login de usuario
- `/usuarios/crear-admin-dev` - Crear admin (desarrollo)
- `/admin/login` - Login de admin
- `/admin/registro-admin-dev` - Registro admin (desarrollo)

## Ejecución Local

1. Clonar repositorio
2. Ejecutar:
```bash
mvn spring-boot:run