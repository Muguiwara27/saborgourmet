package com.restaurant.saborgourmet.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utilidad para generar contraseñas cifradas con BCrypt
 * Ejecuta este programa para obtener contraseñas cifradas para todos los usuarios
 */
public class PasswordGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12); // Usando fuerza 12 para mayor seguridad

        // Definir usuarios, contraseñas y roles
        String[][] users = {
                {"admin", "password123", "ADMIN", "Administrador del Sistema"},
                {"mozo1", "Mozo123", "MOZO", "Mesero Principal"},
                {"cocinero1", "Cocinero123", "COCINERO", "Chef Ejecutivo"},
                {"cajero1", "Cajero123", "CAJERO", "Cajero Principal"}
        };

        System.out.println("=== GENERADOR DE HASHES BCRYPT ===\n");
        System.out.println("=== HASHS PARA data.sql ===\n");

        // Mostrar hashes individuales
        for (String[] user : users) {
            String username = user[0];
            String password = user[1];
            String encoded = encoder.encode(password);

            System.out.println("-- Usuario: " + username + " / " + password);
            System.out.println("-- Hash: " + encoded);

            // Verificar que el hash funciona
            boolean matches = encoder.matches(password, encoded);
            System.out.println("-- Verificación: " + (matches ? "✓ Correcta" : "✗ Incorrecta"));
            System.out.println();
        }

        // Mostrar SQL completo
        System.out.println("\n=== SQL COMPLETO PARA data.sql ===\n");
        System.out.println("-- Usuarios de prueba con contraseñas ENCRIPTADAS con BCrypt (fuerza 12)");
        System.out.println("-- Contraseñas en texto plano:");

        for (String[] user : users) {
            System.out.printf("-- %s / %s (%s)\n", user[0], user[1], user[2]);
        }

        System.out.println("\n-- Eliminar usuarios existentes (opcional)");
        System.out.println("-- DELETE FROM usuario;");

        System.out.println("\n-- Insertar usuarios");
        System.out.println("INSERT INTO usuario (nombre_usuario, contrasena, rol, estado, fecha_creacion, fecha_modificacion) VALUES");

        for (int i = 0; i < users.length; i++) {
            String username = users[i][0];
            String password = users[i][1];
            String rol = users[i][2];
            String nombreCompleto = users[i][3];
            String encoded = encoder.encode(password);

            String sql = String.format(
                    "    ('%s', '%s', '%s', true, NOW(), NOW())%s",
                    username,
                    encoded,
                    rol,
                    (i < users.length - 1) ? "," : ";"
            );

            System.out.println(sql);
        }

        System.out.println("\n-- Verificar usuarios insertados");
        System.out.println("-- SELECT nombre_usuario, rol, estado FROM usuario;");
    }
}