package entidades;

import java.awt.Color;
import java.awt.Graphics2D;

public class Bloque extends Entidad {

    private double ancho;
    private double alto;
    private int resistencia; // Cuántos impactos soporta el bloque

    public Bloque(double x, double y, double ancho, double alto, int resistencia) {
        super(x, y);
        this.ancho = ancho;
        this.alto = alto;
        this.resistencia = resistencia;
    }

    public double getAncho() { return ancho; }
    public double getAlto() { return alto; }
    public int getResistencia() { return resistencia; }

    public void golpear() {
        resistencia--;
        if (resistencia <= 0) {
            destruir(); // Método heredado de Entidad que pone viva = false
        }
    }

    @Override
    public void actualizar(double dt) {
        // Los bloques son estáticos, no necesitan lógica de movimiento por segundo
    }

    @Override
    public void dibujar(Graphics2D g) {
        // Cambiar de color según la resistencia del bloque
        if (resistencia == 2) {
            g.setColor(Color.ORANGE);
        } else if (resistencia >= 3) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.GREEN);
        }
        
        g.fillRect((int) getX(), (int) getY(), (int) ancho, (int) alto);
        g.setColor(Color.BLACK);
        g.drawRect((int) getX(), (int) getY(), (int) ancho, (int) alto); // Borde estético
    }
}