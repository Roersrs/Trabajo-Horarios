DROP DATABASE IF EXISTS asos;
CREATE DATABASE asos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE asos;

CREATE TABLE usuarios (
 id INT AUTO_INCREMENT PRIMARY KEY,
 usuario VARCHAR(50) NOT NULL UNIQUE,
 correo VARCHAR(120) NOT NULL UNIQUE,
 clave VARCHAR(100) NOT NULL
);

CREATE TABLE jornadas (
 id INT AUTO_INCREMENT PRIMARY KEY,
 nombre VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE cursos (
 id INT AUTO_INCREMENT PRIMARY KEY,
 grado VARCHAR(20) NOT NULL,
 nombre VARCHAR(50) NOT NULL,
 numero_estudiantes INT NOT NULL DEFAULT 30,
 jornada_id INT NOT NULL,
 CONSTRAINT fk_curso_jornada FOREIGN KEY (jornada_id) REFERENCES jornadas(id)
);

CREATE TABLE docentes (
 id INT AUTO_INCREMENT PRIMARY KEY,
 nombre VARCHAR(100) NOT NULL,
 correo VARCHAR(120) NOT NULL UNIQUE,
 telefono VARCHAR(30),
 max_horas_diarias INT NOT NULL DEFAULT 8
);

CREATE TABLE asignaturas (
 id INT AUTO_INCREMENT PRIMARY KEY,
 nombre VARCHAR(100) NOT NULL UNIQUE,
 intensidad_horaria INT NOT NULL DEFAULT 2
);

CREATE TABLE horarios (
 id INT AUTO_INCREMENT PRIMARY KEY,
 curso_id INT NOT NULL,
 asignatura_id INT NOT NULL,
 docente_id INT NOT NULL,
 dia ENUM('Lunes','Martes','Miercoles','Jueves','Viernes') NOT NULL,
 hora_inicio TIME NOT NULL,
 hora_fin TIME NOT NULL,
 CONSTRAINT fk_horario_curso FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE,
 CONSTRAINT fk_horario_asignatura FOREIGN KEY (asignatura_id) REFERENCES asignaturas(id) ON DELETE RESTRICT,
 CONSTRAINT fk_horario_docente FOREIGN KEY (docente_id) REFERENCES docentes(id) ON DELETE RESTRICT,
 CONSTRAINT chk_horas CHECK (hora_fin > hora_inicio)
);

INSERT INTO usuarios(usuario,correo,clave) VALUES
('admin','admin@asos.local','admin123');

INSERT INTO jornadas(nombre) VALUES ('Mañana'),('Tarde');

INSERT INTO cursos(grado,nombre,numero_estudiantes,jornada_id) VALUES
('6','601',30,1),('6','602',29,1),('7','701',31,1),('7','702',30,1),
('8','801',28,1),('8','802',30,2),('9','901',32,2),('9','902',30,2),
('10','1001',28,2),('11','1101',27,2);

INSERT INTO docentes(nombre,correo,telefono,max_horas_diarias) VALUES
('Carlos Martinez','carlos@asos.local','3001110001',8),
('Laura Gomez','laura@asos.local','3001110002',8),
('Jorge Perez','jorge@asos.local','3001110003',8),
('Diana Rojas','diana@asos.local','3001110004',8),
('Andres Vargas','andres@asos.local','3001110005',8),
('Valentina Ramirez','valentina@asos.local','3001110006',8),
('Maria Salazar','maria@asos.local','3001110007',8),
('Luis Castro','luis@asos.local','3001110008',8),
('Andrea Silva','andrea@asos.local','3001110009',8),
('Felipe Mora','felipe@asos.local','3001110010',8);

INSERT INTO asignaturas(nombre,intensidad_horaria) VALUES
('Matematicas',5),('Español',5),('Ingles',4),('Ciencias',4),('Sociales',4),
('Informatica',2),('Educacion Fisica',2),('Etica',1),('Fisica',3),('Quimica',3);

-- 30 clases de ejemplo
INSERT INTO horarios(curso_id,asignatura_id,docente_id,dia,hora_inicio,hora_fin) VALUES
(1,1,1,'Lunes','06:30','07:30'),(2,1,1,'Lunes','07:30','08:30'),(3,1,1,'Lunes','08:30','09:30'),
(1,2,2,'Martes','06:30','07:30'),(2,2,2,'Martes','07:30','08:30'),(3,2,2,'Martes','08:30','09:30'),
(1,3,3,'Miercoles','06:30','07:30'),(2,3,3,'Miercoles','07:30','08:30'),(3,3,3,'Miercoles','08:30','09:30'),
(1,4,4,'Jueves','06:30','07:30'),(2,4,4,'Jueves','07:30','08:30'),(3,4,4,'Jueves','08:30','09:30'),
(1,5,5,'Viernes','06:30','07:30'),(2,5,5,'Viernes','07:30','08:30'),(3,5,5,'Viernes','08:30','09:30'),
(4,6,6,'Lunes','09:30','10:30'),(5,6,6,'Lunes','10:30','11:30'),(6,6,6,'Lunes','13:00','14:00'),
(4,7,7,'Martes','09:30','10:30'),(5,7,7,'Martes','10:30','11:30'),(6,7,7,'Martes','13:00','14:00'),
(4,8,8,'Miercoles','09:30','10:30'),(5,8,8,'Miercoles','10:30','11:30'),(6,8,8,'Miercoles','13:00','14:00'),
(7,9,9,'Jueves','13:00','14:00'),(8,9,9,'Jueves','14:00','15:00'),(9,9,9,'Jueves','15:00','16:00'),
(7,10,10,'Viernes','13:00','14:00'),(8,10,10,'Viernes','14:00','15:00'),(10,10,10,'Viernes','15:00','16:00');
