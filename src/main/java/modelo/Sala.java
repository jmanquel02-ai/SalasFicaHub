package modelo;

/**
 * Representa una sala física dentro de un edificio del campus UFRO.
 * Extiende {@link Ubicacion}. Se conecta al grafo mediante {@code nodoId}.
 *
 * @author Vicente Flores
 * @version 2.0
 */
public class Sala extends Ubicacion {

    /** Código único (ej: "R101", "D201"). */
    private String codigo;

    /** ID del edificio al que pertenece. */
    private String edificioId;

    /** Piso donde se ubica (1 = primer nivel). */
    private int piso;

    /** Capacidad máxima de personas. */
    private int capacidad;

    /** ID del nodo del grafo más cercano a la entrada de la sala. */
    private String nodoId;

    /**
     * @param codigo      código único
     * @param edificioId  id del edificio contenedor
     * @param descripcion tipo de sala
     * @param piso        número de piso
     * @param capacidad   capacidad en personas
     * @param nodoId      nodo del grafo más cercano
     */
    public Sala(String codigo, String edificioId, String descripcion,
                int piso, int capacidad, String nodoId) {
        super(codigo, descripcion);
        this.codigo     = codigo;
        this.edificioId = edificioId;
        this.piso       = piso;
        this.capacidad  = capacidad;
        this.nodoId     = nodoId;
    }

    @Override
    public String getIdentificador() { return codigo; }

    public String getCodigo()               { return codigo; }
    public String getEdificioId()           { return edificioId; }
    public int    getPiso()                 { return piso; }
    public int    getCapacidad()            { return capacidad; }
    public String getNodoId()               { return nodoId; }
    public void   setNodoId(String nodoId)  { this.nodoId = nodoId; }
}
