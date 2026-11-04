package motor;

import contratos.Actualizable;
import contratos.Dibujable;
import entidades.Bloque;
import entidades.Entidad;
import entidades.Paleta;
import entidades.Pelota;
import entidades.PowerUp;
import estructuras.Lista;
import estructuras.MiCola;
import estructuras.MiPila;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.Rectangle;

public class Mundo implements Actualizable, Dibujable {

    private final int ancho;
    private final int alto;
    private final Lista<Entidad> entidades;
    private Entidad[] cuadro = new Entidad[64];
    private int cantidad;
    private boolean sucio = true;

    private Paleta paleta;
    private MiCola<Integer> nivelesPendientes;
    private MiPila<String> powerUpsRecogidos;
    private int puntaje;
    private int vidas;
    private int nivelActual;
    private boolean juegoGanado;

    public Mundo(int ancho, int alto, Lista<Entidad> entidades) {
        if (ancho <= 0 || alto <= 0) {
            throw new IllegalArgumentException("dimensiones inválidas");
        }
        if (entidades == null) {
            throw new IllegalArgumentException("la lista no puede ser nula");
        }
        this.ancho = ancho;
        this.alto = alto;
        this.entidades = entidades;
        this.puntaje = 0;
        this.vidas = 3;
        this.juegoGanado = false;

        this.nivelesPendientes = new MiCola<>();
        this.powerUpsRecogidos = new MiPila<>();

        // Instanciar y agregar paleta y pelota inicial
        this.paleta = new Paleta(ancho / 2 - 40, alto - 40, 80, 15);
        this.agregar(paleta);
        this.agregar(new Pelota(ancho / 2, alto / 2, 8, 180, -180));

        inicializarNiveles();
    }

    public void procesarEntrada(motor.Entrada entrada) {
        if (entrada.estaPresionada(KeyEvent.VK_LEFT) || entrada.estaPresionada(KeyEvent.VK_A)) {
            paleta.setVelocidad(-380, 0);
        } else if (entrada.estaPresionada(KeyEvent.VK_RIGHT) || entrada.estaPresionada(KeyEvent.VK_D)) {
            paleta.setVelocidad(380, 0);
        } else {
            paleta.setVelocidad(0, 0);
        }
    }
    
    private void inicializarNiveles() {
        nivelesPendientes.encolar(1);
        nivelesPendientes.encolar(2);
        nivelesPendientes.encolar(3);
        nivelesPendientes.encolar(4);

        if (!nivelesPendientes.estaVacia()) {
            nivelActual = nivelesPendientes.desencolar();
            cargarBloquesNivel(nivelActual);
        }
    }

