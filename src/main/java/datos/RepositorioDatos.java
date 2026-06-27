package datos;

import modelo.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.*;

/**
 * Repositorio central de datos de SalasFICA.
 * Lee edificios.json, salas.json y puntos.json desde resources/data/.
 * Proporciona métodos de consulta a los controladores.
 *
 * @author Jonathan Manquel
 * @version 2.0
 */
public class RepositorioDatos {

    private Map<String, Edificio>        edificios = new LinkedHashMap<>();
    private Map<String, Sala>            salas     = new LinkedHashMap<>();
    private Map<String, PuntoReferencia> puntos    = new LinkedHashMap<>();
    private Map<String, Nodo>            grafo     = new LinkedHashMap<>();

    /**
     * Carga todos los datos desde archivos externos al iniciar.
     *
     * @throws Exception si algún archivo falta o tiene formato incorrecto
     */
    public RepositorioDatos() throws Exception {
        cargarEdificios();
        cargarSalas();
        cargarPuntos();
        grafo = new GrafoLoader().cargarGrafo();
    }

    // ── Carga desde classpath ───────────────────────────────────

    private void cargarEdificios() throws Exception {
        JSONArray arr = parsearJSON("data/edificios.json");
        for (Object obj : arr) {
            JSONObject jo = (JSONObject) obj;
            Edificio e = new Edificio(
                (String) jo.get("id"),
                (String) jo.get("nombre"),
                (String) jo.get("descripcion"),
                ((Long)  jo.get("numMapa")).intValue(),
                ((Number)jo.get("latitud")).doubleValue(),
                ((Number)jo.get("longitud")).doubleValue()
            );
            edificios.put(e.getId(), e);
        }
    }

    private void cargarSalas() throws Exception {
        JSONArray arr = parsearJSON("data/salas.json");
        for (Object obj : arr) {
            JSONObject jo = (JSONObject) obj;
            Sala s = new Sala(
                (String) jo.get("codigo"),
                (String) jo.get("edificioId"),
                (String) jo.get("descripcion"),
                ((Long)  jo.get("piso")).intValue(),
                ((Long)  jo.get("capacidad")).intValue(),
                (String) jo.get("nodoId")
            );
            salas.put(s.getCodigo().toUpperCase(), s);
            Edificio ed = edificios.get(s.getEdificioId());
            if (ed != null) ed.agregarSala(s);
        }
    }

    private void cargarPuntos() throws Exception {
        JSONArray arr = parsearJSON("data/puntos.json");
        for (Object obj : arr) {
            JSONObject jo = (JSONObject) obj;
            PuntoReferencia p = new PuntoReferencia(
                (String) jo.get("id"),
                (String) jo.get("nombre"),
                (String) jo.get("descripcion"),
                ((Number)jo.get("latitud")).doubleValue(),
                ((Number)jo.get("longitud")).doubleValue(),
                (String) jo.get("nodoId")
            );
            puntos.put(p.getId(), p);
        }
    }

    /**
     * Lee un archivo JSON desde el classpath y lo parsea como JSONArray.
     *
     * @param path ruta relativa dentro de resources (ej: "data/salas.json")
     * @return JSONArray con el contenido
     */
    private JSONArray parsearJSON(String path) throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        if (is == null)
            throw new IOException("No se encontró " + path + " en resources");
        try (InputStreamReader reader = new InputStreamReader(is)) {
            return (JSONArray) new JSONParser().parse(reader);
        }
    }

    // ── Consultas ───────────────────────────────────────────────

    /** * Retorna una sala por código (insensible a mayúsculas).
     * Maneja de forma segura valores nulos o vacíos.
     */
    public Sala buscarSala(String codigo) {
        // Validación defensiva para evitar el NullPointerException
        if (codigo == null || codigo.trim().isEmpty()) {
            return null;
        }
        return salas.get(codigo.trim().toUpperCase());
    }

    /** @return todas las salas */
    public Collection<Sala> getSalas()               { return salas.values(); }

    /**
     * Retorna la lista de salas que pertenecen a un edificio específico,
     * ordenadas por código alfabéticamente.
     *
     * @param idEdificio id del edificio (ej: "D", "R1", "RA")
     * @return lista de salas del edificio, o lista vacía si no hay
     */
    public List<Sala> getSalasPorEdificio(String idEdificio) {
        List<Sala> resultado = new ArrayList<>();
        for (Sala s : salas.values()) {
            if (s.getEdificioId().equalsIgnoreCase(idEdificio)) {
                resultado.add(s);
            }
        }
        resultado.sort((a, b) -> a.getCodigo().compareTo(b.getCodigo()));
        return resultado;
    }

    /** @return todos los edificios */
    public Collection<Edificio> getEdificios()       { return edificios.values(); }

    /** @return todos los puntos de referencia */
    public Collection<PuntoReferencia> getPuntos()   { return puntos.values(); }

    /** @return nodo por id, o null */
    public Nodo getNodo(String id)                   { return grafo.get(id); }

    /** @return grafo completo */
    public Map<String, Nodo> getGrafo()              { return grafo; }
}
