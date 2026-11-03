package demo;

import pantallas.EscenaMenu;
import motor.Ventana;

public class Main {
    public static void main(String[] args) {
        // Abre la ventana de Swing con las dimensiones del juego y arranca en tu EscenaMenu
        Ventana.abrir("Breakout - Java Game Jam", 800, 600, new EscenaMenu());
    }
}