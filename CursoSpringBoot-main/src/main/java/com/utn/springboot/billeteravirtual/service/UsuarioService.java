package com.utn.springboot.billeteravirtual.service;

import com.utn.springboot.billeteravirtual.controller.OrdenUsuario;
import com.utn.springboot.billeteravirtual.exception.UsuarioNoEncontradoException;
import com.utn.springboot.billeteravirtual.model.Usuario;
import com.utn.springboot.billeteravirtual.utils.Utilidades;
import com.utn.springboot.billeteravirtual.utils.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// Esta clase deberá tener la lógica de negocio para poder realizar las operaciones CRUD de la entidad Usuario.
// La anotación @Service indica que esta clase es un servicio. En Spring, los servicios son clases que contienen la lógica de negocio.
@Service
public class UsuarioService {
    private final List<Usuario> usuarios = new ArrayList<>();
    private final Utilidades utilidades;
    private final Log log;

    @Autowired
    public UsuarioService(Utilidades utilidades, @Qualifier("consoleLog") Log logService) {
        this.utilidades = utilidades;
        this.log = logService;
        usuarios.add(new Usuario(1L, "Juan Perez", "juan@example.com", 41));
        usuarios.add(new Usuario(2L, "Ana Garcia", "ana@example.com", 34));
        usuarios.add(new Usuario(3L, "Maria Romero", "maria@example.com", 27));
        usuarios.add(new Usuario(4L, "Roberto Aguirre", "roberto@example.com", 60));
        usuarios.add(new Usuario(5L, "Jose Ortiz", "jose@example.com", 23));
        usuarios.add(new Usuario(6L, "Patricia Barreto", "maria@example.com", 27));
        usuarios.add(new Usuario(7L, "Juan Pablo Villa", "jpv@example.com", 44));
    }

    // Método para obtener todos los usuarios
    public List<Usuario> obtenerTodosLosUsuarios(String nombre, Integer edadMin, Integer edadMax, OrdenUsuario orden) {
        return usuarios.stream()
                .filter(usuario -> (nombre == null || usuario.getNombre().toLowerCase().contains(nombre.toLowerCase())))
                .filter(usuario -> (edadMin == null || usuario.getEdad() >= edadMin))
                .filter(usuario -> (edadMax == null || usuario.getEdad() <= edadMax))
                .sorted(Comparator.comparing(usuario -> switch (orden) {
                    case OrdenUsuario.NOMBRE -> usuario.getNombre();
                    case OrdenUsuario.EMAIL -> usuario.getEmail();
                    default -> usuario.getId().toString();
                }))
                .collect(Collectors.toList());
    }


    public Usuario obtenerUsuarioPorId(Long id){

        return usuarios.stream()
                .filter(usuario -> usuario.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));

    }

    // Método para crear un nuevo usuario
    public Usuario crearUsuario(Usuario usuario) {
        usuario.setId((long) (usuarios.size() + 1)); // Asignar ID al usuario nuevo
        String nombre = utilidades.formatearTexto(usuario.getNombre());
        usuario.setNombre(nombre);
        usuarios.add(usuario);
        return usuario;
    }



    // Método para actualizar un usuario existente. Si el usuario no existe, se lanzará una excepción UsuarioNoEncontradoException.
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) throws UsuarioNoEncontradoException {
        return usuarios.stream().filter(usuario -> usuario.getId().equals(id)).peek(usuario -> {
            usuario.setNombre(usuarioActualizado.getNombre());
            usuario.setEmail(usuarioActualizado.getEmail());
            usuario.setEdad(usuarioActualizado.getEdad());
        }).findFirst().orElseThrow(() -> new UsuarioNoEncontradoException(id));
    }

    public Usuario actualizarUsuarioPatch(Long id, Usuario datosActualizados){


        Usuario usuarioExistente = usuarios.stream()
                .filter(usuario -> usuario.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));

        if (usuarioExistente != null) {
            // Solo actualizar los campos que no son nulos en el cuerpo de la solicitud
            if (datosActualizados.getNombre() != null) {
                usuarioExistente.setNombre(datosActualizados.getNombre());
            }
            if (datosActualizados.getEmail() != null) {
                usuarioExistente.setEmail(datosActualizados.getEmail());
            }
            if (datosActualizados.getEdad() > 0) {
                usuarioExistente.setEdad(datosActualizados.getEdad());
            }
        }
        return usuarioExistente;

    }


    // Método para eliminar un usuario por ID. Devuelve true si el usuario fue eliminado, false en caso contrario.
    public boolean eliminarUsuario(Long id) {
        return usuarios.removeIf(usuario -> usuario.getId().equals(id));
    }
}
