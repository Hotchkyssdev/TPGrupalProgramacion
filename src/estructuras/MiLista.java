package estructuras;

public class MiLista<T> implements Lista<T> {
    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;
        Nodo<T> anterior;

        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
            this.anterior = null;
        }
    }

    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamanio;

    public MiLista() {
        this.cabeza = null;
        this.cola = null;
        this.tamanio = 0;
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("Indice fuera de rango: " + indice + ", tamanio: " + tamanio);
        }
    }

    private void validarIndiceParaInsertar(int indice) {
        if (indice < 0 || indice > tamanio) {
            throw new IndexOutOfBoundsException("Indice fuera de rango para insertar: " + indice + ", tamanio: " + tamanio);
        }
    }

    @Override 
    public void agregar(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);
        if (estaVacia()) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.siguiente = nuevo;
            nuevo.anterior = cola;
            cola = nuevo;
        }
        tamanio++;
    }

    @Override 
    public void insertar(int indice, T elemento) {
        validarIndiceParaInsertar(indice);
        if (indice == tamanio) {
            agregar(elemento);
            return;
        }
        Nodo<T> nuevo = new Nodo<>(elemento);
        if (indice == 0) {
            nuevo.siguiente = cabeza;
            if (cabeza != null) {
                cabeza.anterior = nuevo;
            }
            cabeza = nuevo;
            if (cola == null) {
                cola = nuevo;
            }
        } else {
            Nodo<T> actual = obtenerNodo(indice);
            Nodo<T> ant = actual.anterior;
            ant.siguiente = nuevo;
            nuevo.anterior = ant;
            nuevo.siguiente = actual;
            actual.anterior = nuevo;
        }
        tamanio++;
    }

    @Override 
    public T obtener(int indice) {
        validarIdiceSeguro(indice);
        return obtenerNodo(indice).dato;
    }

    private void validarIdiceSeguro(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("Indice invalido: " + indice);
        }
    }

    private Nodo<T> obtenerNodo(int indice) {
        Nodo<T> actual;
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
        return actual;
    }

    @Override 
    public int indiceDe(T elemento) {
        Nodo<T> actual = cabeza;
        int index = 0;
        while (actual != null) {
            if (elemento == null ? actual.dato == null : elemento.equals(actual.dato)) {
                return index;
            }
            actual = actual.siguiente;
            index++;
        }
        return -1;
    }

    @Override 
    public int tamanio() {
        return tamanio;
    }

    @Override 
    public boolean estaVacia() {
        return tamanio == 0;
    }

    @Override 
    public T eliminar(int indice) {
        validarIndice(indice);
        Nodo<T> aEliminar = obtenerNodo(indice);
        T dato = aEliminar.dato;

        if (tamanio == 1) {
            cabeza = null;
            cola = null;
        } else if (aEliminar == cabeza) {
            cabeza = cabeza.siguiente;
            cabeza.anterior = null;
        } else if (aEliminar == cola) {
            cola = cola.anterior;
            cola.siguiente = null;
        } else {
            Nodo<T> ant = aEliminar.anterior;
            Nodo<T> sig = aEliminar.siguiente;
            ant.siguiente = sig;
            sig.anterior = ant;
        }
        tamanio--;
        return dato;
    }

    @Override 
    public void vaciar() {
        cabeza = null;
        cola = null;
        tamanio = 0;
    }
}