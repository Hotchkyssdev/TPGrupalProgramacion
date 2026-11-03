package estructuras;

public class MiPila<T> implements Pila<T> {
    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;

        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    private Nodo<T> cima;
    private int tamanio;

    public MiPila() {
        this.cima = null;
        this.tamanio = 0;
    }

    @Override 
    public void apilar(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);
        nuevo.siguiente = cima;
        cima = nuevo;
        tamanio++;
    }

    @Override 
    public T desapilar() {
        if (estaVacia()) {
            throw new IllegalStateException("No se puede desapilar: la pila esta vacia.");
        }
        T dato = cima.dato;
        cima = cima.siguiente;
        tamanio--;
        return dato;
    }

    @Override 
    public T tope() {
        if (estaVacia()) {
            throw new IllegalStateException("No se puede consultar el tope: la pila esta vacia.");
        }
        return cima.dato;
    }

    @Override 
    public boolean estaVacia() {
        return cima == null;
    }

    @Override 
    public int tamanio() {
        return tamanio;
    }

    @Override 
    public void vaciar() {
        cima = null;
        tamanio = 0;
    }
}