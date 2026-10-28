package listas;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Lista simplemente enlazada genérica.
 *
 * Solo se usan nodos propios (clase Nodo); no se usa java.util.LinkedList
 * ni ArrayList.
 */
public class ListaEnlazada<T> {

    /** Primer nodo de la lista (null cuando la lista está vacía). */
    private Nodo<T> cabeza;

    /** Cantidad de elementos. Debe mantenerse actualizada en cada operación. */
    private int cantidad;

    public ListaEnlazada() {
        this.cabeza = null;
        this.cantidad = 0;
    }

    /**
     * Retorna el nodo que está en la posición indicada.
     * No valida el índice: quien lo llama debe haberlo validado antes.
     */
    private Nodo<T> obtenerNodo(int indice) {
        Nodo<T> actual = this.cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        return actual;
    }

    // =====================================================================
    // ETAPA 1. Estado de la lista
    // =====================================================================

    /** @return true si la lista no tiene elementos. */
    public boolean estaVacia() {
        return this.cantidad == 0;
    }

    /** @return número de elementos de la lista. */
    public int tamanio() {
        return this.cantidad;
    }

    // =====================================================================
    // ETAPA 2. Agregar en los extremos
    // =====================================================================

    /** Inserta el dato como nuevo primer elemento. */
    public void agregarAlInicio(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.setSiguiente(this.cabeza);
        this.cabeza = nuevo;
        this.cantidad++;
    }

    /** Inserta el dato como nuevo último elemento. */
    public void agregarAlFinal(T dato) {
        if (estaVacia()) {
            agregarAlInicio(dato);
            return;
        }
        Nodo<T> ultimo = this.cabeza;
        while (ultimo.getSiguiente() != null) {
            ultimo = ultimo.getSiguiente();
        }
        ultimo.setSiguiente(new Nodo<>(dato));
        this.cantidad++;
    }

    // =====================================================================
    // ETAPA 3. Consultar elementos
    // =====================================================================

    /** @throws NoSuchElementException si la lista está vacía */
    public T obtenerPrimero() {
        if (estaVacia()) {
            throw new NoSuchElementException("La lista está vacía");
        }
        return this.cabeza.getDato();
    }

    /** @throws NoSuchElementException si la lista está vacía */
    public T obtenerUltimo() {
        if (estaVacia()) {
            throw new NoSuchElementException("La lista está vacía");
        }
        return obtenerNodo(this.cantidad - 1).getDato();
    }

