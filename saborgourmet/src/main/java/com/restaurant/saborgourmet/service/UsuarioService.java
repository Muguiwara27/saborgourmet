package com.restaurant.saborgourmet.service;

import com.restaurant.saborgourmet.dto.UsuarioDTO;
import com.restaurant.saborgourmet.model.Usuario;

import java.util.List;
import java.util.Optional;


public interface UsuarioService {


    Usuario crearUsuario(UsuarioDTO usuarioDTO);


    Usuario actualizarUsuario(Long id, UsuarioDTO usuarioDTO);


    Usuario cambiarEstado(Long id, Boolean estado);


    void eliminarUsuario(Long id);


    Optional<Usuario> buscarPorId(Long id);


    Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario);


    List<Usuario> listarTodos();


    List<Usuario> listarActivos();


    List<Usuario> listarPorRol(Usuario.Rol rol);


    List<Usuario> listarPorRolYEstado(Usuario.Rol rol, Boolean estado);


    List<Usuario> buscarPorNombre(String nombre);


    boolean existeNombreUsuario(String nombreUsuario);


    void cambiarContrasena(Long id, String nuevaContrasena);
}