    private void cargarBloquesNivel(int nivel) {
        if (nivel == 1) {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 3; j++) {
                    agregar(new Bloque(50 + i * 95, 40 + j * 35, 85, 25, 1));
                }
            }
        } else if (nivel == 2) {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 4; j++) {
                    agregar(new Bloque(50 + i * 95, 40 + j * 30, 85, 20, 1));
                }
            }
        } else if (nivel == 3) {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 3; j++) {
                    int resistencia = (j == 0) ? 2 : 1;
                    agregar(new Bloque(50 + i * 95, 40 + j * 35, 85, 25, resistencia));
                }
            }
        } else if (nivel == 4) {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 4; j++) {
                    int resistencia = (j <= 1) ? 2 : 1;
                    agregar(new Bloque(50 + i * 95, 40 + j * 30, 85, 20, resistencia));
                }
            }
        }
    }

    public int getAncho() { return ancho; }
    public int getAlto() { return alto; }
    public int cantidad() { return entidades.tamanio(); }
    public int getVidas() { return vidas; }
    public int getPuntaje() { return puntaje; }
    public int getNivelActual() { return nivelActual; }
    public boolean estaTerminado() { return vidas <= 0 || juegoGanado; }

    public void agregar(Entidad entidad) {
        if (entidad == null) {
            throw new IllegalArgumentException("la entidad no puede ser nula");
        }
        entidades.agregar(entidad);
        sucio = true;
    }

    public void vaciar() {
        refrescar();
        for (int i = 0; i < cantidad; i++) {
            cuadro[i].destruir();
        }
    }

    @Override
    public void actualizar(double dt) {
        refrescar();

        // Actualizar entidades
        for (int i = 0; i < cantidad; i++) {
            Entidad e = cuadro[i];
            if (e.estaViva()) {
                e.actualizar(dt);
                e.ajustarALimites(ancho, alto);

                // Control si la pelota cae al fondo
                if (e instanceof Pelota && e.getY() > alto) {
                    e.destruir();
                }
            }
        }

        resolverColisiones();

        // Verificar si se cayeron todas las pelotas
        boolean hayPelotasVivas = false;
        for (int i = 0; i < cantidad; i++) {
            if (cuadro[i] instanceof Pelota && cuadro[i].estaViva()) {
                hayPelotasVivas = true;
                break;
            }
        }

        if (!hayPelotasVivas && vidas > 0) {
            vidas--;
            if (vidas > 0) {
                agregar(new Pelota(ancho / 2, alto / 2, 8, 180, -180));
            }
        }

        // Verificar si se limpiaron todos los bloques del nivel
        boolean quedanBloques = false;
        for (int i = 0; i < cantidad; i++) {
            if (cuadro[i] instanceof Bloque && cuadro[i].estaViva()) {
                quedanBloques = true;
                break;
            }
        }

        if (!quedanBloques) {
            if (!nivelesPendientes.estaVacia()) {
                // Destruir todas las pelotas Y power-ups restantes para limpiar la pantalla por completo
                for (int i = 0; i < cantidad; i++) {
                    if (cuadro[i] instanceof Pelota || cuadro[i] instanceof PowerUp) {
                        cuadro[i].destruir();
                    }
                }

                nivelActual = nivelesPendientes.desencolar();
                cargarBloquesNivel(nivelActual);
                agregar(new Pelota(ancho / 2, alto / 2, 8, 190, -190));
            } else {
                juegoGanado = true;
            }
        }

        limpiar();
    }

    @Override
    public void dibujar(Graphics2D g) {
        refrescar();
        for (int i = 0; i < cantidad; i++) {
            if (cuadro[i].estaViva()) {
                cuadro[i].dibujar(g);
            }
        }
    }
    
    private void resolverColisiones() {
        for (int i = 0; i < cantidad; i++) {
            Entidad entA = cuadro[i];
            if (!entA.estaViva()) continue;
            
            for (int j = i + 1; j < cantidad; j++) {
                Entidad entB = cuadro[j];
                if (!entB.estaViva()) continue;

                // 1. Colisión entre Pelota y Paleta
                if ((entA instanceof Pelota && entB instanceof Paleta) ||
                    (entB instanceof Pelota && entA instanceof Paleta)) {
                    
                    Pelota pelota = (Pelota) (entA instanceof Pelota ? entA : entB);
                    Paleta paletaObj = (Paleta) (entA instanceof Paleta ? entA : entB);

                    if (pelota.getX() >= paletaObj.getX() && pelota.getX() <= paletaObj.getX() + paletaObj.getAncho() &&
                        pelota.getY() + pelota.getRadio() >= paletaObj.getY() && pelota.getY() - pelota.getRadio() <= paletaObj.getY() + paletaObj.getAlto()) {
                        
                        pelota.setPosicion(pelota.getX(), paletaObj.getY() - pelota.getRadio());
                        pelota.setVelocidad(pelota.getVx(), -Math.abs(pelota.getVy()));
                    }
                }

                // 2. Colisión entre Pelota y Bloque
                if ((entA instanceof Pelota && entB instanceof Bloque) ||
                    (entB instanceof Pelota && entA instanceof Bloque)) {
                    
                    Pelota pelota = (Pelota) (entA instanceof Pelota ? entA : entB);
                    Bloque bloque = (Bloque) (entA instanceof Bloque ? entA : entB);

                    if (pelota.getX() >= bloque.getX() && pelota.getX() <= bloque.getX() + bloque.getAncho() &&
                        pelota.getY() >= bloque.getY() && pelota.getY() <= bloque.getY() + bloque.getAlto()) {
                        
                        pelota.setVelocidad(pelota.getVx(), -pelota.getVy());
                        bloque.golpear();
                        puntaje += 15;

                        if (!bloque.estaViva()) {
                            if (Math.random() < 0.30) {
                                String tipoElegido = (Math.random() < 0.5) ? "TRES_PELOTAS" : "TRIPLICAR_PELOTAS";
                                agregar(new PowerUp(bloque.getX() + bloque.getAncho() / 2 - 10, bloque.getY(), tipoElegido));
                            }
                        }
                    }
                }

                // 3. Recoger Power-Up con la Paleta
                if ((entA instanceof Paleta && entB instanceof PowerUp) ||
                    (entB instanceof Paleta && entA instanceof PowerUp)) {
                    
                    Paleta paletaObj = (Paleta) (entA instanceof Paleta ? entA : entB);
                    PowerUp pUp = (PowerUp) (entA instanceof PowerUp ? entA : entB);

                    Rectangle rectPaleta = new Rectangle((int) paletaObj.getX(), (int) paletaObj.getY(), (int) paletaObj.getAncho(), (int) paletaObj.getAlto());
                    
                    if (pUp.getLimites().intersects(rectPaleta)) {
                        powerUpsRecogidos.apilar(pUp.getTipo());

                        if (pUp.getTipo().equals("TRES_PELOTAS")) {
                            agregar(new Pelota(paleta.getX() + paleta.getAncho()/2, paleta.getY() - 10, 8, -160, -210));
                            agregar(new Pelota(paleta.getX() + paleta.getAncho()/2, paleta.getY() - 10, 8, 0, -230));
                            agregar(new Pelota(paleta.getX() + paleta.getAncho()/2, paleta.getY() - 10, 8, 160, -210));
                        } else if (pUp.getTipo().equals("TRIPLICAR_PELOTAS")) {
                            // Triplicar CADA UNA de las pelotas activas en el mapa
                            Pelota[] pelotasActuales = new Pelota[64];
                            int countPelotas = 0;
                            for (int k = 0; k < cantidad; k++) {
                                if (cuadro[k] instanceof Pelota && cuadro[k].estaViva()) {
                                    if (countPelotas < pelotasActuales.length) {
                                        pelotasActuales[countPelotas++] = (Pelota) cuadro[k];
                                    }
                                }
                            }

                            for (int k = 0; k < countPelotas; k++) {
                                Pelota original = pelotasActuales[k];
                                agregar(new Pelota(original.getX(), original.getY(), original.getRadio(), -original.getVx(), original.getVy()));
                                agregar(new Pelota(original.getX(), original.getY(), original.getRadio(), original.getVx(), -original.getVy()));
                            }
                        }
                        pUp.destruir();
                    }
                }
            }
        }
    }

    private void limpiar() {
        boolean hayMuertas = false;
        for (int i = 0; i < cantidad; i++) {
            if (!cuadro[i].estaViva()) {
                hayMuertas = true;
                break;
            }
        }
        if (!hayMuertas) return;

        for (int k = entidades.tamanio() - 1; k >= 0; k--) {
            if (!entidades.obtener(k).estaViva()) {
                entidades.eliminar(k);
            }
        }
        sucio = true;
    }

    private void refrescar() {
        int n = entidades.tamanio();
        if (!sucio && n == cantidad) return;
        
        if (cuadro.length < n) {
            cuadro = new Entidad[Math.max(n, cuadro.length * 2)];
        }
        
        int i = 0;
        for (int j = 0; j < n; j++) {
            Entidad e = entidades.obtener(j);
            if (i < cuadro.length) {
                cuadro[i++] = e;
            }
        }
        for (int k = i; k < cuadro.length; k++) {
            cuadro[k] = null;
        }
        cantidad = i;
        sucio = false;
    }
}