# 🍽️ Sabor Gourmet - Sistema de Gestión de Restaurante
## Módulo 6: Administración y Seguridad

Sistema completo de gestión de restaurante con enfoque en seguridad, autenticación y auditoría automática.

---

## 📋 Tabla de Contenidos
- [Características](#características)
- [Tecnologías](#tecnologías)
- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Ejecución](#ejecución)
- [Usuarios de Prueba](#usuarios-de-prueba)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Funcionalidades](#funcionalidades)
- [Evaluación](#evaluación)

---

## ✨ Características

### Requerimientos Funcionales Implementados

- **RF16: Gestión de Usuarios**
  - CRUD completo de usuarios
  - Validación de datos (nombre único, contraseña segura)
  - Asignación de roles: ADMIN, MOZO, CAJERO, COCINERO
  - Activación/desactivación de usuarios (eliminación lógica)
  - Búsqueda y filtrado por rol y estado

- **RF17: Sistema de Bitácora**
  - Registro automático de TODAS las acciones CRUD
  - Captura de: usuario, acción, tabla, ID registro, fecha/hora, IP
  - Visualización cronológica de eventos
  - Filtros por usuario, fecha, tabla y tipo de acción
  - Paginación de resultados

- **RF18: Control de Accesos**
  - Autenticación con Spring Security
  - Contraseñas cifradas con BCrypt (strength 12)
  - Autorización por roles:
    - `/admin/**` → Solo ADMIN
    - `/pedidos/**` → MOZO, COCINERO, ADMIN
    - `/ventas/**` → CAJERO, ADMIN
    - `/inventario/**` → Solo ADMIN
    - `/bitacora/**` → Solo ADMIN
  - Página de login personalizada
  - Logout seguro
  - Protección CSRF habilitada

### Requerimientos No Funcionales

- **RNF1**: Contraseñas cifradas con BCrypt (strength 12)
- **RNF2**: Solo usuarios autenticados pueden acceder
- **RNF3**: Auditoría automática con AOP de todas las operaciones CRUD
- **RNF8-9**: Interfaz responsive con Bootstrap 5, mensajes claros, validaciones

---

## 🛠️ Tecnologías

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Spring Boot | 3.5.7 | Framework principal |
| Spring Security | 6.x | Autenticación y autorización |
| Spring Data JPA | 3.x | Persistencia de datos |
| Spring AOP | 6.x | Auditoría transversal |
| Thymeleaf | 3.x | Motor de plantillas |
| Bootstrap | 5.3.0 | Framework CSS |
| MySQL | 8.x | Base de datos |
| Lombok | Latest | Reducción de boilerplate |
| BCrypt | - | Cifrado de contraseñas |

---

## 📦 Requisitos

- **Java**: JDK 17 o superior
- **Maven**: 3.8+
- **MySQL**: 8.0+
- **IDE**: IntelliJ IDEA, Eclipse o VS Code

---

## 🚀 Instalación

### 1. Clonar el repositorio
```bash
git clone <url-repositorio>
cd saborgourmet
```**
