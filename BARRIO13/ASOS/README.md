BARRIO 13 - versión sencilla (sin API REST)

Requisitos:
- Java 21
- Tomcat 10.1.x o TomEE 10
- MySQL 8.x
- NetBeans (opcional)

Pasos:
1. En MySQL Workbench abre y ejecuta: src/java/Modelo/BD.sql
2. La base creada se llama: asos
3. En src/java/conf/ConexionBD.java cambia USUARIO y CONTRASENA si tu MySQL no usa root/root.
4. Abre el proyecto en NetBeans y configura Tomcat 10.1.x.
5. Clean and Build y luego Run.
6. Login de prueba: admin / admin123

El proyecto NO usa APIs REST ni fetch(). Los formularios JSP envían directamente a Servlets por POST.
Reglas de horarios:
- No cruces para el mismo docente.
- No cruces para el mismo curso.
- Un docente no puede superar su max_horas_diarias (máximo 8 desde el formulario).

## Consulta de horario por docente
El administrador puede abrir **Horario docente** desde el menú, seleccionar un profesor y consultar sus materias, cursos, días, hora de inicio y hora de fin. También existe un botón **Ver horario** en la lista de docentes.

Esta función usa las tablas `docentes`, `horarios`, `asignaturas` y `cursos` existentes; no requiere cambios adicionales en la base de datos.
