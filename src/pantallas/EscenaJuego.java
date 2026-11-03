package pantallas;

import reglas.Mundo;
import motor.Escena;
import ui.Texto;
import java.awt.Color;
import java.awt.Graphics2D;

public class EscenaJuego extends Escena {
    private Mundo mundo;

    @Override
    public void alEntrar() {
        mundo = new Mundo(getContexto().getAncho(), getContexto().getAlto());
    }

    @Override
    public void actualizar(double dt) {
        mundo.actualizar(dt, getEntrada());

        // Si el juego termina, transiciona a la pantalla de fin pasando el puntaje
        if (mundo.estaTerminado()) {
            getContexto().irA(new EscenaFin(mundo.getPuntaje()));
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, getContexto().getAncho(), getContexto().getAlto());

        // Dibujar elementos del mundo
        mundo.dibujar(g);

        // HUD superior usando ui.Texto
        Texto.izquierda(g, "Puntaje: " + mundo.getPuntaje(), 20, 30, 14, Color.WHITE);
        Texto.derecha(g, "Vidas: " + mundo.getVidas(), getContexto().getAncho() - 20, 30, 14, Color.RED);
    }
}