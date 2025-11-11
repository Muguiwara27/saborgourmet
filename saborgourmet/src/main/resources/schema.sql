-- Módulo 6: Administración y Seguridad
-- Sistema de Gestión de Restaurante "Sabor Gourmet"

-- Eliminar tablas si existen (para desarrollo)
DROP TABLE IF EXISTS bitacora;
DROP TABLE IF EXISTS usuario;

-- Tabla: Usuario
CREATE TABLE usuario (
    id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN', 'MOZO', 'CAJERO', 'COCINERO') NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_nombre_usuario (nombre_usuario),
    INDEX idx_rol (rol),
    INDEX idx_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: Bitacora (Auditoría)
CREATE TABLE bitacora (
    id_bitacora BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT,
    accion VARCHAR(255) NOT NULL,
    tabla_afectada VARCHAR(100),
    registro_id BIGINT,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    detalles TEXT,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE SET NULL,
    INDEX idx_usuario (id_usuario),
    INDEX idx_fecha_hora (fecha_hora),
    INDEX idx_tabla (tabla_afectada),
    INDEX idx_accion (accion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
