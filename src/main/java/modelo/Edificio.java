package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un edificio físico del campus UFRO.
 * Extiende {@link Ubicacion}. Un Edificio contiene muchas {@link Sala} (composición 1..*).
 *
 * @author Vicente Flores
 * @version 2.0
 */
public class Edificio extends Ubicacion {

    /** Identificador corto (ej: "R", "D", "RA"). */
    private String id;

    /** Número en el mapa institucional UFRO. */
    private int numMapa;

    /** Coordenada GPS — latitud del centro del edificio. */
    private double latitud;

    /** Coordenada GPS — longitud del centro del edificio. */
    private double longitud;

    /** Salas que pertenecen a este edificio. */
    private List<Sala> salas;

    /**
     * @param id          identificador corto
     * @param nombre      nombre oficial
     * @param descripcion descripción
     * @param numMapa     número en mapa UFRO
     * @param latitud     coordenada GPS latitud
     * @param longitud    coordenada GPS longitud
     */
    public Edificio(String id, String nombre, String descripcion,
                    int numMapa, double latitud, double longitud) {
        super(nombre, descripcion);
        this.id       = id;
        this.numMapa  = numMapa;
        this.latitud  = latitud;
        this.longitud = longitud;
        this.salas    = new ArrayList<>();
    }

    @Override
    public String getIdentificador() { return id; }

    /** Agrega una sala a este edificio. */
    public void agregarSala(Sala sala) { salas.add(sala); }

    /**
     * Busca una sala por código dentro de este edificio.
     *
     * @param codigo código de la sala
     * @return sala encontrada o null
     */
    public Sala buscarSalaPorCodigo(String codigo) {
        for (Sala s : salas)
            if (s.getCodigo().equalsIgnoreCase(codigo)) return s;
        return null;
    }

    public String       getId()                        { return id; }
    public int          getNumMapa()                   { return numMapa; }
    public double       getLatitud()                   { return latitud; }
    public void         setLatitud(double latitud)     { this.latitud = latitud; }
    public double       getLongitud()                  { return longitud; }
    public void         setLongitud(double longitud)   { this.longitud = longitud; }
    public List<Sala>   getSalas()                     { return salas; }
}
