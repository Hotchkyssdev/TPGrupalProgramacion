package pantallas;

import motor.Escena;
import ui.Texto;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class EscenaFin extends Escena {
    private int puntajeFinal;

    public EscenaFin(int puntajeFinal) {
        this.puntajeFinal = puntajeFinal;
    }

    @Override
    public void actualizar(double dt) {
        // Volver al menú con ENTER
        if (getEntrada().fuePresionada(KeyEvent.VK_ENTER)) {
            getContexto().irA(new EscenaMenu());
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getContexto().getAncho(), getContexto().getAlto());

        Texto.centrado(g, "FIN DE LA PARTIDA", getContexto().getAncho() / 2, 180, 24, Color.RED);
        Texto.centrado(g, "Puntaje Final: " + puntajeFinal, getContexto().getAncho() / 2, 240, 18, Color.WHITE);
        Texto.centrado(g, "Presiona [ENTER] para volver al Menú", getContexto().getAncho() / 2, 320, 14, Color.YELLOW);
    }
}