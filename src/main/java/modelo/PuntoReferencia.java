package modelo;

/**
 * Representa un punto de referencia conocido en el campus UFRO.
 * Extiende {@link Ubicacion}. Ejemplos: C1, C3, Entrada principal.
 *
 * @author Vicente Flores
 * @version 2.0
 */
public class PuntoReferencia extends Ubicacion {

    /** Identificador (ej: "C1", "C3", "ENTRADA_PRINCIPAL"). */
    private String id;

    /** Coordenada GPS — latitud. */
    private double latitud;

    /** Coordenada GPS — longitud. */
    private double longitud;

    /** ID del nodo del grafo en este punto. */
    private String nodoId;

    /**
     * @param id          identificador
     * @param nombre      nombre visible
     * @param descripcion dónde está / cómo reconocerlo
     * @param latitud     coordenada GPS latitud
     * @param longitud    coordenada GPS longitud
     * @param nodoId      nodo del grafo en este punto
     */
    public PuntoReferencia(String id, String nombre, String descripcion,
                           double latitud, double longitud, String nodoId) {
        super(nombre, descripcion);
        this.id       = id;
        this.latitud  = latitud;
        this.longitud = longitud;
        this.nodoId   = nodoId;
    }

    @Override
    public String getIdentificador() { return id; }

    public String getId()        { return id; }
    public double getLatitud()   { return latitud; }
    public double getLongitud()  { return longitud; }
    public String getNodoId()    { return nodoId; }
}
