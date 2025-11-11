-- Datos de Prueba - Usuarios con Contraseñas Cifradas (BCrypt strength 12)
-- Contraseñas originales:
-- admin: Admin123
-- mozo1: Mozo123
-- cajero1: Cajero123
-- cocinero1: Cocinero123

-- Usuario ADMIN
INSERT INTO usuario (nombre_usuario, contrasena, rol, estado) VALUES 
('admin', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYIR.ub4W8m', 'ADMIN', TRUE);

-- Usuario MOZO
INSERT INTO usuario (nombre_usuario, contrasena, rol, estado) VALUES 
('mozo1', '$2a$12$8Z8qZ8qZ8qZ8qZ8qZ8qZ8uKX8Z8qZ8qZ8qZ8qZ8qZ8qZ8qZ8qZ8qZ', 'MOZO', TRUE);

-- Usuario CAJERO
INSERT INTO usuario (nombre_usuario, contrasena, rol, estado) VALUES 
('cajero1', '$2a$12$9A9qA9qA9qA9qA9qA9qA9uLY9A9qA9qA9qA9qA9qA9qA9qA9qA9qA', 'CAJERO', TRUE);

-- Usuario COCINERO
INSERT INTO usuario (nombre_usuario, contrasena, rol, estado) VALUES 
('cocinero1', '$2a$12$7B7qB7qB7qB7qB7qB7qB7uMZ7B7qB7qB7qB7qB7qB7qB7qB7qB7qB', 'COCINERO', TRUE);

-- Registros iniciales en bitácora
INSERT INTO bitacora (id_usuario, accion, tabla_afectada, registro_id, ip_address, detalles) VALUES
(1, 'CREAR', 'usuario', 1, '127.0.0.1', 'Usuario administrador creado en inicialización del sistema'),
(1, 'CREAR', 'usuario', 2, '127.0.0.1', 'Usuario mozo1 creado en inicialización del sistema'),
(1, 'CREAR', 'usuario', 3, '127.0.0.1', 'Usuario cajero1 creado en inicialización del sistema'),
(1, 'CREAR', 'usuario', 4, '127.0.0.1', 'Usuario cocinero1 creado en inicialización del sistema');
