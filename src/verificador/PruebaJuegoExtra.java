package verificador;

import estructuras.MiCola;
import estructuras.MiPila;

public class PruebaJuegoExtra {
    public static void main(String[] args) {
        System.out.println("Iniciando pruebas propias del sistema...");
        
        MiCola<Integer> colaPrueba = new MiCola<>();
        colaPrueba.encolar(1);
        colaPrueba.encolar(2);
        assert !colaPrueba.estaVacia() : "Error: La cola de niveles debería tener elementos";
        
        MiPila<String> pilaPrueba = new MiPila<>();
        pilaPrueba.apilar("TRES_PELOTAS");
        assert pilaPrueba.tope().equals("TRES_PELOTAS") : "Error en Pila de power-ups";
        
        System.out.println("¡Todas las pruebas propias pasaron con éxito!");
    }
}