CREATE DATABASE IF NOT EXISTS ClienteServidor;
USE ClienteServidorDB;

-- Eliminar tablas existentes si existen
DROP TABLE IF EXISTS mensajes_chat;
DROP TABLE IF EXISTS tickets;
DROP TABLE IF EXISTS usuarios;

-- Tabla de usuarios
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    tipo VARCHAR(20) NOT NULL -- 'empleado' o 'tecnico'
);

-- Tabla de tickets
CREATE TABLE tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    urgencia INT NOT NULL,
    departamento VARCHAR(50) NOT NULL,
    empleado_nombre VARCHAR(50) NOT NULL,
    tecnico_asignado VARCHAR(50) DEFAULT NULL,
    estado VARCHAR(20) DEFAULT 'PENDIENTE', -- PENDIENTE, EN_PROCESO, COMPLETADO
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_completado TIMESTAMP NULL
);

-- Tabla de mensajes del chat por ticket
CREATE TABLE mensajes_chat (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id INT NOT NULL,
    remitente VARCHAR(50) NOT NULL,
    tipo_remitente VARCHAR(20) NOT NULL, -- EMPLEADO o TECNICO
    mensaje TEXT NOT NULL,
    fecha_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE
);

-- Insertar usuarios de prueba
INSERT INTO usuarios (nombre, password, tipo) VALUES
('sergio','123','empleado'),
('sebas','123','empleado'),
('kelvin','123','tecnico'),
('jose','123','tecnico');

-- Insertar algunos tickets de ejemplo
INSERT INTO tickets (descripcion, urgencia, departamento, empleado_nombre, estado) VALUES
('No puedo acceder al sistema', 3, 'IT', 'sergio', 'PENDIENTE'),
('Impresora no funciona', 2, 'Administración', 'sebas', 'PENDIENTE'),
('Problema con el aire acondicionado', 4, 'Mantenimiento', 'sergio', 'PENDIENTE');

-- Insertar algunos mensajes de ejemplo
INSERT INTO mensajes_chat (ticket_id, remitente, tipo_remitente, mensaje) VALUES
(1, 'sergio', 'EMPLEADO', 'Hola, necesito ayuda urgente'),
(1, 'kelvin', 'TECNICO', 'Hola Sergio, ¿puedes darme más detalles del problema?'),
(1, 'sergio', 'EMPLEADO', 'No me deja ingresar mi usuario y contraseña'),
(2, 'sebas', 'EMPLEADO', 'La impresora del segundo piso no imprime nada');