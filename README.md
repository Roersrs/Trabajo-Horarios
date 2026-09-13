BARRIO 13 - versión 1

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
