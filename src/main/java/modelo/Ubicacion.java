package modelo;

/**
 * Clase abstracta que representa cualquier ubicación dentro del campus UFRO.
 * Superclase de {@link Edificio}, {@link Sala} y {@link PuntoReferencia}.
 *
 * @author Vicente Flores
 * @version 2.0
 */
public abstract class Ubicacion {

    /** Nombre descriptivo de la ubicación. */
    private String nombre;

    /** Descripción textual de la ubicación. */
    private String descripcion;

    /**
     * @param nombre      nombre de la ubicación
     * @param descripcion descripción breve
     */
    public Ubicacion(String nombre, String descripcion) {
        this.nombre      = nombre;
        this.descripcion = descripcion;
    }

    /**
     * Retorna el identificador único de esta ubicación.
     * Cada subclase define cómo se identifica.
     *
     * @return identificador único
     */
    public abstract String getIdentificador();

    public String getNombre()                    { return nombre; }
    public void   setNombre(String nombre)        { this.nombre = nombre; }
    public String getDescripcion()               { return descripcion; }
    public void   setDescripcion(String desc)    { this.descripcion = desc; }

    @Override
    public String toString() {
        return "[" + getIdentificador() + "] " + nombre;
    }
}
