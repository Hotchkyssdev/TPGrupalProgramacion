package entidades;

import java.awt.Color;
import java.awt.Graphics2D;

public class Pelota extends Entidad {

    private double radio;

    public Pelota(double x, double y, double radio, double vx, double vy) {
        super(x, y);
        this.radio = radio;
        setVelocidad(vx, vy);
    }

    public double getRadio() { return radio; }

    @Override
    public void actualizar(double dt) {
        mover(dt);
    }

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(Color.YELLOW);
        g.fillOval((int) (getX() - radio), (int) (getY() - radio), (int) (radio * 2), (int) (radio * 2));
    }

    @Override
    public void ajustarALimites(double limiteAncho, double limiteAlto) {
        // Rebote en la pared izquierda y derecha
        if (getX() - radio < 0) {
            setPosicion(radio, getY());
            setVelocidad(-getVx(), getVy());
        } else if (getX() + radio > limiteAncho) {
            setPosicion(limiteAncho - radio, getY());
            setVelocidad(-getVx(), getVy());
        }

        // Rebote en el techo
        if (getY() - radio < 0) {
            setPosicion(getX(), radio);
            setVelocidad(getVx(), -getVy());
        }
        
        // Nota: Si la pelota cae por debajo del borde inferior (getY() > limiteAlto), 
        // la regla del juego indicará que se pierde una vida.
    }
}