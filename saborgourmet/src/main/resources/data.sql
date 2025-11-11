-- Usuarios de prueba con contraseñas ENCRIPTADAS con BCrypt (fuerza 12)
-- Contraseñas en texto plano:
-- admin / password123 (Administrador)
-- mozo1 / Mozo123 (Mozo)
-- cajero1 / Cajero123 (Cajero)
-- cocinero1 / Cocinero123 (Cocinero)

-- Eliminar usuarios existentes (opcional)
-- DELETE FROM usuario;

-- Insertar usuarios
INSERT INTO usuario (nombre_usuario, contrasena, rol, estado, fecha_creacion, fecha_modificacion) VALUES
    ('admin', '$2a$12$V1U0fksEtAcWvCbhG9Et6utnkJMCipPtyNmkLS8ajJJ1dQPXMb8zS', 'ADMIN', true, NOW(), NOW()),
    ('mozo1', '$2a$12$TDo3PMPtJo4vs7j8Sz7hsu2Z1ktL/Jri2WsBDcKTHiRm7pOxEuJbO', 'MOZO', true, NOW(), NOW()),
    ('cocinero1', '$2a$12$zyYfzswg9alzFoKD5mRIG.pMfGtfk/4SZvR6cPwb0hZ95ftHq1SeS', 'COCINERO', true, NOW(), NOW()),
    ('cajero1', '$2a$12$/u.TWnIE9TGeFyH8xW5FWuFzBlN2vQKia2rOhx3s.UDAxK3Iuz4ny', 'CAJERO', true, NOW(), NOW());

-- Verificar usuarios insertados
-- SELECT nombre_usuario, rol, estado FROM usuario;
