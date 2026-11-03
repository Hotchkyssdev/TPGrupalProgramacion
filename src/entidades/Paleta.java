package entidades;

import java.awt.Color;
import java.awt.Graphics2D;

public class Paleta extends Entidad {

    private double ancho;
    private double alto;

    public Paleta(double x, double y, double ancho, double alto) {
        super(x, y);
        this.ancho = ancho;
        this.alto = alto;
    }

    public double getAncho() { return ancho; }
    public double getAlto() { return alto; }

    @Override
    public void actualizar(double dt) {
        mover(dt);
    }

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect((int) getX(), (int) getY(), (int) ancho, (int) alto);
    }

    @Override
    public void ajustarALimites(double limiteAncho, double limiteAlto) {
        // Evita que la paleta salga de los bordes izquierdo y derecho de la pantalla
        if (getX() < 0) {
            setPosicion(0, getY());
        }
        if (getX() + ancho > limiteAncho) {
            setPosicion(limiteAncho - ancho, getY());
        }
    }
}