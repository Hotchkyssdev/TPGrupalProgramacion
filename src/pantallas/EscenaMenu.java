package pantallas;

import motor.Escena;
import ui.Texto;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class EscenaMenu extends Escena {

    @Override
    public void actualizar(double dt) {
        // Al presionar ENTER, pasa a la escena de juego pasándosela por constructor
        if (getEntrada().fuePresionada(KeyEvent.VK_ENTER)) {
            getContexto().irA(new EscenaJuego());
        }
        // ESC para salir
        if (getEntrada().fuePresionada(KeyEvent.VK_ESCAPE)) {
            getContexto().salir();
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getContexto().getAncho(), getContexto().getAlto());

        Texto.centrado(g, "BREAKOUT - JAVA GAME JAM", getContexto().getAncho() / 2, 180, 24, Color.WHITE);
        Texto.centrado(g, "Usa A/D o Flechas para moverte", getContexto().getAncho() / 2, 250, 14, Color.LIGHT_GRAY);
        Texto.centrado(g, "Presiona [ENTER] para Comenzar", getContexto().getAncho() / 2, 330, 16, Color.YELLOW);
    }
}