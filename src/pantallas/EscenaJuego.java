package pantallas;

import motor.Mundo;
import motor.Escena;
import ui.Texto;
import estructuras.MiLista;
import entidades.Entidad;
import java.awt.Color;
import java.awt.Graphics2D;

public class EscenaJuego extends Escena {
    private Mundo mundo;

    @Override
    public void alEntrar() {
        mundo = new Mundo(getContexto().getAncho(), getContexto().getAlto(), new MiLista<Entidad>());
    }

    @Override
    public void actualizar(double dt) {
        // Procesamos el movimiento de la paleta con la instancia de entrada de la escena
        mundo.procesarEntrada(getEntrada());
        
        // Actualizamos el motor del mundo
        mundo.actualizar(dt);

        if (mundo.estaTerminado()) {
            getContexto().irA(new EscenaFin(mundo.getPuntaje()));
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, getContexto().getAncho(), getContexto().getAlto());

        mundo.dibujar(g);

        Texto.izquierda(g, "Puntaje: " + mundo.getPuntaje(), 20, 30, 14, Color.WHITE);
        Texto.derecha(g, "Vidas: " + mundo.getVidas(), getContexto().getAncho() - 20, 30, 14, Color.RED);
    }
}