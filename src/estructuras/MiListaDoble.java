package estructuras;

/**
 * =====================================================================
 * JUSTIFICACIÓN TÉCNICA DE LA SEGUNDA IMPLEMENTACIÓN (MiListaDoble):
 * =====================================================================
 * A diferencia de MiLista (que es una lista simplemente enlazada), esta 
 * implementación corresponde a una Lista Doblemente Enlazada. 
 * Se justifica su incorporación para escenarios donde se requiere una 
 * navegación bidireccional eficiente (recorridos hacia adelante y hacia atrás) 
 * y operaciones de inserción o eliminación optimizadas desde los extremos 
 * (cabeza y cola) gracias a los punteros dobles en cada nodo, manteniendo 
 * el cumplimiento estricto de no utilizar colecciones de java.util.
 */

public class MiListaDoble<T> {

    private static class NodoDoble<T> {
        T dato;
        NodoDoble<T> siguiente;
        NodoDoble<T> anterior;

        NodoDoble(T dato) {
            this.dato = dato;
            this.siguiente = null;
            this.anterior = null;
        }
    }

    private NodoDoble<T> cabeza;
    private NodoDoble<T> cola;
    private int tamanio;

    public MiListaDoble() {
        this.cabeza = null;
        this.cola = null;
        this.tamanio = 0;
    }

    public void agregar(T elemento) {
        NodoDoble<T> nuevo = new NodoDoble<>(elemento);
        if (cabeza == null) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.siguiente = nuevo;
            nuevo.anterior = cola;
            cola = nuevo;
        }
        tamanio++;
    }

    public T obtener(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
        NodoDoble<T> actual;
        if (indice < tamanio / 2) {
            actual = cabeza;
            for (int i = 0; i < indice; i++) {
                actual = actual.siguiente;
            }
        } else {
            actual = cola;
            for (int i = tamanio - 1; i > indice; i--) {
                actual = actual.anterior;
            }
        }
        return actual.dato;
    }

    public void eliminar(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }

        NodoDoble<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.siguiente;
        }

        if (actual == cabeza) {
            cabeza = cabeza.siguiente;
            if (cabeza != null) {
                cabeza.anterior = null;
            } else {
                cola = null;
            }
        } else if (actual == cola) {
            cola = cola.anterior;
            if (cola != null) {
                cola.siguiente = null;
            } else {
                cabeza = null;
            }
        } else {
            actual.anterior.siguiente = actual.siguiente;
            actual.siguiente.anterior = actual.anterior;
        }
        tamanio--;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int tamanio() {
        return tamanio;
    }

    public void vaciar() {
        cabeza = null;
        cola = null;
        tamanio = 0;
    }
}