package com.PruebaSpringBoot.Pruebita;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerPruebita {

    @GetMapping("/calculadora/{operador}/{num1}/{num2}")
    public int calculadora(@PathVariable int operador,@PathVariable int num1,@PathVariable int num2){

        int resultado = 0;

        if(operador >= 1 && operador <= 4){

            switch (operador){

                case 1:
                    resultado = num1 + num2;
                    break;

                case 2:
                    resultado = num1 - num2;
                    break;

                case 3:
                    resultado = num1 * num2;
                    break;

                case 4:
                    resultado = num1/num2;
                    break;

                default:

                    break;
            }
        }
        return resultado;
    }
}
