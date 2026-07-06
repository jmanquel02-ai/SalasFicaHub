package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una intersección o punto de paso en el campus UFRO.
 * Los nodos forman el grafo sobre el cual {@link Ruta} calcula caminos.
 * Relación: un Nodo conoce sus vecinos directos (1..* ↔ 1..* Nodo).
 *
 * @author Jonathan Manquel
 * @version 2.0
 */
public class Nodo {

    /** Identificador único (ej: "N001"). */
    private String id;

    /** Coordenada GPS — latitud de la intersección. */
    private double latitud;

    /** Coordenada GPS — longitud de la intersección. */
    private double longitud;

    /** Descripción del punto (ej: "Frente a entrada Pabellón R"). */
    private String descripcion;

    /** Nodos directamente conectados a este. */
    private List<Nodo> vecinos;

    /**
     * @param id          identificador único
     * @param latitud     coordenada GPS latitud
     * @param longitud    coordenada GPS longitud
     * @param descripcion descripción del punto
     */
    public Nodo(String id, double latitud, double longitud, String descripcion) {
        this.id          = id;
        this.latitud     = latitud;
        this.longitud    = longitud;
        this.descripcion = descripcion;
        this.vecinos     = new ArrayList<>();
    }

    /**
     * Agrega un vecino si no existe ya en la lista.
     *
     * @param vecino nodo a conectar
     */
    public void agregarVecino(Nodo vecino) {
        if (!vecinos.contains(vecino)) vecinos.add(vecino);
    }

    public String      getId()           { return id; }
    public double      getLatitud()      { return latitud; }
    public double      getLongitud()     { return longitud; }
    public String      getDescripcion()  { return descripcion; }
    public List<Nodo>  getVecinos()      { return vecinos; }

    @Override
    public String toString()             { return "Nodo[" + id + "]"; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Nodo)) return false;
        return id.equals(((Nodo) o).id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }
}
