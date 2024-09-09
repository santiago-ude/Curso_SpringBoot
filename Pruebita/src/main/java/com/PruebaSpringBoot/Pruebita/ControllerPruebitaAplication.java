package com.PruebaSpringBoot.Pruebita;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController // Estoy diciendo que esto va a porcesar solicitudes HTTP
//@Controller esto sirve para el holaMundo que me renderiza el codigo
public class ControllerSpringBoot {

    @GetMapping("/hola")// cada uno de estos metodos es un endpoint
    public String hola(){
        return String.format("Hola");
    }


    @GetMapping("/descripcion")// si yo pongo /descripcion me va a ejecutar lo que este dentro de este metodo
    public String holaMundo(Model model){

        model.addAttribute("nombre","santiago");
        model.addAttribute("apellido","ude");
        model.addAttribute("edad",20);
        return String.format("Hola, mi nombre es " + model.getAttribute("nombre") + " " + model.getAttribute("apellido" )+ " " +
        "y tengo " + model.getAttribute("edad") + " anios");
    }


    //Ingresa nombre por el path
    @GetMapping("/Persona/{nombre}/{apellido}/{edad}")
    public String cursoSpringBoot(@PathVariable String nombre, @PathVariable String apellido,@PathVariable int edad){

        return String.format("El nombre de la persona es " + nombre + " " + apellido + " y la edad es " + edad);

    }



}
