package estructuras;

public class MiCola<T> implements Cola<T> {
    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;

        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    private Nodo<T> frenteNodo;
    private Nodo<T> finNodo;
    private int tamanio;

    public MiCola() {
        this.frenteNodo = null;
        this.finNodo = null;
        this.tamanio = 0;
    }

    @Override 
    public void encolar(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);
        if (estaVacia()) {
            frenteNodo = nuevo;
            finNodo = nuevo;
        } else {
            finNodo.siguiente = nuevo;
            finNodo = nuevo;
        }
        tamanio++;
    }

    @Override 
    public T desencolar() {
        if (estaVacia()) {
            throw new IllegalStateException("No se puede desencolar: la cola esta vacia.");
        }
        T dato = frenteNodo.dato;
        frenteNodo = frenteNodo.siguiente;
        if (frenteNodo == null) {
            finNodo = null;
        }
        tamanio--;
        return dato;
    }

    @Override 
    public T frente() {
        if (estaVacia()) {
            throw new IllegalStateException("No se puede consultar el frente: la cola esta vacia.");
        }
        return frenteNodo.dato;
    }

    @Override
    public boolean estaVacia() {
        return frenteNodo == null;
    }

    @Override 
    public int tamanio() {
        return tamanio;
    }

    @Override 
    public void vaciar() {
        frenteNodo = null;
        finNodo = null;
        tamanio = 0;
    }
}