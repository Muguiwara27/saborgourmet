package com.restaurant.saborgourmet.controller;

import com.restaurant.saborgourmet.dto.UsuarioDTO;
import com.restaurant.saborgourmet.model.Usuario;
import com.restaurant.saborgourmet.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador de Administración
 * RF16: Gestión de Usuarios
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final UsuarioService usuarioService;

    @GetMapping("/usuarios")
    public String listarUsuarios(
            @RequestParam(required = false) String rol,
            @RequestParam(required = false) Boolean estado,
            Model model) {
        
        List<Usuario> usuarios;
        
        if (rol != null && !rol.isEmpty() && estado != null) {
            usuarios = usuarioService.listarPorRolYEstado(Usuario.Rol.valueOf(rol), estado);
        } else if (rol != null && !rol.isEmpty()) {
            usuarios = usuarioService.listarPorRol(Usuario.Rol.valueOf(rol));
        } else if (estado != null) {
            usuarios = usuarioService.listarTodos().stream()
                .filter(u -> u.getEstado().equals(estado))
                .toList();
        } else {
            usuarios = usuarioService.listarTodos();
        }
        
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("roles", Usuario.Rol.values());
        return "admin/usuarios-list";
    }

    @GetMapping("/usuarios/nuevo")
    public String nuevoUsuarioForm(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        model.addAttribute("roles", Usuario.Rol.values());
        model.addAttribute("esNuevo", true);
        return "admin/usuario-form";
    }

    @PostMapping("/usuarios/nuevo")
    public String guardarNuevoUsuario(
            @Valid @ModelAttribute UsuarioDTO usuarioDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("roles", Usuario.Rol.values());
            model.addAttribute("esNuevo", true);
            return "admin/usuario-form";
        }
        
        try {
            usuarioService.crearUsuario(usuarioDTO);
            redirectAttributes.addFlashAttribute("success", "Usuario creado exitosamente");
            return "redirect:/admin/usuarios";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("roles", Usuario.Rol.values());
            model.addAttribute("esNuevo", true);
            return "admin/usuario-form";
        }
    }

    @GetMapping("/usuarios/editar/{id}")
    public String editarUsuarioForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
        model.addAttribute("usuarioDTO", usuarioDTO);
        model.addAttribute("roles", Usuario.Rol.values());
        model.addAttribute("esNuevo", false);
        return "admin/usuario-form";
    }

    @PostMapping("/usuarios/editar/{id}")
    public String actualizarUsuario(
            @PathVariable Long id,
            @Valid @ModelAttribute UsuarioDTO usuarioDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("roles", Usuario.Rol.values());
            model.addAttribute("esNuevo", false);
            return "admin/usuario-form";
        }
        
        try {
            usuarioService.actualizarUsuario(id, usuarioDTO);
            redirectAttributes.addFlashAttribute("success", "Usuario actualizado exitosamente");
            return "redirect:/admin/usuarios";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("roles", Usuario.Rol.values());
            model.addAttribute("esNuevo", false);
            return "admin/usuario-form";
        }
    }

    @PostMapping("/usuarios/cambiar-estado/{id}")
    public String cambiarEstado(
            @PathVariable Long id,
            @RequestParam Boolean estado,
            RedirectAttributes redirectAttributes) {
        
        try {
            usuarioService.cambiarEstado(id, estado);
            String mensaje = estado ? "Usuario activado exitosamente" : "Usuario desactivado exitosamente";
            redirectAttributes.addFlashAttribute("success", mensaje);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/admin/usuarios";
    }
}
