package entidades;

import java.awt.Color;
import java.awt.Graphics2D;

public class PowerUp extends Entidad {

    private double ancho = 20;
    private double alto = 20;
    private String tipo; 

    public PowerUp(double x, double y, String tipo) {
        super(x, y);
        this.tipo = tipo;
        setVelocidad(0, 130); 
    }

    public String getTipo() {
        return tipo;
    }

    public double getAncho() {
        return ancho;
    }

    public double getAlto() {
        return alto;
    }

    @Override
    public void actualizar(double dt) {
        mover(dt);
    }

    @Override
    public void dibujar(Graphics2D g) {
        if (tipo.equals("TRES_PELOTAS")) {
            g.setColor(Color.GREEN); 
        } else {
            g.setColor(Color.CYAN); 
        }
        
        g.fillOval((int) getX(), (int) getY(), (int) ancho, (int) alto);
        g.setColor(Color.WHITE);
        g.drawOval((int) getX(), (int) getY(), (int) ancho, (int) alto);
    }
}