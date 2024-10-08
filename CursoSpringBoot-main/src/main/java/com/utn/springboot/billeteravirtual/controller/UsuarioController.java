package com.utn.springboot.billeteravirtual.controller;

import com.utn.springboot.billeteravirtual.exception.UsuarioNoEncontradoException;
import com.utn.springboot.billeteravirtual.model.Usuario;
import com.utn.springboot.billeteravirtual.utils.Utilidades;
import com.utn.springboot.billeteravirtual.utils.log.CodigoLog;
import com.utn.springboot.billeteravirtual.utils.log.ConsoleLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.utn.springboot.billeteravirtual.service.UsuarioService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// @RestController indica que esta clase es un controlador de Spring MVC y que los métodos de esta clase devuelven objetos JSON en lugar
// de vistas.
@RestController
// @RequestMapping indica la URL base para todas las solicitudes de este controlador.
@RequestMapping("/usuarios")
public class UsuarioController {


    private final UsuarioService service;


    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    // 1. Obtener una lista de usuarios
    // @GetMapping indica que este método maneja las solicitudes GET a la URL /usuarios.
    // @RequestParam se usa para obtener los parámetros de la URL.
    // - Si se especifica required = false, el parámetro es opcional.
    // - Si no se encuentra el parámetro en la URL, el valor es null (por eso se utiliza Integer en lugar de int).
    // - Si se especifica defaultValue, se usa ese valor si no se encuentra el parámetro.
    // El método filtra la lista de usuarios según los parámetros de búsqueda y los ordena según el parámetro orden.
    @Operation(summary = "Obtener una lista de usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida con éxito"),
            @ApiResponse(responseCode = "400", description = "Parámetros de búsqueda inválidos")
    })
    @GetMapping
    public List<Usuario> obtenerUsuarios(
            @Parameter(description = "Filtro opcional para buscar usuarios por nombre", example = "Juan")
            @RequestParam(required = false) String nombre,
            @Parameter(description = "Filtro opcional para buscar usuarios que superen una edad mínima", example = "40")
            @RequestParam(required = false) Integer edadMin,
            @Parameter(description = "Filtro opcional para buscar usuarios que no superen una edad máxima", example = "50")
            @RequestParam(required = false) Integer edadMax,
            @Parameter(description = "Ordenar la lista de usuarios por id, nombre o email", example = "NOMBRE")
            @RequestParam(required = false, defaultValue = "ID") OrdenUsuario orden) {
        return service.obtenerTodosLosUsuarios(nombre, edadMin, edadMax,orden);

    }

    // 2. Obtener un usuario por ID
    // @GetMapping indica que este método maneja las solicitudes GET a la URL /usuarios/{id}.
    // @PathVariable se usa para obtener el valor de la variable {id} de la URL.
    // El método busca un usuario por ID en la lista de usuarios y devuelve el usuario si lo encuentra.
    // Si no encuentra el usuario, lanza una excepción UsuarioNoEncontradoException.
    @Operation(summary = "Obtener un usuario por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario obtenido con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public Usuario obtenerUsuarioPorId(
            @Parameter(description = "ID del usuario a obtener", example = "1")
            @PathVariable Long id) {

        return service.obtenerUsuarioPorId(id);

    }




    // 3. Crear un nuevo usuario
    // @PostMapping indica que este método maneja las solicitudes POST a la URL /usuarios.
    // @Valid se usa para validar el objeto Usuario con las anotaciones de validación.
    // @RequestBody se usa para obtener el cuerpo de la solicitud y convertirlo automáticamente a un objeto Usuario.
    // El método asigna un ID al nuevo usuario y lo agrega a la lista de usuarios. Devuelve el usuario creado.
    // @ResponseStatus(HttpStatus.CREATED) indica que el código de estado de la respuesta será 201 Created.
    @Operation(summary = "Crear un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos del usuario inválidos", content = @Content(schema = @Schema(implementation = Map.Entry.class), examples = @ExampleObject(value = "{ \"nombre\": \"no puede estar vacío\" }")))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario crearUsuario(
            @Parameter(description = "Datos del nuevo usuario")
            @Valid @RequestBody Usuario nuevoUsuario) {

        return service.crearUsuario(nuevoUsuario);
    }



    // 4. Actualizar un usuario
    // @PutMapping indica que este método maneja las solicitudes PUT a la URL /usuarios/{id}.
    // El método busca un usuario por ID en la lista de usuarios y actualiza sus datos con los datos del usuario enviado en el cuerpo de
    // la solicitud.
    // Si el usuario no se encuentra, lanza una excepción UsuarioNoEncontradoException.
    @Operation(summary = "Actualizar un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos del usuario inválidos", content = @Content(schema = @Schema(implementation = Map.Entry.class), examples = @ExampleObject(value = "{ \"nombre\": \"no puede estar vacío\" }"))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    public Usuario actualizarUsuario(
            @Parameter(description = "ID del usuario a actualizar", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados del usuario")
            @Valid @RequestBody Usuario usuarioActualizado) {

        return service.actualizarUsuario(id,usuarioActualizado);
    }

    // 5. Actualizar parcialmente un usuario
    // @PatchMapping indica que este método maneja las solicitudes PATCH a la URL /usuarios/{id}.
    // El método busca un usuario por ID en la lista de usuarios y actualiza solo los campos que no son nulos en el cuerpo de la solicitud.
    // Si el usuario no se encuentra, lanza una excepción UsuarioNoEncontradoException.
    @Operation(summary = "Actualizar parcialmente un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado parcialmente con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PatchMapping("/{id}")
    public Usuario actualizarParcialUsuario(
            @Parameter(description = "ID del usuario a actualizar", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados del usuario")
            @RequestBody Usuario datosActualizados) {


        return service.actualizarUsuarioPatch(id,datosActualizados);
    }


    // 6. Eliminar un usuario
    // @DeleteMapping indica que este método maneja las solicitudes DELETE a la URL /usuarios/{id}.
    // El método busca un usuario por ID en la lista de usuarios y lo elimina si lo encuentra.
    // Si no encuentra el usuario, lanza una excepción UsuarioNoEncontradoException.
    // @ResponseStatus(HttpStatus.NO_CONTENT) indica que el código de estado de la respuesta será 204 No Content.
    @Operation(summary = "Eliminar un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarUsuario(
            @Parameter(description = "ID del usuario a eliminar", example = "1")
            @PathVariable Long id) {
        boolean usuarioEliminado = service.eliminarUsuario(id);

        if (!usuarioEliminado) {
            throw new UsuarioNoEncontradoException(id);
        }
    }





}