    /**
     * @param indice posición (0 = primer elemento)
     * @throws IndexOutOfBoundsException si indice < 0 o indice >= tamanio()
     */
    public T obtener(int indice) {
        if (indice < 0 || indice >= this.cantidad) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }
        return obtenerNodo(indice).getDato();
    }

    // =====================================================================
    // ETAPA 4. Búsqueda
    // =====================================================================

    /** @return true si algún elemento es igual (equals) al dato. Admite null. */
    public boolean contiene(T dato) {
        return indiceDe(dato) != -1;
    }

    /** @return posición de la primera aparición del dato, o -1 si no está. */
    public int indiceDe(T dato) {
        Nodo<T> actual = this.cabeza;
        int posicion = 0;
        while (actual != null) {
            if (Objects.equals(actual.getDato(), dato)) {
                return posicion;
            }
            actual = actual.getSiguiente();
            posicion++;
        }
        return -1;
    }

    // =====================================================================
    // ETAPA 5. Insertar y reemplazar en cualquier posición
    // =====================================================================

    /**
     * Inserta el dato en la posición indicada, desplazando los siguientes.
     * Se permite indice == tamanio() (equivale a agregar al final).
     *
     * @throws IndexOutOfBoundsException si indice < 0 o indice > tamanio()
     */
    public void insertar(int indice, T dato) {
        if (indice < 0 || indice > this.cantidad) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }
        if (indice == 0) {
            agregarAlInicio(dato);
            return;
        }
        Nodo<T> anterior = obtenerNodo(indice - 1);
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.setSiguiente(anterior.getSiguiente());
        anterior.setSiguiente(nuevo);
        this.cantidad++;
    }

    /**
     * Reemplaza el dato de la posición indicada.
     *
     * @return el dato que había antes
     * @throws IndexOutOfBoundsException si indice < 0 o indice >= tamanio()
     */
    public T reemplazar(int indice, T dato) {
        if (indice < 0 || indice >= this.cantidad) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }
        Nodo<T> nodo = obtenerNodo(indice);
        T datoAnterior = nodo.getDato();
        nodo.setDato(dato);
        return datoAnterior;
    }

    // =====================================================================
    // ETAPA 6. Eliminar
    // =====================================================================

    /**
     * Elimina y retorna el primer elemento.
     * @throws NoSuchElementException si la lista está vacía
     */
    public T eliminarPrimero() {
        if (estaVacia()) {
            throw new NoSuchElementException("La lista está vacía");
        }
        T datoEliminado = this.cabeza.getDato();
        this.cabeza = this.cabeza.getSiguiente();
        this.cantidad--;
        return datoEliminado;
    }

    /**
     * Elimina y retorna el último elemento.
     * @throws NoSuchElementException si la lista está vacía
     */
    public T eliminarUltimo() {
        if (estaVacia()) {
            throw new NoSuchElementException("La lista está vacía");
        }
        if (this.cantidad == 1) {
            return eliminarPrimero();
        }
        Nodo<T> penultimo = obtenerNodo(this.cantidad - 2);
        T datoEliminado = penultimo.getSiguiente().getDato();
        penultimo.setSiguiente(null);
        this.cantidad--;
        return datoEliminado;
    }

    /**
     * Elimina y retorna el elemento de la posición indicada.
     * @throws IndexOutOfBoundsException si indice < 0 o indice >= tamanio()
     */
    public T eliminar(int indice) {
        if (indice < 0 || indice >= this.cantidad) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }
        if (indice == 0) {
            return eliminarPrimero();
        }
        Nodo<T> anterior = obtenerNodo(indice - 1);
        Nodo<T> nodoEliminado = anterior.getSiguiente();
        anterior.setSiguiente(nodoEliminado.getSiguiente());
        this.cantidad--;
        return nodoEliminado.getDato();
    }

    /**
     * Elimina la primera aparición del dato.
     * @return true si se eliminó algo, false si el dato no estaba
     */
    public boolean eliminarValor(T dato) {
        int indice = indiceDe(dato);
        if (indice == -1) {
            return false;
        }
        eliminar(indice);
        return true;
    }

    public void vaciar() {
        this.cabeza = null;
        this.cantidad = 0;
    }

    // =====================================================================
    // ETAPA 7. Representación en texto
    // =====================================================================

    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder("[");
        Nodo<T> actual = this.cabeza;
        while (actual != null) {
            texto.append(actual.getDato());
            if (actual.getSiguiente() != null) {
                texto.append(", ");
            }
            actual = actual.getSiguiente();
        }
        texto.append("]");
        return texto.toString();
    }

    // =====================================================================
    // ETAPA 8. Retos
    // =====================================================================

    /**
     * Invierte la lista EN SITIO: no cree nodos nuevos, solo cambie
     * las referencias 'siguiente' de los nodos existentes.
     */
    public void invertir() {
        Nodo<T> anterior = null;
        Nodo<T> actual = this.cabeza;
        while (actual != null) {
            Nodo<T> siguiente = actual.getSiguiente(); 
            actual.setSiguiente(anterior);
            anterior = actual;
            actual = siguiente;
        }
        this.cabeza = anterior;
    }

    /**
     * Retorna el elemento del medio. Si el tamaño es par, el segundo de los
     * dos centrales (ej.: [1,2,3,4] -> 3).
     *
     * @throws NoSuchElementException si la lista está vacía
     */
    public T obtenerMedio() {
        if (estaVacia()) {
            throw new NoSuchElementException("La lista está vacía");
        }
        Nodo<T> lento = this.cabeza;
        Nodo<T> rapido = this.cabeza;
        while (rapido != null && rapido.getSiguiente() != null) {
            lento = lento.getSiguiente();
            rapido = rapido.getSiguiente().getSiguiente();
        }
        return lento.getDato();
    }

    /**
     * Elimina los elementos repetidos dejando solo la primera aparición
     * de cada uno, conservando el orden. Ej.: [1,2,1,3,2] -> [1,2,3]
     */
    public void eliminarDuplicados() {
        Nodo<T> actual = this.cabeza;
        while (actual != null) {
            Nodo<T> anteriorRevisado = actual;
            Nodo<T> revisado = actual.getSiguiente();
            while (revisado != null) {
                if (Objects.equals(actual.getDato(), revisado.getDato())) {
                    anteriorRevisado.setSiguiente(revisado.getSiguiente());
                    this.cantidad--;
                } else {
                    anteriorRevisado = revisado;
                }
                revisado = anteriorRevisado.getSiguiente();
            }
            actual = actual.getSiguiente();
        }
    }
}