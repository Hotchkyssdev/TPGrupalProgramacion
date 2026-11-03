package reglas;

import entidades.Bloque;
import entidades.Paleta;
import entidades.Pelota;
import estructuras.MiLista;
import estructuras.MiCola;
import estructuras.MiPila;
import motor.Entrada;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class Mundo {
    private Paleta paleta;
    private Pelota pelota;
    private MiLista<Bloque> ladrillos;
    private MiCola<Integer> nivelesPendientes;
    private MiPila<String> powerUpsRecogidos;
    private int puntaje;
    private int vidas;
    private double anchoMundo;
    private double altoMundo;

    public Mundo(double ancho, double alto) {
        this.anchoMundo = ancho;
        this.altoMundo = alto;
        this.puntaje = 0;
        this.vidas = 3;
        
        this.paleta = new Paleta(ancho / 2 - 40, alto - 40, 80, 15);
        this.pelota = new Pelota(ancho / 2, alto / 2, 8, 180, -180);
        
        this.ladrillos = new MiLista<>();
        this.nivelesPendientes = new MiCola<>();
        this.powerUpsRecogidos = new MiPila<>();
        
        inicializarNivelesYBloques();
    }

    private void inicializarNivelesYBloques() {
        // Cola: Niveles en orden
        nivelesPendientes.encolar(1);
        nivelesPendientes.encolar(2);
        
        // Lista: Ladrillos del juego
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 3; j++) {
                ladrillos.agregar(new Bloque(50 + i * 95, 40 + j * 35, 85, 25, 1));
            }
        }
    }

    public void actualizar(double dt, Entrada entrada) {
        // Control de la paleta con flechas o A/D
        if (entrada.estaPresionada(KeyEvent.VK_LEFT) || entrada.estaPresionada(KeyEvent.VK_A)) {
            paleta.setVelocidad(-350, 0);
        } else if (entrada.estaPresionada(KeyEvent.VK_RIGHT) || entrada.estaPresionada(KeyEvent.VK_D)) {
            paleta.setVelocidad(350, 0);
        } else {
            paleta.setVelocidad(0, 0);
        }

        paleta.actualizar(dt);
        paleta.ajustarALimites(anchoMundo, altoMundo);

        pelota.actualizar(dt);
        pelota.ajustarALimites(anchoMundo, altoMundo);

        // Colisión: Pelota con Paleta
        if (pelota.getX() >= paleta.getX() && pelota.getX() <= paleta.getX() + paleta.getAncho() &&
            pelota.getY() + pelota.getRadio() >= paleta.getY() && pelota.getY() - pelota.getRadio() <= paleta.getY() + paleta.getAlto()) {
            pelota.setPosicion(pelota.getX(), paleta.getY() - pelota.getRadio());
            pelota.setVelocidad(pelota.getVx(), -Math.abs(pelota.getVy()));
        }

        // Colisión: Pelota con Ladrillos (usando MiLista)
        for (int i = 0; i < ladrillos.tamanio(); i++) {
            Bloque b = ladrillos.obtener(i);
            if (b.estaViva()) {
                if (pelota.getX() >= b.getX() && pelota.getX() <= b.getX() + b.getAncho() &&
                    pelota.getY() >= b.getY() && pelota.getY() <= b.getY() + b.getAlto()) {
                    b.golpear();
                    pelota.setVelocidad(pelota.getVx(), -pelota.getVy());
                    puntaje += 15;
                    
                    if (!b.estaViva()) {
                        ladrillos.eliminar(i);
                        // Pila: Power-ups recogidos
                        powerUpsRecogidos.apilar("BONUS_VELOCIDAD");
                    }
                    break;
                }
            }
        }

        // Control de pérdida de vida si la pelota cae
        if (pelota.getY() > altoMundo) {
            vidas--;
            pelota.setPosicion(anchoMundo / 2, altoMundo / 2);
            pelota.setVelocidad(180, -180);
        }
    }

    public void dibujar(Graphics2D g) {
        paleta.dibujar(g);
        pelota.dibujar(g);
        for (int i = 0; i < ladrillos.tamanio(); i++) {
            ladrillos.obtener(i).dibujar(g);
        }
    }

    public int getVidas() { return vidas; }
    public int getPuntaje() { return puntaje; }
    public boolean estaTerminado() { return vidas <= 0 || ladrillos.estaVacia(); }
}